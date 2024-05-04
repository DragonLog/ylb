package com.zcx.pay.util;

import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * HttpUtil 工具类
 */
public class HttpUtil {

	private static final Log LOGGER = LogFactory.getLog(HttpUtil.class);
	
	private static PoolingHttpClientConnectionManager connManager;
	private static RequestConfig requestConfig;
	
	static{
		try {
			SSLContext sslcontext = createIgnoreVerifySSL();
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		    sslContext.init(null, null, null);
		    SSLContext.setDefault(sslContext);	
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
			        .<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
			        .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
			connManager = new PoolingHttpClientConnectionManager(
			        socketFactoryRegistry);
			// 连接池超时时间使用connect超时时间
			requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(1000)
					.setConnectTimeout(1000)
					.setSocketTimeout(5000).build();
		} catch (Exception e) {
			LOGGER.error(" [XPAY-SDK] init connectionManager or requestConfig error !!! ",e);
			e.printStackTrace();
		}
	}

	public static String doPostJsonRequest(String reqeustString, String url,
			int connectTimeout, int socketTimeOut) throws Exception {

		CloseableHttpResponse response = null;
		try {
			
			changeRequestConfig(connectTimeout,socketTimeOut);
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new StringEntity(reqeustString, ContentType.APPLICATION_JSON));
			response = httpclient.execute(httpPost);
			// get http status code
			int resStatu = response.getStatusLine().getStatusCode();
			String responseString = null;
			if (resStatu == HttpStatus.SC_OK) {
				responseString = EntityUtils.toString(response.getEntity());
			} else {
				throw new Exception(url + ",the statusCode is " + resStatu);
			}
			return responseString;
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}
	
	/**
     * 绕过验证
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                     String paramString) throws CertificateException {}


            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                     String paramString) throws CertificateException {}


            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

	public static String doPostJsonRequestByHttps(String reqeustString, String url,
			int connectTimeout, int socketTimeOut) {
		long startTime = System.currentTimeMillis();
		CloseableHttpResponse response = null;
        String responseString = null;
		try {
			
			changeRequestConfig(connectTimeout,socketTimeOut);
			CloseableHttpClient httpsClient = HttpClients.custom().setConnectionManager(connManager).build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setConfig(requestConfig);
			httpPost.setEntity(new StringEntity(reqeustString, ContentType.APPLICATION_JSON));
			response = httpsClient.execute(httpPost);
			// get http status code
			int resStatu = response.getStatusLine().getStatusCode();
			responseString = null;
			if (resStatu == HttpStatus.SC_OK) {
				responseString = EntityUtils.toString(response.getEntity());
			} else {
				throw new Exception(url + ",the statusCode is " + resStatu);
			}
			LOGGER.info(String.format("response data : [ %s ] , time consuming : [ %s ] ms !! ",responseString
					,(System.currentTimeMillis()- startTime)));
			return responseString;
		}catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseString;
    }
	
	/**
	 * 修改默认超时时间
	 * @param connectionTime
	 * @param soTimeout
	 */
	private static void changeRequestConfig(int connectionTime,int soTimeout){
		if(connectionTime != requestConfig.getConnectionRequestTimeout()  
				|| soTimeout != requestConfig.getSocketTimeout()){
			requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(connectionTime)
					.setConnectTimeout(connectionTime)
					.setSocketTimeout(soTimeout).build();
		}
	}
}

import axios from "axios"
import qs from "qs"
import layx from "vue-layx"

axios.defaults.baseURL = "http://localhost:8000/api";
axios.defaults.timeout = 5000;

function doGet(url, params) {
	return axios({
		url:url,
		method:"get",
		params:params
	});
}

function doPost(url, params) {
	return axios({
		url:url,
		method:"post",
		data:params
	});
}

function doPost1(url, params) {
	let requestData = qs.stringify(params);
	return axios.post(url, requestData);
}

axios.interceptors.request.use(function(config) {
	console.log("request拦截器" + config.url);
	if (config.url == "/v1/user/realName" || config.url == "/v1/user/userCenter" || config.url == "/v1/recharge/records" || config.url == "/v1/invest/records" || config.url == "/v1/income/records" || config.url == "/v1/invest/product") {
		let storageToken = window.localStorage.getItem("token");
		let userInfo = window.localStorage.getItem("userInfo");
		if (storageToken && userInfo) {
			config.headers["Authorization"] = "Bearer " + storageToken;
			config.headers["uid"] = JSON.parse(userInfo).uid;
		}
	}
	return config;
}, function(err) {
	console.log("request拦截器" + err);
});

axios.interceptors.response.use(function(resp) {
	if(resp && resp.data.code != 200) {
		let code = resp.data.code;
		if (code == 3000) {
			window.location.href = "/page/user/login";
		} else {
			console.log("response拦截器")
			layx.msg(resp.data.msg, {dialogIcon:"warn", position:"ct"});
		}
	}
	return resp;
}, function(err) {
	console.log("response拦截器" + err);
	window.location.href = "/";
})

export {doGet, doPost, doPost1}
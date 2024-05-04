package com.zcx.dataService.service;

import com.zcx.api.model.ProductInfo;
import com.zcx.api.pojo.MultiProduct;
import com.zcx.api.service.ProductService;
import com.zcx.common.constants.YLBConstant;
import com.zcx.common.util.CommonUtil;
import com.zcx.dataService.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductService.class, version = "1.0")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize) {

        List<ProductInfo> productInfos = new ArrayList<>();
        if (pType == 0 || pType == 1 || pType == 2) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            productInfos = productInfoMapper.selectByTypeLimit(pType, offset, pageSize);
        }



        return productInfos;
    }

    @Override
    public Integer queryRecordNumsByType(Integer pType) {
        Integer counts = 0;
        if (pType == 0 || pType == 1 || pType == 2) {
            counts = productInfoMapper.selectCountByType(pType);
        }
        return counts;
    }

    @Override
    public MultiProduct queryIndexPageProducts() {
        MultiProduct result = new MultiProduct();
        List<ProductInfo> xinShouBaoList = productInfoMapper.selectByTypeLimit(YLBConstant.PRODUCT_TYPE_XINSHOUBAO, 0, 1);
        List<ProductInfo> youXuanList = productInfoMapper.selectByTypeLimit(YLBConstant.PRODUCT_TYPE_YOUXUAN, 0, 3);
        List<ProductInfo> sanBiaoList = productInfoMapper.selectByTypeLimit(YLBConstant.PRODUCT_TYPE_SANBIAO, 0, 3);
        result.setXinShouBao(xinShouBaoList);
        result.setYouXuan(youXuanList);
        result.setSanBiao(sanBiaoList);
        return result;
    }

    @Override
    public ProductInfo queryById(Integer id) {
        ProductInfo productInfo = null;
        if (id != null && id > 0) {
             productInfo = productInfoMapper.selectByPrimaryKey(id);
        }
        return productInfo;
    }
}

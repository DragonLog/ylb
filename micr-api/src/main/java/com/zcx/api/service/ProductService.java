package com.zcx.api.service;

import com.zcx.api.model.ProductInfo;
import com.zcx.api.pojo.MultiProduct;

import java.util.List;

public interface ProductService {

    List<ProductInfo> queryByTypeLimit(Integer pType, Integer pageNo, Integer pageSize);

    Integer queryRecordNumsByType(Integer pType);

    MultiProduct queryIndexPageProducts();

    ProductInfo queryById(Integer id);

}

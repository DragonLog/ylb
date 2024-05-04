package com.zcx.api.service;

import com.zcx.api.model.BidInfo;
import com.zcx.api.pojo.BidInfoProduct;
import com.zcx.api.pojo.InvestInfo;

import java.math.BigDecimal;
import java.util.List;

public interface InvestService {

    List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize);

    List<InvestInfo> queryByUid(Integer uid, Integer pageNo, Integer pageSize);

    int investProduct(Integer uid, Integer productId, BigDecimal money);
}

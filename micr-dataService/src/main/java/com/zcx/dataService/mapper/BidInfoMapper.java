package com.zcx.dataService.mapper;

import com.zcx.api.model.BidInfo;
import com.zcx.api.pojo.BidInfoProduct;
import com.zcx.api.pojo.InvestInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {

    BigDecimal selectSumBidMoney();

    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    List<BidInfoProduct> selectByProductId(@Param("productId") Integer productId, @Param("offset") Integer offset, @Param("rows") Integer rows);

    List<InvestInfo> selectByUid(@Param("uid") Integer uid, @Param("offset") int offset, @Param("rows") Integer rows);

    List<BidInfo> selectByProdId(@Param("productId") Integer productId);
}
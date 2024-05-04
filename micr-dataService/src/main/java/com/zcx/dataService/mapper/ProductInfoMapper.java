package com.zcx.dataService.mapper;

import com.zcx.api.model.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductInfoMapper {

    BigDecimal selectAvgRate();

    List<ProductInfo> selectByTypeLimit(@Param("ptype") Integer ptype, @Param("offset") Integer offset, @Param("rows") Integer rows);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);

    Integer selectCountByType(@Param("ptype") Integer pType);

    ProductInfo selectByPrimaryKeyForUpdate(@Param("productId") Integer productId);

    int updateLeftProductMoney(@Param("productId") Integer productId, @Param("money") BigDecimal money);

    int updateSelled(@Param("productId") Integer productId);

    List<ProductInfo> selectFullTimeProducts(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int updateStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);
}

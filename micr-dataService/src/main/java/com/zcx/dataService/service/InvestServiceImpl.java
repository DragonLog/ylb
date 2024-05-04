package com.zcx.dataService.service;

import com.zcx.api.model.BidInfo;
import com.zcx.api.model.FinanceAccount;
import com.zcx.api.model.ProductInfo;
import com.zcx.api.pojo.BidInfoProduct;
import com.zcx.api.pojo.InvestInfo;
import com.zcx.api.service.InvestService;
import com.zcx.common.constants.YLBConstant;
import com.zcx.common.util.CommonUtil;
import com.zcx.dataService.mapper.BidInfoMapper;
import com.zcx.dataService.mapper.FinanceAccountMapper;
import com.zcx.dataService.mapper.ProductInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImpl implements InvestService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public List<BidInfoProduct> queryBidListByProductId(Integer productId, Integer pageNo, Integer pageSize) {
        List<BidInfoProduct> bidList = new ArrayList<>();
        if (productId != null && productId > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            bidList = bidInfoMapper.selectByProductId(productId, offset, pageSize);
        }
        return bidList;
    }

    @Override
    public List<InvestInfo> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<InvestInfo> records = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            records = bidInfoMapper.selectByUid(uid, offset, pageSize);
        }
        return records;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int investProduct(Integer uid, Integer productId, BigDecimal money) {
        int result = 0;
        int rows = 0;
        if (uid != null && uid > 0 && productId != null && productId > 0 && money != null && money.intValue() >= 100 && money.intValue() % 100 == 0) {
            FinanceAccount financeAccount = financeAccountMapper.selectByUidForUpdate(uid);
            if (financeAccount != null) {
                if (CommonUtil.ge(financeAccount.getAvailableMoney(), money)) {
                    //这里我也给上锁了
                    ProductInfo productInfo = productInfoMapper.selectByPrimaryKeyForUpdate(productId);
                    if (productInfo != null && productInfo.getProductStatus() == YLBConstant.PRODUCT_STATUS_SELLING) {
                        //对于UPDATE、DELETE和INSERT语句，InnoDB会自动给涉及的数据集加上排他锁
                        if (CommonUtil.ge(productInfo.getLeftProductMoney(), money) && CommonUtil.ge(money, productInfo.getBidMinLimit()) && CommonUtil.ge(productInfo.getBidMaxLimit(), money)) {
                            rows = financeAccountMapper.updateAvailableMoneyByInvest(uid, money);
                            if (rows < 1) {
                                throw new RuntimeException("投资更新账号资金失败");
                            }

                            rows = productInfoMapper.updateLeftProductMoney(productId, money);
                            if (rows < 1) {
                                throw new RuntimeException("投资更新产品剩余金额失败");
                            }

                            BidInfo bidInfo = new BidInfo();
                            bidInfo.setBidMoney(money);
                            bidInfo.setBidStatus(YLBConstant.INVEST_STATUS_SUCC);
                            bidInfo.setBidTime(new Date());
                            bidInfo.setProdId(productId);
                            bidInfo.setUid(uid);
                            bidInfoMapper.insertSelective(bidInfo);

                            ProductInfo dbProductInfo = productInfoMapper.selectByPrimaryKeyForUpdate(productId);
                            if (dbProductInfo.getLeftProductMoney().compareTo(new BigDecimal("0")) == 0) {
                                rows = productInfoMapper.updateSelled(productId);
                                if (rows < 1) {
                                    throw new RuntimeException("投资更新产品满标失败");
                                }
                            }

                            result = 1;
                        }
                    } else {
                        result = 4;
                    }
                } else {
                    result = 3;
                }
            } else {
                result = 2;
            }
        }
        return result;
    }
}

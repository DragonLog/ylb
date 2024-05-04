package com.zcx.dataService.service;

import com.zcx.api.model.BidInfo;
import com.zcx.api.model.IncomeRecord;
import com.zcx.api.model.ProductInfo;
import com.zcx.api.pojo.IncomeInfo;
import com.zcx.api.service.IncomeService;
import com.zcx.common.constants.YLBConstant;
import com.zcx.common.util.CommonUtil;
import com.zcx.dataService.mapper.BidInfoMapper;
import com.zcx.dataService.mapper.FinanceAccountMapper;
import com.zcx.dataService.mapper.IncomeRecordMapper;
import com.zcx.dataService.mapper.ProductInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class, version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void generateIncomePlan() {
        Date currentDate = new Date();

        Date beginTime = DateUtils.truncate(DateUtils.addDays(currentDate, -1), Calendar.DATE);
        Date endTime = DateUtils.truncate(currentDate, Calendar.DATE);

        BigDecimal dayRate = null;
        BigDecimal income = null;
        BigDecimal cycle = null;
        Date incomeDate = null;
        int rows = 0;

        List<ProductInfo> productInfoList = productInfoMapper.selectFullTimeProducts(beginTime, endTime);
        for (ProductInfo product : productInfoList) {
            dayRate = product.getRate().divide(new BigDecimal(360), 10, RoundingMode.HALF_UP).divide(new BigDecimal(100), 10, RoundingMode.HALF_UP);
            if (product.getProductType() == YLBConstant.PRODUCT_TYPE_XINSHOUBAO) {
                cycle = new BigDecimal(product.getCycle());
                incomeDate = DateUtils.addDays(product.getProductFullTime(), 1 + product.getCycle());
            } else {
                cycle = new BigDecimal(product.getCycle() * 30);
                incomeDate = DateUtils.addDays(product.getProductFullTime(), 1 + product.getCycle() * 30);
            }
            System.out.println("##############" + dayRate + "####################");
            List<BidInfo> bidInfoList = bidInfoMapper.selectByProdId(product.getId());
            for (BidInfo bid : bidInfoList) {
                income = bid.getBidMoney().multiply(cycle).multiply(dayRate);
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bid.getId());
                incomeRecord.setBidMoney(bid.getBidMoney());
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeStatus(YLBConstant.INCOME_STATUS_PLAN);
                incomeRecord.setProdId(product.getId());
                incomeRecord.setIncomeMoney(income);
                incomeRecord.setUid(bid.getUid());
                incomeRecordMapper.insertSelective(incomeRecord);
            }
            rows = productInfoMapper.updateStatus(product.getId(), YLBConstant.PRODUCT_STATUS_PLAN);
            if (rows < 1) {
                throw new RuntimeException("生成收益计划，更新产品状态失败");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void generateIncomeBack() {
        int rows = 0;

        Date currentDate = new Date();
        Date expiredDate = DateUtils.truncate(DateUtils.addDays(currentDate, -1), Calendar.DATE);

        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectExpiredIncome(expiredDate);
        for (IncomeRecord incomeRecord : incomeRecordList) {
            rows = financeAccountMapper.updateAvailableMoneyByIncomeBack(incomeRecord.getUid(), incomeRecord.getBidMoney(), incomeRecord.getIncomeMoney());
            if (rows < 1) {
                throw new RuntimeException("收益返还更新账户资金失败");
            }
            incomeRecord.setIncomeStatus(YLBConstant.INCOME_STATUS_BACK);
            rows = incomeRecordMapper.updateByPrimaryKey(incomeRecord);
            if (rows < 1) {
                throw new RuntimeException("收益返还更新收益记录状态失败");
            }
        }
    }

    @Override
    public List<IncomeInfo> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<IncomeInfo> records = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            records = incomeRecordMapper.selectByUid(uid, offset, pageSize);
        }
        return records;
    }
}

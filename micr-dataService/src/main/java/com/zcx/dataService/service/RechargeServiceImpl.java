package com.zcx.dataService.service;

import com.zcx.api.model.RechargeRecord;
import com.zcx.api.service.RechargeService;
import com.zcx.common.constants.YLBConstant;
import com.zcx.common.util.CommonUtil;
import com.zcx.dataService.mapper.FinanceAccountMapper;
import com.zcx.dataService.mapper.RechargeRecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeService.class, version = "1.0")
public class RechargeServiceImpl implements RechargeService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<RechargeRecord> queryByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<RechargeRecord> records = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            int offset = (pageNo - 1) * pageSize;
            records = rechargeRecordMapper.selectByUid(uid, offset, pageSize);
        }
        return records;
    }

    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized int handleFastMoneyNotify(String orderId, String payAmount, String payResult) {
        int result = 0;
        int rows = 0;
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByRechargeNo(orderId);
        if (rechargeRecord != null) {
            if (rechargeRecord.getRechargeStatus() == YLBConstant.RECHARGE_STATUS_PROCESSING) {
                String fen = rechargeRecord.getRechargeMoney().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString();
                if (fen.equals(payAmount)) {
                    if ("10".equals(payResult)) {
                        rows = financeAccountMapper.updateAvailableMoneyByRecharge(rechargeRecord.getUid(), rechargeRecord.getRechargeMoney());
                        if (rows < 1) {
                            throw new RuntimeException("充值更新资金账户失败");
                        }
                        rechargeRecord.setRechargeStatus(YLBConstant.RECHARGE_STATUS_SUCCESS);
                        rows = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord); //这里直接用了，不再新起一个改状态的方法了，太累了
                        if (rows < 1) {
                            throw new RuntimeException("更新充值记录状态失败");
                        }
                        result = 1;
                    } else {
                        rechargeRecord.setRechargeStatus(YLBConstant.RECHARGE_STATUS_FAIL);
                        rows = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord); //这里直接用了，不再新起一个改状态的方法了，太累了
                        if (rows < 1) {
                            throw new RuntimeException("更新充值记录状态失败");
                        }
                        result = 2;
                    }
                } else {
                    result = 4;
                }
            } else {
                result = 3;
            }
        }
        return result;
    }
}

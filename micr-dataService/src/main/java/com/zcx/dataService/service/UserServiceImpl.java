package com.zcx.dataService.service;

import com.zcx.api.model.FinanceAccount;
import com.zcx.api.model.User;
import com.zcx.api.pojo.UserAccountInfo;
import com.zcx.api.service.UserService;
import com.zcx.common.util.CommonUtil;
import com.zcx.dataService.mapper.FinanceAccountMapper;
import com.zcx.dataService.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Value("${ylb.config.yan}")
    private String salt;

    @Override
    public User queryByPhone(String phone) {
        User user = null;
        if (CommonUtil.checkPhone(phone)) {
            user = userMapper.selectByPhone(phone);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized int userRegister(String phone, String password) {
        System.out.println("##################" + salt + "####################");
        int result = 0;
        if (CommonUtil.checkPhone(phone) && password != null && password.length() == 32) {

            User queryUser = userMapper.selectByPhone(phone);
            if (queryUser == null) {
                User user = new User();
                user.setPhone(phone);
                user.setLoginPassword(DigestUtils.md5Hex(password + salt));
                user.setAddTime(new Date());
                userMapper.insertReturnPrimaryKey(user);

                FinanceAccount account = new FinanceAccount();
                account.setUid(user.getId());
                account.setAvailableMoney(new BigDecimal("0"));
                financeAccountMapper.insertSelective(account);
                result = 1;
            } else {
                result = 2;
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User userLogin(String phone, String password) {
        User user = null;
        if (CommonUtil.checkPhone(phone) && password != null && password.length() == 32) {

            user = userMapper.selectLogin(phone, DigestUtils.md5Hex(password + salt));
            if (user != null) {
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
        return user;
    }

    @Override
    public boolean modifyRealName(String phone, String name, String idCard) {
        int rows = 0;
        if (CommonUtil.checkPhone(phone) && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(idCard)) {
            rows = userMapper.updateRealName(phone, name, idCard);
        }
        return rows > 0;
    }

    @Override
    public UserAccountInfo queryUserAllInfo(Integer uid) {
        UserAccountInfo info = null;
        if (uid != null && uid > 0) {
            info = userMapper.selectUserAccountById(uid);
        }
        return info;
    }

    @Override
    public User queryById(Integer uid) {
        User user = null;
        if (uid != null && uid > 0) {
            user = userMapper.selectByPrimaryKey(uid);
        }
        return user;
    }
}

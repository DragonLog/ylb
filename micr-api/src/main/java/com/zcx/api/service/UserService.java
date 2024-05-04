package com.zcx.api.service;

import com.zcx.api.model.User;
import com.zcx.api.pojo.UserAccountInfo;

public interface UserService {

    User queryByPhone(String phone);

    int userRegister(String phone, String password);

    User userLogin(String phone, String password);

    boolean modifyRealName(String phone, String name, String idCard);

    UserAccountInfo queryUserAllInfo(Integer uid);

    User queryById(Integer uid);
}

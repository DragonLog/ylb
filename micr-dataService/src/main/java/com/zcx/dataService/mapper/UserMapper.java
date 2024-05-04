package com.zcx.dataService.mapper;

import com.zcx.api.model.User;
import com.zcx.api.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int selectCountUsers();

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPhone(@Param("phone") String phone);

    int insertReturnPrimaryKey(User user);

    User selectLogin(@Param("phone") String phone, @Param("password") String password);

    int updateRealName(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);

    UserAccountInfo selectUserAccountById(@Param("uid") Integer uid);
}
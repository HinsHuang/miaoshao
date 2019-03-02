package com.hins.service;

import com.hins.error.BusinessException;
import com.hins.service.model.UserModel;

public interface UserService {
    //通过id来获取用户对象的方法
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     * @param telphone 用户注册手机
     * @param encrptPassword 加密后的密码
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
}

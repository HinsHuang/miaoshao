package com.hins.service;

import com.hins.service.model.UserModel;

public interface UserService {
    //通过id来获取用户对象的方法
    UserModel getUserById(Integer id);
}

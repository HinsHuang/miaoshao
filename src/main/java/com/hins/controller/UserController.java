package com.hins.controller;

import com.hins.controller.viemobject.UserVO;
import com.hins.error.BusinessException;
import com.hins.error.EmBusinessError;
import com.hins.response.CommonReturnType;
import com.hins.service.UserService;
import com.hins.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户获取otp接口
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {
        //按照一定规格生产otp
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+= 10000; //[100000 to 199999]
        String optCode = String.valueOf(randomInt);

        //将otp验证码同对应的手机号关联起来，使用httpsession的方式绑定他的手机号与otpCode
        httpServletRequest.getSession().setAttribute(telphone, optCode);

        //将otp验证码通过短信通道发送给用户，省略
        System.out.println("telphone = " + telphone + " & optCode = " + optCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用service服务获得相应id的用户并返回给前端
        UserModel userModel = userService.getUserById(id);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIT);
        }
        UserVO userVO = convertFromUserModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

}

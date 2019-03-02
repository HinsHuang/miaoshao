package com.hins.controller;

import com.hins.controller.viemobject.UserVO;
import com.hins.error.BusinessException;
import com.hins.error.EmBusinessError;
import com.hins.response.CommonReturnType;
import com.hins.service.UserService;
import com.hins.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "");
        }

        //用户登录服务，用来校验用户登录是否合法
        String encrptPassword = encodeByMD5(password);
        UserModel userModel = userService.validateLogin(telphone, encrptPassword);
        //将登陆凭证加入到用户成功的session内
        httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);
    }


    //用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "gender") Integer gender) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和optcode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码错误");
        }

        //用户注册的流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender.byteValue());
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byTelphone");
        userModel.setEncrptPassword(this.encodeByMD5(password));
        userService.register(userModel);

        return CommonReturnType.create(null);
    }

    private String encodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String encrptStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return encrptStr;
    }

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

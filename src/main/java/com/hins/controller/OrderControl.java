package com.hins.controller;

import com.hins.error.BusinessException;
import com.hins.error.EmBusinessError;
import com.hins.response.CommonReturnType;
import com.hins.service.OrderService;
import com.hins.service.model.OrderModel;
import com.hins.service.model.UserModel;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderControl extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType create(@RequestParam(name = "itemId") Integer itemId,
                                   @RequestParam(name = "promoId", required = false) Integer promoId,
                                   @RequestParam(name = "amount") Integer amount) throws BusinessException {
        //获取用户的登录信息
        Boolean islogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (islogin == null || !islogin ) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel loginUser = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(loginUser.getId(), itemId, promoId, amount);

        return CommonReturnType.create(null);

    }
}

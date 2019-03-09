package com.hins.service;

import com.hins.error.BusinessException;
import com.hins.service.model.OrderModel;

public interface OrderService {

    //使用1.通过前端传过来秒杀活动id，然后下单接口校验活动id是否属于该商品并且活动已经开始
    //2. 直接在下单接口内判断该商品是否有秒杀活动，若存在进行中的则以秒杀价格下单
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}

package com.hins.service;

import com.hins.error.BusinessException;
import com.hins.service.model.OrderModel;

public interface OrderService {

    OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException;
}

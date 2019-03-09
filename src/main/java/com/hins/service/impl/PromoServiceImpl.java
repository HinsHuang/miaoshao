package com.hins.service.impl;

import com.hins.dao.PromoDOMapper;
import com.hins.domain.PromoDO;
import com.hins.service.PromoService;
import com.hins.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDOMapper promoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的促销活动
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel = converFromDataObject(promoDO);
        if (promoModel == null) {
            return null;
        }
        //判断秒杀活动是否即将开始或者正在进行中
        DateTime now = new DateTime();
        if (promoModel.getStartDate().isAfterNow()) {
            //活动还未开始
            promoModel.setStatus(1);
        } else if (promoModel.getEndDate().isBeforeNow()) {
            //活动已经结束
            promoModel.setStatus(3);
        } else {
            //活动正在进行中
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel converFromDataObject(PromoDO promoDO) {
        if (promoDO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
        return promoModel;
    }
}

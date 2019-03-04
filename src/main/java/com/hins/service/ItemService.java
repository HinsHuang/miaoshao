package com.hins.service;

import com.hins.error.BusinessException;
import com.hins.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表
    List<ItemModel> listItem();

    //商品查询
    ItemModel getItemById(Integer id);
}

package com.kzm.api;

import cn.kzm.manage.dto.front.ProductDet;
import cn.kzm.manage.pojo.TbPanel;

import java.util.List;

public interface ContentService {

    /**
     * 获取首页数据
     * @return
     */
   List<TbPanel> getHome();

    ProductDet getProductDet(Long productId);

    Boolean delCart(String userId, String itemId);
}

package cn.kzm.sso.service;

import cn.kzm.manage.dto.front.CartProduct;

import java.util.List;

public interface CartService {

    /**
     * 添加
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    int addCart(long userId,long itemId,int num);

    List<CartProduct> getCartList(Long userId);

    int cartEdit(Long userId, Long productId, int productNum, String checked);

    int editCartCheckAll(String userId, String checked);
}

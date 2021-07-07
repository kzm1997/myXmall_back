package cn.kzm.sso.service;

public interface CartService {

    /**
     * 添加
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    int addCart(long userId,long itemId,int num);
}

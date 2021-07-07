package com.example.myxmallssoservice.serviceImpl;

import cn.kzm.sso.service.CartService;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public int addCart(long userId, long itemId, int num) {

        return 0;
    }
}

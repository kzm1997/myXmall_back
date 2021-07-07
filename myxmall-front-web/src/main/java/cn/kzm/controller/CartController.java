package cn.kzm.controller;

import cn.kzm.common.dto.Result;
import cn.kzm.common.utils.ResultUtil;
import cn.kzm.manage.dto.front.Cart;
import cn.kzm.sso.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/user/addCart")
    @ApiOperation("添加购物车")
    public Result<Object> addCart(@RequestBody Cart cart) {
        int result =cartService.addCart(cart.getUserId(),cart.getProductId(),cart.getProductNum());
        return new ResultUtil<>().setData(result);
    }
}

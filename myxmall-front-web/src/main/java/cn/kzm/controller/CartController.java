package cn.kzm.controller;

import cn.kzm.common.dto.Result;
import cn.kzm.common.utils.ResultUtil;
import cn.kzm.manage.dto.front.Cart;
import cn.kzm.manage.dto.front.CartProduct;
import cn.kzm.sso.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kzm.api.ContentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Reference
    private CartService cartService;

    @Reference
    private ContentService contentService;

    @PostMapping("/user/addCart")
    @ApiOperation("添加购物车")
    public Result<Object> addCart(@RequestBody Cart cart) {
        int result = cartService.addCart(cart.getUserId(), cart.getProductId(), cart.getProductNum());
        return new ResultUtil<>().setData(result);
    }


    @RequestMapping("/goods/cartDel")
    public Result delCart(@RequestParam String userId, @RequestParam String itemId) {
        Boolean b = contentService.delCart(userId, itemId);
        if (b) {
            return new ResultUtil().BuildSuccess();
        }
        return new ResultUtil().BuidFailure();
    }

    @RequestMapping("/goods/cartList")
    @ApiOperation("获取购物车列表")
    public Result<List<CartProduct>> cartList(@RequestBody Cart cart) {
         List<CartProduct> list= cartService.getCartList(cart.getUserId());
         return new ResultUtil().setData(list);
    }


    @RequestMapping("/goods/cartEdit")
    @ApiOperation("编辑购物车")
    public Result cartEdit(@RequestBody Cart cart){
       int result=cartService.cartEdit(cart.getUserId(),cart.getProductId(),
               cart.getProductNum(),cart.getChecked());
       if (result==1){
           return new ResultUtil().BuildSuccess();
       }
       return new ResultUtil().BuidFailure();
    }

    @RequestMapping("/goods/editCheckAll")
    public Result editCartAll(@RequestParam String userId,@RequestParam String checked){
        int result= cartService.editCartCheckAll(userId, checked);
        return new ResultUtil().BuildSuccess();
    }


}

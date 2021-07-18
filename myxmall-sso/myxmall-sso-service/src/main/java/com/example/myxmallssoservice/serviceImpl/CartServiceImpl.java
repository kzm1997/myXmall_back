package com.example.myxmallssoservice.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.kzm.common.myxmallConst.MyXmallConst;
import cn.kzm.manage.dto.front.CartProduct;
import cn.kzm.manage.pojo.TbItem;
import cn.kzm.sso.service.CartService;
import com.alibaba.dubbo.config.annotation.Service;
import com.example.myxmallssoservice.Mapper.TbItemMapper;
import com.example.myxmallssoservice.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;


    @Autowired
    private RedisUtils redisUtils;

    @Override
    public int addCart(long userId, long itemId, int num) {

        boolean b = redisUtils.hHasKey(MyXmallConst.CART_PRE + userId, String.valueOf(itemId));

        if (b) {
            //如果存在则数量相加
            Object cart = redisUtils.hget(MyXmallConst.CART_PRE + userId, String.valueOf(itemId));
            if (cart != null) {
                CartProduct cartProduct = BeanUtil.mapToBean((Map<?, ?>) cart, CartProduct.class, null);
                cartProduct.setProductNum(cartProduct.getProductNum() + 1);
                redisUtils.hset(MyXmallConst.CART_PRE + userId, String.valueOf(itemId), cartProduct);
            } else {
                return 0;
            }
            return 1;
        }

        //如果商品不存在,根据商品id获取商品信息
        TbItem tbItem = itemMapper.selectById(itemId);
        if (tbItem == null) {
            return 0;
        }

        CartProduct cartProduct=new CartProduct();

        cartProduct.setProductId(tbItem.getId());
        cartProduct.setProductName(tbItem.getTitle());
        cartProduct.setSalePrice(tbItem.getPrice());
        cartProduct.setProductImg(tbItem.getImages()[0]);
        if (tbItem.getLimitNum()==null){
            cartProduct.setLimitNum(Long.valueOf(tbItem.getNum()));
        }else if (tbItem.getLimitNum()<0&&tbItem.getNum()<0){
            cartProduct.setLimitNum(10L);
        }else {
            cartProduct.setLimitNum(Long.valueOf(tbItem.getLimitNum()));
        }

        cartProduct.setProductNum(Long.valueOf(num));
        cartProduct.setChecked("1");

        redisUtils.hset(MyXmallConst.CART_PRE+userId, String.valueOf(itemId),cartProduct);
        return 1;
    }

    @Override
    public List<CartProduct> getCartList(Long userId) {
        Map<Object, Object> hmget = redisUtils.hmget(MyXmallConst.CART_PRE + userId);

        if (hmget.size()>0){
            ArrayList arrayList = new ArrayList(hmget.values());
           List<CartProduct> list= (List<CartProduct>) arrayList.stream().map(item -> {
                CartProduct c = new CartProduct();
                BeanUtil.copyProperties(item, c);
                return c;
            }).collect(Collectors.toList());
           return  list;
        }
        return null;
    }

    @Override
    public int cartEdit(Long userId, Long productId, int productNum, String checked) {

        Object hget = redisUtils.hget(MyXmallConst.CART_PRE + userId, String.valueOf(productId));

        if (hget!=null){
            CartProduct cartProduct=new CartProduct();
            BeanUtil.copyProperties(hget,cartProduct);
            cartProduct.setChecked(checked);
            cartProduct.setProductNum((long)productNum);
            redisUtils.hset(MyXmallConst.CART_PRE+userId, String.valueOf(productId),cartProduct);
            return 1;
        }
        return 0;
    }

    @Override
    public int editCartCheckAll(String userId, String checked) {
        Map<Object, Object> hmget = redisUtils.hmget(MyXmallConst.CART_PRE + userId);

        for (Object o : hmget.keySet()) {
            Object o1 = hmget.get(o);
            CartProduct cartProduct=new CartProduct();
            BeanUtil.copyProperties(o1,cartProduct);
            cartProduct.setChecked(checked);
            redisUtils.hset(MyXmallConst.CART_PRE+userId, (String) o,cartProduct);
        }
        return 1;
    }
}

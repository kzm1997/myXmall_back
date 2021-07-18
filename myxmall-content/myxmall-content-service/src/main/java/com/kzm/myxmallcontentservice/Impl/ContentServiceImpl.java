package com.kzm.myxmallcontentservice.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.kzm.common.myxmallConst.MyXmallConst;
import cn.kzm.manage.dto.front.ProductDet;
import cn.kzm.manage.pojo.TbItem;
import cn.kzm.manage.pojo.TbItemDesc;
import cn.kzm.manage.pojo.TbPanel;
import cn.kzm.manage.pojo.TbPanelContent;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kzm.api.ContentService;
import com.kzm.myxmallcontentservice.mapper.TbItemDescMapper;
import com.kzm.myxmallcontentservice.mapper.TbItemMapper;
import com.kzm.myxmallcontentservice.mapper.TbPanelContentMapper;
import com.kzm.myxmallcontentservice.mapper.TbPanelMapper;
import com.kzm.myxmallcontentservice.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContentServiceImpl implements ContentService {


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private TbPanelMapper tbPanelMapper;

    @Autowired
    private TbPanelContentMapper tbPanelContentMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;


    @Override
    public List<TbPanel> getHome() {

        List<TbPanel> list = new ArrayList<>();

        //查询缓存
        Object productHome = redisUtils.get(MyXmallConst.PRODUCT_HOME);
        if (productHome != null) {
            //此时拿到的已经时反序列化的数据list;
            list = (List<TbPanel>) productHome;
            return list;
        }

        //没有缓存
        list = tbPanelMapper.selectList(new QueryWrapper<TbPanel>().select("distinct *")
                .eq("position", 0).eq("status", "1").orderByAsc("sort_order"));

        for (TbPanel tbPanel : list) {
            List<TbPanelContent> contentList = tbPanelContentMapper.selectList(new QueryWrapper<TbPanelContent>()
                    .lambda().eq(TbPanelContent::getPanelId, tbPanel.getId()).orderByAsc(TbPanelContent::getSortOrder));

            for (TbPanelContent tbPanelContent : contentList) {
                if (tbPanelContent.getProductId() != null) {
                    TbItem tbItem = tbItemMapper.selectById(tbPanelContent.getProductId());
                    tbPanelContent.setProductName(tbItem.getTitle());
                    tbPanelContent.setSalePrice(tbItem.getPrice());
                    tbPanelContent.setSubTitle(tbItem.getSellPoint());
                }
            }
            tbPanel.setPanelContents(contentList);
        }

        //放入缓存
        redisUtils.set(MyXmallConst.PRODUCT_HOME, list);

        return list;
    }

    @Override
    public ProductDet getProductDet(Long productId) {



        //查询缓存
        Object o = redisUtils.get(MyXmallConst.PRODUCT_ITEM + ":" + productId);
        if (o!=null){
            //重置过期时间
            redisUtils.expire(MyXmallConst.PRODUCT_ITEM+":"+productId,1000);
            ProductDet productDet = BeanUtil.mapToBean((Map<String, Object>) o, ProductDet.class, null);

            return  productDet;
        }
        TbItem tbItem = tbItemMapper.selectById(productId);
        ProductDet productDet = new ProductDet();
        productDet.setProductId(productId);
        productDet.setProductName(tbItem.getTitle());
        productDet.setSubTitle(tbItem.getSellPoint());
        if (tbItem.getLimitNum() != null && !tbItem.getLimitNum().toString().isEmpty()) {
            productDet.setLimitNum(Long.valueOf(tbItem.getLimitNum()));
        } else {
            productDet.setLimitNum(Long.valueOf(tbItem.getNum()));
        }
        productDet.setSalePrice(tbItem.getPrice());

        TbItemDesc tbItemDesc = tbItemDescMapper.selectOne(new QueryWrapper<TbItemDesc>()
                .lambda().eq(TbItemDesc::getItemId,productId));
        productDet.setDetail(tbItemDesc.getItemDesc());

        if (tbItem.getImage() != null && !tbItem.getImage().isEmpty()) {
            String[] images = tbItem.getImage().split(",");
            productDet.setProductImageBig(images[0]);
            List<String> list = Arrays.stream(images).collect(Collectors.toList());
            productDet.setProductImageSmall(list);
        }
        //添加缓存
        redisUtils.set(MyXmallConst.PRODUCT_ITEM+":"+productId,productDet,1000);
        return productDet;
    }


    @Override
    public Boolean delCart(String userId, String itemId) {
        Long hedl = redisUtils.hdel(MyXmallConst.CART_PRE + userId, itemId);
         return hedl==null?false:true;
    }

}

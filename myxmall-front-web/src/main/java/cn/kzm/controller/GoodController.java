package cn.kzm.controller;

import cn.kzm.common.dto.Result;
import cn.kzm.common.utils.ResultUtil;
import cn.kzm.manage.dto.front.ProductDet;
import cn.kzm.manage.pojo.TbPanel;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kzm.api.ContentService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoodController {

    @Reference
    private ContentService contentService;


    @RequestMapping(value = "/goods/home", method = RequestMethod.GET)
    @ApiModelProperty("首页内容展示")
    public Result<List<TbPanel>> getProductHome() {
        List<TbPanel> home = contentService.getHome();
        return new ResultUtil<List<TbPanel>>().setData(home);
    }

    @RequestMapping(value = "/goods/productDet", method = RequestMethod.GET)
    @ApiOperation(value = "商品详情")
    public Result<ProductDet> getProductDet(@RequestParam("productId") Long productId) {
        ProductDet productDet = contentService.getProductDet(productId);
        return new ResultUtil<ProductDet>().setData(productDet);
    }
}


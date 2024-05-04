package com.zcx.front.controller;

import com.zcx.api.model.ProductInfo;
import com.zcx.api.pojo.BidInfoProduct;
import com.zcx.api.pojo.MultiProduct;
import com.zcx.common.enums.RCodeEnum;
import com.zcx.common.util.CommonUtil;
import com.zcx.front.view.PageInfo;
import com.zcx.front.view.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "理财产品功能")
@RestController
@RequestMapping("/v1")
public class ProductController extends BaseController {


    @ApiOperation(value = "首页三类产品列表", notes = "一个新手宝，三个优选，三个散标产品")
    @GetMapping("/product/index")
    public RespResult queryProductIndex() {
        RespResult result = RespResult.ok();
        MultiProduct multiProduct = productService.queryIndexPageProducts();
        result.setData(multiProduct);
        return result;
    }

    @GetMapping("/product/list")
    @ApiOperation(value = "产品分页查询", notes = "按产品类型分页查询")
    public RespResult queryProductByType(@RequestParam("ptype") Integer pType, @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        RespResult result = RespResult.fail();
        if (pType != null && (pType == 0 || pType == 1 || pType == 2)) {
            pageNo = CommonUtil.defaultPageNo(pageNo);
            pageSize = CommonUtil.defaultPageSize(pageSize);
            Integer recordNums = productService.queryRecordNumsByType(pType);
            if (recordNums > 0) {
                List<ProductInfo> productInfos = productService.queryByTypeLimit(pType, pageNo, pageSize);
                result = RespResult.ok();
                result.setList(productInfos);
                PageInfo pageInfo = new PageInfo(pageNo, pageSize, recordNums);
                result.setPage(pageInfo);
            }
        } else {
            result.setRCode(RCodeEnum.REQUEST_PRODUCT_TYPE_ERR);
        }
        return result;
    }

    @ApiOperation(value = "产品的详情", notes = "查询某个产品的详细信息和投资5条记录")
    @GetMapping("/product/info")
    public RespResult queryProductDetail(@RequestParam("productId") Integer id) {
        RespResult result = RespResult.fail();
        if (id != null && id > 0) {
            ProductInfo productInfo = productService.queryById(id);
            if (productInfo != null) {
                List<BidInfoProduct> bidInfoProducts = investService.queryBidListByProductId(id, 1, 5);
                result = RespResult.ok();
                result.setData(productInfo);
                result.setList(bidInfoProducts);
            } else {
                result.setRCode(RCodeEnum.PRODUCT_OFFLINE);
            }
        }
        return result;
    }
}

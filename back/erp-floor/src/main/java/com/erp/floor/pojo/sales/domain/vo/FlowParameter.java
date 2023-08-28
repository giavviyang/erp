package com.erp.floor.pojo.sales.domain.vo;

import com.erp.floor.pojo.sales.domain.OrderProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**分架参数vo类
 * @version 1.0
 * @author： WangQiMin
 * @date： 2022-08-01 15:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "分架传参", description = "分架传参实体类")
public class FlowParameter {

    /*产品信息集合*/
    @ApiModelProperty("分架产品集合")
    private List<OrderProduct> orderProducts;

    @ApiModelProperty("分架参数")
    private Map<String, Object> parameter;

//    /*每架最大数量*/
//    public double number;
//
//    /*每架最大面积*/
//    public double area;
//
//    /*每架最大重量*/
//    public double weight;
//
//    /*每架最大叠加厚度*/
//    public double fold;
//
//    /*每架最大宽差距*/
//    public double wideBig;
//
//    /*每架最小宽差距*/
//    public double wideSmall;
//
//    /*每架最大高差距*/
//    public int highBig;
//
//    /*每架最小高差距*/
//    public int highSmall;
//
//    /*排序规则*/
//    public int sort;
//
//    /*位置是否相同*/
//    public boolean isPosition;

}

package me.leiho.elasticsearch.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOnePageHotelReq extends BasePageReq {
    @ApiModelProperty("城市编号")
    String city;
    @ApiModelProperty("酒店星级")
    String star;
    @ApiModelProperty("搜索关键字")
    String keyWord;
}

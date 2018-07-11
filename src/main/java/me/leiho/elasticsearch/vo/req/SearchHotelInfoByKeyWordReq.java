package me.leiho.elasticsearch.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchHotelInfoByKeyWordReq {
    @ApiModelProperty("搜索关键字")
    String keyWord;
    @ApiModelProperty("城市编号")
    String cityId;
    @ApiModelProperty("页码")
    Integer page;
    @ApiModelProperty("每页大小")
    Integer size;
}

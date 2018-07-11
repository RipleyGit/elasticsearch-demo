package me.leiho.elasticsearch.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchHotelInfoByCoordinatesAndRangeReq {
    @ApiModelProperty("经度")
    private Double lat;
    @ApiModelProperty("纬度")
    private Double lon;
    @ApiModelProperty("范围,单位为km")
    private String range;
    @ApiModelProperty("页码")
    Integer page;
    @ApiModelProperty("每页大小")
    Integer size;
}

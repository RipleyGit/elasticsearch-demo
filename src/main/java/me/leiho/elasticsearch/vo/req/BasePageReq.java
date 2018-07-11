package me.leiho.elasticsearch.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasePageReq {
    @ApiModelProperty("页码")
    int index;
    @ApiModelProperty("每页大小")
    int size;
    @ApiModelProperty("排序方式")
    String orderBy;
}

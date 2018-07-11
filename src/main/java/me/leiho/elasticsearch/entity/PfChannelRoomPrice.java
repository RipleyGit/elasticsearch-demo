package me.leiho.elasticsearch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 腾住的房型房价表
 * </p>
 *
 * @author 萧大俠
 * @since 2018-07-07
 */
@Data
public class PfChannelRoomPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String cityId;
    private String hotelId;
    /**
     * 渠道商酒店id
     */
    private String channelHotelId;
    /**
     * 关联渠道商ID
     */
    private String channelId;
    /**
     * 关联渠道商房型列表主键
     */
    private String channelRoomTypeId;
    /**
     * 渠道商子房型id
     */
    private String channelSubRoomTypeId;
    /**
     * 子房型名称
     */
    private String nameCn;
    private String nameEn;
    /**
     * 房型价格描述（或者子房型描述）
     */
    private Integer price;
    private Date date;
    /**
     * (0:无早；1：单早；2；双早；3：三早)
     */
    private Integer breakfast;
    /**
     * 基础房价(单位分)
     */
    private Integer status;
    /**
     * 床型
     */
    private Integer bed;
    private Integer wifi;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 删除标识（0：未删除；1：已删除）
     */
    private Integer markForDelete;
    private BigDecimal roomSize;
    private String promotion;
}

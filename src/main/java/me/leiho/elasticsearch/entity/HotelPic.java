package me.leiho.elasticsearch.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 萧大俠
 * @since 2018-07-03
 */
public class HotelPic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;
    /**
     * 城市id
     */
    private String cityId;
    private String channelId;
    /**
     * 酒店id
     */
    private String hotelId;
    /**
     * 图片
     */
    private String picUrl;
    private String type;
    private String roomType;
    /**
     * 0'未同步'1'已同步'
     */
    private Integer sync;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标识（0未删除1删除）
     */
    private Integer del;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    @Override
    public String toString() {
        return "HotelPic{" +
        "id=" + id +
        ", cityId=" + cityId +
        ", channelId=" + channelId +
        ", hotelId=" + hotelId +
        ", picUrl=" + picUrl +
        ", type=" + type +
        ", roomType=" + roomType +
        ", sync=" + sync +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", del=" + del +
        "}";
    }
}

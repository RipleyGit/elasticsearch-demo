package me.leiho.elasticsearch.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 萧大俠
 * @since 2018-06-28
 */
@TableName("pf_hotel_facility")
public class PfHotelFacility implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String hotelId;
    private String channelId;
    private String cityId;
    /**
     * 中文名
     */
    private String nameCn;
    /**
     * 英文名
     */
    private String nameEn;
    /**
     * 设施简介
     */
    private String intro;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 删除标识
     */
    private Boolean markForDelete;


    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getMarkForDelete() {
        return markForDelete;
    }

    public void setMarkForDelete(Boolean markForDelete) {
        this.markForDelete = markForDelete;
    }

    @Override
    public String toString() {
        return "PfHotelFacility{" +
        "hotelId=" + hotelId +
        ", channelId=" + channelId +
        ", cityId=" + cityId +
        ", nameCn=" + nameCn +
        ", nameEn=" + nameEn +
        ", intro=" + intro +
        ", createDate=" + createDate +
        ", updateDate=" + updateDate +
        ", markForDelete=" + markForDelete +
        "}";
    }
}

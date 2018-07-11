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
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市编号
     */
    private String cityId;
    /**
     * 城市中文名
     */
    private String cityCn;
    /**
     * 城市英文名
     */
    private String cityEn;
    /**
     * 州、省份编号
     */
    private String provId;
    /**
     * 州、省份中文名
     */
    private String provCn;
    /**
     * 州、省份英文名
     */
    private String provEn;
    /**
     * 国家编号
     */
    private String countryId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 删除标识
     */
    private Integer del;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;


    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityCn() {
        return cityCn;
    }

    public void setCityCn(String cityCn) {
        this.cityCn = cityCn;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    public String getProvCn() {
        return provCn;
    }

    public void setProvCn(String provCn) {
        this.provCn = provCn;
    }

    public String getProvEn() {
        return provEn;
    }

    public void setProvEn(String provEn) {
        this.provEn = provEn;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "City{" +
        "cityId=" + cityId +
        ", cityCn=" + cityCn +
        ", cityEn=" + cityEn +
        ", provId=" + provId +
        ", provCn=" + provCn +
        ", provEn=" + provEn +
        ", countryId=" + countryId +
        ", status=" + status +
        ", del=" + del +
        ", createdTime=" + createdTime +
        ", updatedTime=" + updatedTime +
        "}";
    }
}

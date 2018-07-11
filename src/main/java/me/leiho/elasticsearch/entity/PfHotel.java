package me.leiho.elasticsearch.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 腾住酒店表
 * </p>
 *
 * @author 萧大俠
 * @since 2018-06-28
 */
@Data
@TableName("pf_hotel")
public class PfHotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    private String hotelName;
    /**
     * 地址
     */
    private String addr;
    /**
     * 酒店名字拼音
     */
    private String namepy;
    private String headimage;
    /**
     * 国家
     */
    private String nation;
    private String prov;
    private String city;
    private String area;
    /**
     * 总机
     */
    private String switchboard;
    /**
     * 酒店类型
     */
    private String style;
    /**
     * 卖点
     */
    private String salespoint;
    /**
     * 开店时间
     */
    private String opentime;
    /**
     * 0 快捷连锁 1 二星以下/经济 2三星/舒适 3四星/高档 4五星/豪华
     */
    private String star;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 入住离店须知
     */
    private String checkinfo;
    /**
     * 儿童政策
     */
    private String child;
    /**
     * 膳食安排
     */
    private String diet;
    /**
     * 宠物
     */
    private String pet;
    /**
     * 权重 从大到小排列
     */
    private Integer grade;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 删除标识（0：未删除；1：已删除）
     */
    private Boolean markForDelete;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 酒店简介
     */
    private String intro;
    /**
     * 商圈
     */
    private String busiZone;
}

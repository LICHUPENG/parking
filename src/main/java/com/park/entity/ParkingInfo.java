package com.park.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ParkingInfo {

    /** 停车场Id */
    private Integer parkId;

    /** 停车场UUID */
    private String parkUUId;

    /** 停车场名称 */
    private String name;

    /** 停车场类型 */
    private String parkType;

    /** 省份名 */
    private String provinceName;

    /** 城市名 */
    private String cityName;

    /** 区域名 */
    private String areaName;

    /** 地址 */
    private String address;

    /** 经度 */
    private String longitude;

    /** 纬度 */
    private String latitude;

    /** 总车位数 */
    private Integer total;

    /** 剩余车位数 */
    private String leftNum;

    /** 价格 */
    BigDecimal price;

    /** 价格单位 */
    private String priceUnit;

    /** 收费描述 */
    private String priceDesc;

    /** 停车场图片列表 */
    private List<String> picUrlList;

    /** 是否可预订，1—可预订； 2—不可预订 */
    private String isDestine;

    /** 预订价格 */
    private BigDecimal reservePrice;

    /** 停车场距离请求坐标点的距离，单位：米 */
    private String distance;
}

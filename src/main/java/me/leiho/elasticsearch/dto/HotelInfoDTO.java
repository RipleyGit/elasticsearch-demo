package me.leiho.elasticsearch.dto;

import lombok.Data;

@Data
public class HotelInfoDTO {
    private String id;
    private String hotelName;
    private String addr;
    private String nation;
    private String prov;
    private String city;
    private String area;
    private String switchboard;
    private String star;
    private Integer grade;
    private String intro;
    private String busiZone;
    private String cityName;
    private String facilities;
    private Integer priceLevel;
    private LocationDTO location;
}

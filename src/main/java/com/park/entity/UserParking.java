package com.park.entity;

import lombok.Data;

@Data
public class UserParking {

    private Integer parkId;

    private String user_id;

    private Integer status;

    private Integer in_time;

    private Integer out_time;

    private Float stay_time;

        private String region;

    private Integer port;

    private Integer created_time;
}

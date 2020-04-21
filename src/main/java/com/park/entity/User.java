package com.park.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class User {

    @Id
    private String user_id;

    private String phone;

    private String openid;

    private String city;

    private Integer created_time;

    private Integer update_time;

    private String nickname;

    private String avatar_url;
}

package com.park.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Feedback {

    @Id
    private String feedback_id;

    private String contest;

    private String nickname;

    private String avatar_url;

    private Integer created_time;
}

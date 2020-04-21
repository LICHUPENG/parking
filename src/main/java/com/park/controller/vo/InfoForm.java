package com.park.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InfoForm {

    @NotBlank(message = "经度必填")
    private String longitude;

    @NotBlank(message = "纬度必填")
    private String latitude;

    private String distance;
}

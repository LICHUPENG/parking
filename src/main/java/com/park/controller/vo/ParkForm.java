package com.park.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ParkForm {

    @NotNull(message = "请填写停车场ID")
    private Integer parkId;

    @NotBlank(message = "请填写昵称")
    private String nickname;
}

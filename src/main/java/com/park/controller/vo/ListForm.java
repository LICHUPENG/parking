package com.park.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ListForm {

    @NotNull(message = "status状态必填")
    private Integer status;

    @NotBlank(message = "nickname必填")
    private String nickname;
}

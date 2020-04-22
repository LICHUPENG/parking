package com.park.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FeedbackForm {

    @NotBlank(message = "请填写内容")
    private String contest;

    @NotBlank(message = "用户名必填")
    private String nickname;
}
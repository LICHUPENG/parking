package com.park.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserInfoForm {

    @NotBlank(message = "请填写用户昵称！")
    private String nickname;

    private String city;

    @NotBlank(message = "请添加头像")
    private String avatarUrl;

}

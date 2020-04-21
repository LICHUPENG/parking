package com.park.controller;


import com.park.common.AppException;
import com.park.common.FastuuidUtil;
import com.park.common.Func;
import com.park.controller.vo.UserInfoForm;
import com.park.entity.User;
import com.park.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("user")
public class UserController {

    private final UserMapper userMapper;

    @GetMapping("getUserInfo")
    public Map<String, Object> getUserInfo(@RequestParam("nickname") String nickname) {
        User userParams = new User();
        userParams.setNickname(nickname);
        List<User> user = this.userMapper.select(userParams);
        if (user.isEmpty()) {
            throw new AppException("未找到用户", AppException.FORM_INVALID);
        }
        return Func.successMap(user.get(0));
    }

    @GetMapping("setUserInfo")
    public Map<String, Object> setUserInfo(@Validated UserInfoForm form) {
        User userParams = new User();
        userParams.setNickname(form.getNickname());
        List<User> users = this.userMapper.select(userParams);
        if (users.isEmpty()) {
            userParams.setUser_id(FastuuidUtil.genUUID());
            userParams.setAvatar_url(form.getAvatarUrl());
            userParams.setCity(form.getCity());
            this.userMapper.insertSelective(userParams);
            return Func.successMap(userParams);
        }
        return Func.successMap(users.get(0));
    }
}

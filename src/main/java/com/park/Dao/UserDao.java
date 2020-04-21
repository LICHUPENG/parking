package com.park.Dao;

import com.park.common.AppException;
import com.park.entity.User;
import com.park.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserDao {

    private final UserMapper userMapper;

    public User getUserByNickname(String nickname) {
        User user = new User();
        user.setNickname(nickname);
        List<User> users = this.userMapper.select(user);
        if (users.isEmpty()) {
            throw new AppException("未找到用户，请重新插入用户信息！", AppException.FORM_INVALID);
        }
        return users.get(0);
    }
}

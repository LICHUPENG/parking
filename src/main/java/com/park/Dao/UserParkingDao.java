package com.park.Dao;

import com.park.common.Func;
import com.park.entity.ParkingInfo;
import com.park.entity.User;
import com.park.entity.UserParking;
import com.park.mapper.ParkingInfoMapper;
import com.park.mapper.UserParkingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserParkingDao {

    private final UserParkingMapper userParkingMapper;

    private final UserDao userDao;

    private final ParkingInfoMapper parkingInfoMapper;

    public UserParking getUserParking(String userId, Integer parkId) {
        UserParking userParkingParams = new UserParking();
        userParkingParams.setUser_id(userId);
        userParkingParams.setParkId(parkId);
        List<UserParking> list = this.userParkingMapper.select(userParkingParams)
                .stream()
                .filter(userParking -> userParking.getStatus() == 1 || userParking.getStatus() == 2)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getList(Integer status, String nickname) {
        User user = this.userDao.getUserByNickname(nickname);
        UserParking userParkingParams = new UserParking();
        userParkingParams.setUser_id(user.getUser_id());
        return this.userParkingMapper.select(userParkingParams)
                .stream()
                .filter(userParking -> {
                    if (status == 1) {
                        return userParking.getStatus() == 1 || userParking.getStatus() == 0;
                    } else {
                        return userParking.getStatus() == 2;
                    }
                })
                .map(userParking -> {
                    ParkingInfo parkingInfo = this.parkingInfoMapper.selectByPrimaryKey(userParking.getParkId());
                    Map<String, Object> map = Func.beanToMap(parkingInfo);
                    Map<String, Object> map2 = Func.beanToMap(userParking);
                    map.putAll(map2);
                    return map;
                })
                .collect(Collectors.toList());
    }
}

package com.park.mapper;

import com.park.entity.UserParking;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserParkingMapper extends Mapper<UserParking> {

    UserParking getUserParking(@Param("user_id") String userId, @Param("park_id") Integer parkId);

    List<Map<String, Object>> getList(@Param("status") Integer status, @Param("nickname") String nickename);
}

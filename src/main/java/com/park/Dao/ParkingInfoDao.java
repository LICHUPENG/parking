package com.park.Dao;

import com.park.entity.ParkingInfo;
import com.park.mapper.ParkingInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ParkingInfoDao {

    private final ParkingInfoMapper parkingInfoMapper;

    public void insert(ParkingInfo parkingInfo) {
        ParkingInfo newInfo = new ParkingInfo();
        newInfo.setParkId(parkingInfo.getParkId());
        newInfo.setParkUuid(parkingInfo.getParkUuid());
        newInfo.setAddress(parkingInfo.getAddress());
        newInfo.setAreaName(parkingInfo.getAreaName());
        newInfo.setCityName(parkingInfo.getCityName());
        newInfo.setDistance(parkingInfo.getDistance());
        newInfo.setIsDestine(parkingInfo.getIsDestine());
        newInfo.setLatitude(parkingInfo.getLatitude());
        newInfo.setLongitude(parkingInfo.getLongitude());
        newInfo.setLeftNum(parkingInfo.getLeftNum());
        newInfo.setName(parkingInfo.getName());
        newInfo.setParkType(parkingInfo.getParkType());
        newInfo.setPrice(parkingInfo.getPrice());
        newInfo.setPriceDesc(parkingInfo.getPriceDesc());
        newInfo.setPriceUnit(parkingInfo.getPriceUnit());
        newInfo.setProvinceName(parkingInfo.getProvinceName());
        newInfo.setReservePrice(parkingInfo.getReservePrice());
        newInfo.setTotal(parkingInfo.getTotal());
        this.parkingInfoMapper.insertSelective(newInfo);
    }

}

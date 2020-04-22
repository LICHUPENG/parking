package com.park.controller;

import com.park.common.AppException;
import com.park.common.Func;
import com.park.controller.vo.InfoForm;
import com.park.controller.vo.PageNum;
import com.park.entity.ParkingInfo;
import com.park.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("net")
public class ParkingController {

    private final ParkingService parkingService;

    /**
     * 获取输入城市的停车场列表
     * @param cityName
     * @param pageNum
     * @return
     */
    @GetMapping("list")
    public Map<String, Object> getParkList(String cityName, @Validated PageNum pageNum) {
        if (StringUtils.isBlank(cityName)) {
            throw new AppException("城市名称必填!", AppException.FORM_INVALID);
        }
        List<ParkingInfo> list = this.parkingService.getParkList(cityName, pageNum);
        if (list == null) {
            throw new AppException("请求失败，请重新尝试！", AppException.FORM_INVALID);
        }
        return Func.successMap(list);
    }

    /**
     * 获取附近停车场列表
     * @param form
     * @param pageNum
     * @return
     */
    @GetMapping("near_list")
    public Map<String, Object> getNearList(@Validated InfoForm form, @Validated PageNum pageNum) {
        List<ParkingInfo> list = this.parkingService.getNearParkingList(form, pageNum);
        if (list == null) {
            throw new AppException("请求失败，请重新尝试！", AppException.FORM_INVALID);
        }
        return Func.successMap(list);
    }

    @GetMapping("parking_info")
    public Map<String, Object> getInfo(String parkId) {
        if (StringUtils.isBlank(parkId)) {
            throw new AppException("停车场Id必填!", AppException.FORM_INVALID);
        }
        ParkingInfo info = this.parkingService.getInfo(parkId);
        if (info == null || info.getParkId() == null) {
            throw new AppException("请求失败，请重新尝试！", AppException.FORM_INVALID);
        }
        return Func.successMap(info);
    }

}

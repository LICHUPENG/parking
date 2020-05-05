package com.park.controller;

import com.park.controller.vo.ListForm;
import com.park.common.Func;
import com.park.controller.vo.ParkForm;
import com.park.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("park")
public class ParkController {

    private final ParkingService parkingService;

    @GetMapping("book")
    public Map<String, Object> book(@Validated ParkForm form) {
        this.parkingService.book(form);
        Map<String, Object> map = new HashMap<>(2);
        int i = new Random().nextInt(100);
        map.put("num", i);
        return Func.successMap(map);
    }

    @GetMapping("cancel_record")
    public Map<String, Object> cancel(@Validated ParkForm form) {
        this.parkingService.cancel(form);
        return Func.successMap();
    }

    @GetMapping("in")
    public Map<String, Object> in(@Validated ParkForm form) {
        this.parkingService.in(form);
        return Func.successMap();
    }

    @GetMapping("out")
    public Map<String, Object> out(@Validated ParkForm form) {
        this.parkingService.out(form);
        return Func.successMap();
    }

    @GetMapping("list")
    public Map<String, Object> list(@Validated ListForm form) {
        List<Map<String, Object>> list = this.parkingService.getList(form);
        return Func.successMap(list);
    }

    @GetMapping("delete")
    public Map<String, Object> delete(@Validated ParkForm form) {
        this.parkingService.delete(form);
        return Func.successMap();
    }
}

package com.park.controller;

import com.park.common.AppException;
import com.park.common.Func;
import com.park.controller.vo.FeedbackForm;
import com.park.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("create")
    public Map<String, Object> create(@Validated FeedbackForm form) {
        this.feedbackService.create(form);
        return Func.successMap();
    }

    @GetMapping("list")
    public Map<String, Object> list(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new AppException("昵称必填", AppException.FORM_INVALID);
        }
        return Func.successMap(this.feedbackService.getList(nickname));
    }
}

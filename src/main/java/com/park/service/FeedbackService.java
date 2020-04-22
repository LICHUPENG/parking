package com.park.service;

import com.park.Dao.UserDao;
import com.park.common.FastuuidUtil;
import com.park.common.Func;
import com.park.controller.vo.FeedbackForm;
import com.park.entity.Feedback;
import com.park.entity.User;
import com.park.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;

    private final UserDao userDao;

    @Transactional(rollbackFor = Exception.class)
    public void create(FeedbackForm form) {
        User user = this.userDao.getUserByNickname(form.getNickname());
        Feedback feedback = new Feedback();
        feedback.setAvatar_url(user.getAvatar_url());
        feedback.setNickname(user.getNickname());
        feedback.setContest(form.getContest());
        feedback.setFeedback_id(FastuuidUtil.genUUID());
        feedback.setCreated_time(Func.getTime());
        this.feedbackMapper.insertSelective(feedback);
    }

    public List<Map<String, Object>> getList(String nickname) {
        Feedback feedbackParams = new Feedback();
        feedbackParams.setNickname(nickname);
        return this.feedbackMapper.select(feedbackParams)
                .stream()
                .map( feedback -> {
                    Map<String, Object> map = Func.beanToMap(feedback);
                    map.put("created_time", Func.timeFormatted(feedback.getCreated_time()));
                    return map;
                })
                .collect(Collectors.toList());
    }
}

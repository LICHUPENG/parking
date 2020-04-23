package com.park.service;

import com.alibaba.fastjson.JSONObject;
import com.park.Dao.ParkingInfoDao;
import com.park.Dao.UserDao;
import com.park.Dao.UserParkingDao;
import com.park.common.AppException;
import com.park.common.Func;
import com.park.controller.vo.InfoForm;
import com.park.controller.vo.ListForm;
import com.park.controller.vo.PageNum;
import com.park.controller.vo.ParkForm;
import com.park.entity.ParkingInfo;
import com.park.entity.User;
import com.park.entity.UserParking;
import com.park.mapper.ParkingInfoMapper;
import com.park.mapper.UserParkingMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dell
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ParkingService {

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String PUT = "PUT";

    public static final String DELETE = "DELETE";

    public static final String HEAD = "HEAD";

    public static final String OPTIONS = "OPTIONS";

    @Value("${app.appSecret}")
    private String appSecret;

    @Value("${app.parkInfoListUrl}")
    private String parkInfoListUrl;

    @Value("${app.nearbyParkInfoUrl}")
    private String nearbyParkInfoUrl;

    @Value("${app.queryParkInfoUrl}")
    private String queryParkInfoUrl;

    private final ParkingInfoDao parkingInfoDao;

    private final ParkingInfoMapper parkingInfoMapper;

    private final UserDao userDao;

    private final UserParkingMapper userParkingMapper;

    private final UserParkingDao userParkingDao;

    /**
     * 请求接口
     *
     * @param method  请求方法
     * @param url     请求地址
     * @param headers 请求头部
     * @param params  请求参数
     * @return
     */
    public static String proxyToDesURL(String method, String url, Map<String, String> headers,
                                       Map<String, String> params) {
        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            //处理请求头部
            HttpHeaders requestHeaders = new HttpHeaders();
            if (headers != null && !headers.isEmpty()) {
                Set<String> set = headers.keySet();
                for (String key : set) {
                    String value = headers.get(key);
                    requestHeaders.add(key, value);
                }
            }
            //处理请求参数
            MultiValueMap<String, String> paramList = new LinkedMultiValueMap<String, String>();
            if (params != null && !params.isEmpty()) {
                if (method.equalsIgnoreCase(GET)) {
                    url += "?";
                    Set<String> set = params.keySet();
                    StringBuilder urlBuilder = new StringBuilder(url);
                    for (String key : set) {
                        String value = params.get(key);
                        urlBuilder.append(key).append("=").append(value).append("&");
                    }
                    url = urlBuilder.toString();
                    url = url.substring(0, url.length() - 1);
                } else {
                    Set<String> set = params.keySet();
                    for (String key : set) {
                        String value = params.get(key);
                        paramList.add(key, value);
                    }
                }
            }
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
                    paramList, requestHeaders);
            //处理请求方法
            HttpMethod requestType;
            method = method.toUpperCase();
            switch (method) {
                case GET:
                    requestType = HttpMethod.GET;
                    break;
                case POST:
                    requestType = HttpMethod.POST;
                    break;
                case PUT:
                    requestType = HttpMethod.PUT;
                    break;
                case DELETE:
                    requestType = HttpMethod.DELETE;
                    break;
                case HEAD:
                    requestType = HttpMethod.HEAD;
                    break;
                case OPTIONS:
                    requestType = HttpMethod.OPTIONS;
                    break;
                default:
                    requestType = HttpMethod.GET;
                    break;
            }
            assert params != null;
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, requestType, requestEntity,
                    String.class, params);
            //获取返回结果
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取输入城市的停车场列表
     *
     * @param cityName
     * @param pageNum
     * @return
     */
    public List<ParkingInfo> getParkList(String cityName, PageNum pageNum) {
        //请求头部设置
        Map<String, String> headers = new HashMap<>();
        //请求参数设置
        Map<String, String> params = new HashMap<>(8);
        params.put("appkey", appSecret);
        // 城市名,比如：深圳市
        params.put("cityName", cityName);
        // 分页大小
        params.put("pageSize", StringUtils.isNotBlank(pageNum.getPageSize()) ? pageNum.getPageSize() : "50");
        // 当前页
        params.put("currentPage", StringUtils.isNotBlank(pageNum.getCurrentPage()) ? pageNum.getCurrentPage() : "1");
        String result = proxyToDesURL(POST, parkInfoListUrl, headers, params);
        return this.result(result, "parkInfoList");
    }

    /**
     * 获取附近停车场列表
     *
     * @param form
     * @param pageNum
     */
    public List<ParkingInfo> getNearParkingList(InfoForm form, PageNum pageNum) {
        //请求头部设置
        Map<String, String> headers = new HashMap<>();
        //请求参数设置
        Map<String, String> params = new HashMap<>(8);
        params.put("appkey", appSecret);
        // 经度
        params.put("longitude", form.getLongitude());
        // 经度
        params.put("latitude", form.getLatitude());
        //距离，单位米，默认3公里
        params.put("distance", StringUtils.isNotBlank(form.getDistance()) ? form.getDistance() : "3000");
        // 分页大小
        params.put("pageSize", StringUtils.isNotBlank(pageNum.getPageSize()) ? pageNum.getPageSize() : "50");
        // 当前页
        params.put("currentPage", StringUtils.isNotBlank(pageNum.getCurrentPage()) ? pageNum.getCurrentPage() : "1");
        String result = proxyToDesURL(POST, nearbyParkInfoUrl, headers, params);
        return this.result(result, "parkDistanceList");
    }

    public ParkingInfo getInfo(String parkId) {
        ParkingInfo info = this.parkingInfoMapper.selectByPrimaryKey(parkId);
        if (info != null) {
            return info;
        }
        //请求头部设置
        Map<String, String> headers = new HashMap<>();
        //请求参数设置
        Map<String, String> params = new HashMap<>(8);
        params.put("appkey", appSecret);
        params.put("parkId", parkId);
        String result = proxyToDesURL(POST, queryParkInfoUrl, headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        ParkingInfo parkingInfo = jsonObject.getJSONObject("result").toJavaObject(ParkingInfo.class);
        this.parkingInfoDao.insert(parkingInfo);
        return parkingInfo;
    }

    private List<ParkingInfo> result(String result, String type) {
        if (result != null) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status_code = jsonObject.getString("status");
            //有个别接口计费状态码为其他;请注意甄别
            if ("200".equals(status_code)) {
                // 状态码为200, 说明请求成功
                return jsonObject.getJSONObject("result").getJSONArray(type).toJavaList(ParkingInfo.class);
            } else {
                // 状态码非200, 说明请求失败
                throw new AppException("请求失败：" + jsonObject.getString("msg"), AppException.FORM_INVALID);
            }
        } else {
            // 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
            throw new AppException("发送请求失败", AppException.FORM_INVALID);
        }
    }

    public void book(ParkForm form) {
        ParkingInfo parkingInfo = this.parkingInfoMapper.selectByPrimaryKey(form.getParkId());
        if (parkingInfo == null) {
            parkingInfo = this.getInfo(String.valueOf(form.getParkId()));
        }
        User user = this.userDao.getUserByNickname(form.getNickname());

        UserParking parking = this.userParkingDao.getUserParking(user.getUser_id(), form.getParkId());
        if (parking != null) {
            throw new AppException("请先处理未完成订单", AppException.FORM_INVALID);
        }

        UserParking userParking = new UserParking();
        userParking.setUser_id(user.getUser_id());
        userParking.setParkId(parkingInfo.getParkId());
        userParking.setStatus(2);
        userParking.setCreated_time(Func.getTime());
        this.userParkingMapper.insertSelective(userParking);
    }

    public void cancel(ParkForm form) {
        User user = this.userDao.getUserByNickname(form.getNickname());

        UserParking userParkingParams = new UserParking();
        userParkingParams.setUser_id(user.getUser_id());
        userParkingParams.setParkId(form.getParkId());
        userParkingParams.setStatus(2);

        UserParking record = this.userParkingMapper.selectOne(userParkingParams);
        if (record == null) {
            throw new AppException("未知道到记录！", AppException.FORM_INVALID);
        }
        if (record.getStatus() != 2) {
            throw new AppException("请先预约！", AppException.FORM_INVALID);
        }
        this.userParkingMapper.delete(record);
    }

    public void in(ParkForm form) {
        User user = this.userDao.getUserByNickname(form.getNickname());

        UserParking userParkingParams = new UserParking();
        userParkingParams.setUser_id(user.getUser_id());
        userParkingParams.setParkId(form.getParkId());
        userParkingParams.setStatus(2);
        UserParking parking = this.userParkingMapper.selectOne(userParkingParams);
        if (parking == null) {
            throw new AppException("请先预约，或者直接停车", AppException.FORM_INVALID);
        }

        int now = Func.getTime();
        parking.setStatus(1);
        parking.setIn_time(now);

        Example example = new Example(UserParking.class);
        example.createCriteria()
                .andEqualTo("user_id", user.getUser_id())
                .andEqualTo("parkId", form.getParkId())
                .andEqualTo("status", 2);
        this.userParkingMapper.updateByExampleSelective(parking, example);
    }

    public void out(ParkForm form) {
        User user = this.userDao.getUserByNickname(form.getNickname());

        UserParking userParkingParams = new UserParking();
        userParkingParams.setUser_id(user.getUser_id());
        userParkingParams.setParkId(form.getParkId());
        userParkingParams.setStatus(1);
        UserParking parking = this.userParkingMapper.selectOne(userParkingParams);
        if (parking == null) {
            throw new AppException("请先停车", AppException.FORM_INVALID);
        }

        int now = Func.getTime();
        parking.setStatus(0);
        parking.setOut_time(now);
        parking.setStay_time(new BigDecimal((now - parking.getIn_time()) / 60.0)
                .setScale(1,BigDecimal.ROUND_HALF_UP).floatValue());

        Example example = new Example(UserParking.class);
        example.createCriteria()
                .andEqualTo("user_id", user.getUser_id())
                .andEqualTo("parkId", form.getParkId())
                .andEqualTo("status", 1);
        this.userParkingMapper.updateByExampleSelective(parking, example);
    }

    public List<Map<String, Object>> getList(ListForm form) {
        return this.userParkingDao.getList(form.getStatus(), form.getNickname())
                .stream()
                .peek(map -> {
                    switch ((Integer) map.get("status")) {
                        case 0:
                            map.put("status", "已完成");
                            map.put("in_time", Func.timeFormatted((Integer) map.get("in_time")));
                            map.put("out_time", Func.timeFormatted((Integer) map.get("out_time")));
                            map.put("stay_time", map.get("stay_time") + "分钟");
                            break;
                        case 1:
                            Integer inTime = (Integer) map.get("in_time");
                            map.put("status", "停车中");
                            map.put("in_time", Func.timeFormatted(inTime));
                            map.put("stay_time", new BigDecimal((Func.getTime() - inTime) / 60.0)
                                    .setScale(1,BigDecimal.ROUND_HALF_UP).floatValue() + "分钟");
                            break;
                        case 2:
                            map.put("status", "已预约");
                            break;
                        default:
                            map.put("status", "未知状态");
                    }
                    map.put("created_time", Func.timeFormatted((Integer) map.get("created_time")));
                }).collect(Collectors.toList());
    }

}

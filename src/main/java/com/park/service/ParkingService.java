package com.park.service;

import com.alibaba.fastjson.JSONObject;
import com.park.common.AppException;
import com.park.controller.vo.InfoForm;
import com.park.controller.vo.PageNum;
import com.park.entity.ParkingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    /**
     * 请求接口
     * @param method 请求方法
     * @param url 请求地址
     * @param headers 请求头部
     * @param params 请求参数
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
                if(method.equalsIgnoreCase(GET))
                {
                    url += "?";
                    Set<String> set = params.keySet();
                    StringBuilder urlBuilder = new StringBuilder(url);
                    for (String key : set) {
                        String value = params.get(key);
                        urlBuilder.append(key).append("=").append(value).append("&");
                    }
                    url = urlBuilder.toString();
                    url = url.substring(0, url.length() - 1);
                }
                else
                {
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

    public ParkingInfo getInfo (String parkId, String parkUUId) {
        //请求头部设置
        Map<String, String> headers = new HashMap<>();
        //请求参数设置
        Map<String, String> params = new HashMap<>(8);
        params.put("appkey", appSecret);
        params.put("parkId", parkId);
        String result = proxyToDesURL(POST, queryParkInfoUrl, headers, params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getJSONObject("result").toJavaObject(ParkingInfo.class);
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

}

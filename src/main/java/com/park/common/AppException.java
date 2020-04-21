package com.park.common;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = -738439098559776438L;

    /** 正常响应 */
    public static final int NORMAL = 200;

    /** 重定向跳转 */
    public static final int REDIRECT = 300;

    /** 错误的请求 **/
    public static final int BAD_REQUEST = 400;

    /** 无效的认证 */
    public static final int AUTH_INVALID = 401;

    /** 权限不足 */
    public static final int AUTH_DENIED  = 402;

    /** 表单验证失败 */
    public static final int FORM_INVALID = 403;

    /** 地址未开放 */
    public static final int NOT_FOUND =  404;

    /** 未选择仓库 */
    public static final int NOT_CHOICE = 801;

    /** 系统异常 */
    public static final int SYSTEM = 500;

    /** 系统初始化 */
    public static final int SYSTEM_INIT = 501;

    public static final int AREA_SELECT=205;

    private int statusCode = SYSTEM;

    public int getStatusCode() {
        return statusCode;
    }

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
        this.statusCode = SYSTEM;
    }

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

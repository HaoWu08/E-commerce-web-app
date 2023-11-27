package com.mmall.result;

public enum ResponseEnum {
    USER_INFO_NULL(300, "username cannot be null"),
    EMAIL_ERROR(301,"email error"),
    MOBILE_ERROR(302, "Mobile error"),
    USERNAME_EXIST(303, "name exists"),

    REGISTER_ERROR(304, "register error"),

    USERNAME_DOEST_EXIST(305, "username doesn't exist"),
    PASSWORD_DOEST_EXIST(306, "username doesn't exist"),

    PARAMETER_ERROR(307, "parameter error"),

    LOGIN_ERROR(308, "log in error"),

    CART_ADD_ERROR(309, "cart add error"),

    PRODUCT_NOT_EXIST(310,"product not exist"),

    PRODUCT_STOCK_ERROR(311, "product stock error"),

    UPDATE_CART_ERROR(312, "update cart error"),

    UPDATE_CART_PARAMETER_ERROR(313, "update parameter error"),

    DELETE_CART_ERROR(314, "delete cart error"),

    CREATE_ORDER_ERROR(315, "create order error"),

    CREATE_ORDERDETAIL_ERROR(316, "order detail error"),

    DELETE_ERROR(317, "delete error"),

    USER_ADDRESS_ERROR(318, "address error"),

    SET_DEFAULT_ERROR(319, "set default error");
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package com.mmall.vo;

import com.mmall.entity.OrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class OrdersVo {
    private Integer id;

    private String loginName;

    private String userAddress;

    private Float cost;

    private String serialnumber;

    List<OrderDetailVo> orderDetailVoList;
}

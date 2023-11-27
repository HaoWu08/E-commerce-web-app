package com.mmall.vo;

import lombok.Data;

@Data
public class OrderDetailVo {
    private Integer id;

    private String name;

    private Float price;

    private String fileName;

    private Integer quantity;

    private Float cost;
}

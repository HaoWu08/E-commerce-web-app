package com.mmall.vo;

import lombok.Data;

@Data
public class CartVo {
    private Integer id;

    private Integer productId;

    private Integer quantity;

    private Float cost;

    private String fileName;

    private String name;

    private Float price;

    private Integer stock;
}

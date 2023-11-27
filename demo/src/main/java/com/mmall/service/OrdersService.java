package com.mmall.service;

import com.mmall.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.vo.OrdersVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
public interface OrdersService extends IService<Orders> {
    public List<OrdersVo> findAllByUserId(Integer id);
}

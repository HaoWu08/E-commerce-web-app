package com.mmall.service;

import com.mmall.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.entity.Orders;
import com.mmall.entity.User;
import com.mmall.vo.CartVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
public interface CartService extends IService<Cart> {
     public Boolean addCart(Cart cart);

     public List<CartVo> findVoListByUserId(Integer userId);

     public Boolean update(Integer id, Integer quantity, Float cost);

     public Boolean delete(Integer id);

     public Orders commit(String userAddress, String address, String remark, User user);
}

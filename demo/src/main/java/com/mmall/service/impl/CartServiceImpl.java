package com.mmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.Exception.mmallException;
import com.mmall.entity.*;
import com.mmall.mapper.*;
import com.mmall.result.ResponseEnum;
import com.mmall.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
@Service
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Autowired
    private UserAddressMapper userAddressMapper;
    @Override
    @Transactional
    public Boolean addCart(Cart cart){
        int insert = this.cartMapper.insert(cart);
        if(insert != 1){
            throw new mmallException(ResponseEnum.CART_ADD_ERROR);
        }
        // decrease the stock of product
        Integer stock = this.productMapper.getStockById(cart.getProductId());
        if(stock == null){
            log.info("[add cart] product not exist");
            throw new mmallException(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        if(stock == 0){
            log.info("[add cart] product stock error");
            throw new mmallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        Integer newStock = stock - cart.getQuantity();
        if(newStock < 0){
            log.info("[add cart] product stock error");
            throw new mmallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        this.productMapper.updateStockById(cart.getProductId(), newStock);
        return true;
    }


    @Override
    public List<CartVo> findVoListByUserId(Integer userId){
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Cart> cartList = this.cartMapper.selectList(queryWrapper);
        List<CartVo> cartVoList = new ArrayList<>();
        for(Cart cart : cartList){
            Product product = this.productMapper.selectById(cart.getProductId());
            CartVo cartVo = new CartVo();
            BeanUtils.copyProperties(product,cartVo);
            BeanUtils.copyProperties(cart, cartVo);
            cartVoList.add(cartVo);
        }
        return cartVoList;
    }

    @Override
    @Transactional
    public Boolean update(Integer id, Integer quantity, Float cost){
        //update stock
        Cart cart = this.cartMapper.selectById(id);
        Integer oldQuantity = cart.getQuantity();
        if(quantity.equals(oldQuantity)){
            log.info("[update stock] update stock error");
            throw new mmallException(ResponseEnum.UPDATE_CART_PARAMETER_ERROR);
        }
        Integer stock = this.productMapper.getStockById(cart.getProductId());
        Integer newStock = stock - (quantity - oldQuantity);
        if(newStock < 0){
            log.info("[update stock] update stock error");
            throw new mmallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        Integer i = this.productMapper.updateStockById(cart.getProductId(), stock);
        if(i != 1  ){
            log.info("[update stock] update stock error");
            throw new mmallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        //update data
        int update  = this.cartMapper.update(id, quantity, cost);
        if(update != 1){
            log.info("[update cart] update cart error");
            throw new mmallException(ResponseEnum.UPDATE_CART_ERROR);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id){
        //update
        Cart cart = this.cartMapper.selectById(id);
        Integer stock = this.productMapper.getStockById(cart.getProductId());
        Integer newStock = stock + cart.getQuantity();
        Integer integer = this.productMapper.updateStockById(cart.getProductId(), newStock);
        if(integer != 1){
            log.info("[delete cart] delete cart error");
            throw new mmallException(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        // delete
        int i = this.cartMapper.deleteById(id);
        if(i != 1){
            log.info("[delete cart] delete cart error");
            throw new mmallException(ResponseEnum.DELETE_CART_ERROR);
        }
        return true;
    }

    @Override
    public Orders commit(String userAddress,String address, String remark, User user){
        //deal with address
        if(!userAddress.equals("newAddress")){
            address = userAddress;
        }else{
            int i = this.userAddressMapper.setDefault();
            if(i == 0){
                log.info("[isDefault] error");
                throw new mmallException(ResponseEnum.SET_DEFAULT_ERROR);
            }
            //store address in database
            UserAddress userAddress1 = new UserAddress();
            userAddress1.setAddress(address);
            userAddress1.setRemark(remark);
            userAddress1.setUserId(user.getId());
            userAddress1.setIsdefault(1);
            int insert = this.userAddressMapper.insert(userAddress1);
            if(insert == 0){
                log.info("[userAddress] error");
                throw new mmallException(ResponseEnum.USER_ADDRESS_ERROR);
            }
        }
        // create order list
        Orders orders = new Orders();
        orders.setUserId(user.getId());
        orders.setLoginName(user.getLoginName());
        orders.setUserAddress(address);
        String seriaNumber = null;
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<32;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber =  result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.setSerialnumber(seriaNumber);
        orders.setCost(this.cartMapper.getCostByUserId(user.getId()));
        int insert = this.ordersMapper.insert(orders);
        if(insert != 1){
            log.info("[create order] create order error");
            throw new mmallException(ResponseEnum.CREATE_ORDER_ERROR);
        }
        // create order detail
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        List<Cart> carts = this.cartMapper.selectList(queryWrapper);
        for(Cart cart : carts){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            int insert1 = this.orderDetailMapper.insert(orderDetail);
            if(insert1 == 0){
                log.info("[order detail] error");
                throw new mmallException(ResponseEnum.CREATE_ORDERDETAIL_ERROR);
            }

        }
        //delete cart
        QueryWrapper<Cart> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("user_id",user.getId());
        int i = this.cartMapper.delete(queryWrapper1);
        if(i == 0){
            log.info("[delete cart] error");
            throw new mmallException(ResponseEnum.DELETE_ERROR);
        }
        return orders;
    }
}

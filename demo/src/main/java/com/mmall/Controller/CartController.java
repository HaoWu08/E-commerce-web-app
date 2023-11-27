package com.mmall.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.Exception.mmallException;
import com.mmall.entity.Cart;
import com.mmall.entity.Orders;
import com.mmall.entity.User;
import com.mmall.entity.UserAddress;
import com.mmall.result.ResponseEnum;
import com.mmall.service.CartService;
import com.mmall.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
@Controller
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserAddressService userAddressService;
    @GetMapping("add/{productId}/{price}/{quantity}")
    public String addCart(@PathVariable ("productId") Integer productId,
                                @PathVariable ("price") Float price,
                                @PathVariable ("quantity") Integer quantity,
                                HttpSession session
    ){
        if(productId == null || price == null || quantity == null){
            log.info("[add cart] parameter error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }

        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[add cart] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setCost(price * quantity);
        Boolean add = this.cartService.addCart(cart);
        if(!add){
            log.info("[add cart] add fails");
            throw new mmallException(ResponseEnum.CART_ADD_ERROR);
        }

        return "redirect:/cart/get";
    }

    @GetMapping("/get")
    public ModelAndView get(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[add cart] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement1");
        modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        return modelAndView;
    }

    @PostMapping("/update/{id}/{quantity}/{cost}")
    @ResponseBody
    public String update(@PathVariable("id") Integer id, @PathVariable("quantity") Integer quantity, @PathVariable("cost") Float cost, HttpSession session){
        if(id == null || quantity == null || cost == null){
            log.info("[update cart] parameter error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[update cart] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }

        Boolean update = this.cartService.update(id, quantity, cost);
        if(update == true){
            return "success";
        }
        return "fails";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, HttpSession session){
        if(id == null ){
            log.info("[delete cart] parameter error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }
        Boolean delete = this.cartService.delete(id);
        if(delete) return "redirect:/cart/get";
        return null;
    }

    @GetMapping("/confirm")
    public ModelAndView confirm(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[confirm cart] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement2");
        modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList", this.userAddressService.list(queryWrapper));
        return modelAndView;
    }

    @PostMapping("/commit")
    public ModelAndView commit(String userAddress, String address, String remark, HttpSession session){
        if(userAddress == null || address == null || remark == null ){
            log.info("[delete cart2] parameter error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[confirm cart] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("settlement3");
        Orders orders = this.cartService.commit(userAddress, address, remark, user);
        if(orders != null){
            modelAndView.addObject("orders",orders);
            modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
            return modelAndView;
        }
        return null;
    }
}


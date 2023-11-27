package com.mmall.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.Exception.mmallException;
import com.mmall.entity.User;
import com.mmall.entity.UserAddress;
import com.mmall.form.UserLoginForm;
import com.mmall.form.UserRegisterForm;
import com.mmall.result.ResponseEnum;

import com.mmall.service.CartService;
import com.mmall.service.OrdersService;
import com.mmall.service.UserAddressService;
import com.mmall.service.UserService;
import com.mmall.utils.RegexValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserAddressService userAddressService;

    /**
     * user register
     * @param userRegisterForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterForm userRegisterForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.info("user register error");
            throw new mmallException(ResponseEnum.USER_INFO_NULL);
        }

        User register = this.userService.register(userRegisterForm);
        if(register == null){
            log.info("user already exist");
            throw new mmallException(ResponseEnum.REGISTER_ERROR);
        }
        return "redirect:/login";
    }

    /**
     * user login
     */

    @PostMapping("/login")
    public String login(@Valid UserLoginForm userLoginForm, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()){
            log.info("[login page]user info cannot be empty");
            throw new mmallException(ResponseEnum.USER_INFO_NULL);
        }

        User login = this.userService.login(userLoginForm);
        session.setAttribute("user", login);
        return "redirect:/productCategory/main";
    }

    @GetMapping("/orderList")
    public ModelAndView ordersList(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[user center] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("orderList");
        modelAndView.addObject("orderList",this.ordersService.findAllByUserId(user.getId()));
        modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        return modelAndView;
    }

    @GetMapping("/addressList")
    public ModelAndView addressList(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            log.info("[user center] user haven't logged in" );
            throw new mmallException(ResponseEnum.LOGIN_ERROR);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userAddressList");
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        modelAndView.addObject("addressList",this.userAddressService.list(queryWrapper));
        modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        return modelAndView;
    }

}


package com.mmall.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.entity.Cart;
import com.mmall.entity.User;
import com.mmall.service.CartService;
import com.mmall.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
@Controller
@RequestMapping("/productCategory")
public class ProductCategoryController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @GetMapping("/main")
    public ModelAndView main(HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("main");

        //Encaps the product catogory menu
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        // find level one
        modelAndView.addObject("levelOne", this.productCategoryService.findAllProductByCategoryLevelOne());
        // check logined or not
        User user = (User)session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        }
        return modelAndView;
    }
}


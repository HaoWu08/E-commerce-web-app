package com.mmall.Controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.Exception.mmallException;
import com.mmall.entity.Cart;
import com.mmall.entity.Product;
import com.mmall.entity.User;
import com.mmall.result.ResponseEnum;
import com.mmall.service.CartService;
import com.mmall.service.ProductCategoryService;
import com.mmall.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(@PathVariable("type") Integer type, @PathVariable("id") Integer productCategoryId, HttpSession session){
        if(type == null || productCategoryId == null){
            log.info("[productCategory] productCategory error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");
        modelAndView.addObject("productList", this.productService.findAllByTypeAndProductCategoryId(type, productCategoryId));

        User user = (User)session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        }
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView search(String keyWord, HttpSession session){
        if(keyWord == null){
            log.info("[productCategory] parameter error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productList");

        // like search
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyWord);
        modelAndView.addObject("productList", this.productService.list(queryWrapper));
        User user = (User)session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));
        }
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        return modelAndView;
    }


    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable("id") Integer id, HttpSession session){
        if(id == null){
            log.info("[productDetail] parameter error");
            throw new mmallException(ResponseEnum.PARAMETER_ERROR);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("productDetail");
        User user = (User)session.getAttribute("user");
        if(user == null){
            modelAndView.addObject("cartList", new ArrayList<>());
        }else{
            modelAndView.addObject("cartList", this.cartService.findVoListByUserId(user.getId()));

        }
        modelAndView.addObject("list", this.productCategoryService.buildProductCategoryMenu());
        modelAndView.addObject("product", this.productService.getById(id));
        return modelAndView;
    }
}


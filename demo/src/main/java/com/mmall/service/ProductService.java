package com.mmall.service;

import com.mmall.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
public interface ProductService extends IService<Product> {
    public List<Product> findAllByTypeAndProductCategoryId(Integer type, Integer id);
}

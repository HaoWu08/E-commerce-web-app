package com.mmall.service;

import com.mmall.entity.Product;
import com.mmall.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.vo.ProductCategoryVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    public List<ProductCategoryVo> buildProductCategoryMenu();
    public List<ProductCategoryVo> findAllProductByCategoryLevelOne();
}

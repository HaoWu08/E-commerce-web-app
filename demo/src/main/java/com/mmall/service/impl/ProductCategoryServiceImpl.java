package com.mmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.entity.Product;
import com.mmall.entity.ProductCategory;
import com.mmall.mapper.ProductCategoryMapper;
import com.mmall.mapper.ProductMapper;
import com.mmall.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.vo.ProductCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductCategoryVo> buildProductCategoryMenu(){
        //search the whole menu
        List<ProductCategory> list = this.productCategoryMapper.selectList(null);
        List<ProductCategoryVo> productCategoryVoList = list.stream().map(ProductCategoryVo::new).collect(Collectors.toList());
        List<ProductCategoryVo> levelOneList = setChildren(productCategoryVoList);

        // search levelOne products
        return levelOneList;
    }

    public void getLevelOneProduct(List<ProductCategoryVo> list){
        for (ProductCategoryVo vo: list){
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("categorylevelone_id", vo.getId());
            List<Product> productList = this.productMapper.selectList(queryWrapper);
            vo.setProductList(productList);
        }
    }

    public List<ProductCategoryVo> setChildren(List<ProductCategoryVo> list){
        List<ProductCategoryVo> levelOneList= list.stream().filter(c -> c.getParentId() == 0).collect(Collectors.toList());
        for(ProductCategoryVo vo : levelOneList){
            recursion(list,vo);
        }
        return levelOneList;
    }

    public void recursion(List<ProductCategoryVo> list, ProductCategoryVo vo){
        List<ProductCategoryVo> children = getChildren(list, vo);
        vo.setChildren(children);
        if(children.size() > 0){
            for (ProductCategoryVo child : children) {
                recursion(list, child);
            }
        }
    }

    public List<ProductCategoryVo> getChildren(List<ProductCategoryVo> list, ProductCategoryVo vo){
        List<ProductCategoryVo> children = new ArrayList<>();
        Iterator<ProductCategoryVo> iterator = list.iterator();
        while(iterator.hasNext()){
            ProductCategoryVo next = iterator.next();
            if(next.getParentId().equals(vo.getId())){
                children.add(next);
            }
        }
        return children;
    }

    @Override
    public List<ProductCategoryVo> findAllProductByCategoryLevelOne(){
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 1);
        List<ProductCategory> productCategories = this.productCategoryMapper.selectList(queryWrapper);
        List<ProductCategoryVo> productCategoryVoList = productCategories.stream().map(ProductCategoryVo::new).collect(Collectors.toList());
        getLevelOneProduct(productCategoryVoList);
        return productCategoryVoList;
    }


}

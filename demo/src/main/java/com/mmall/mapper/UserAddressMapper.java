package com.mmall.mapper;

import com.mmall.entity.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    public int setDefault();
}

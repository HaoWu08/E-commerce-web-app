package com.mmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mmall.Exception.mmallException;
import com.mmall.entity.User;
import com.mmall.form.UserLoginForm;
import com.mmall.form.UserRegisterForm;
import com.mmall.mapper.UserMapper;
import com.mmall.result.ResponseEnum;
import com.mmall.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.utils.MD5utils;
import com.mmall.utils.RegexValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
        @Autowired
        private UserMapper userMapper;

    /**
     * register page
     * @param userRegisterForm
     * @return
     */
        @Override
        public User register(UserRegisterForm userRegisterForm){
            // username conflits
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_name", userRegisterForm.getLoginName());
            User one = this.userMapper.selectOne(queryWrapper);
            if(one != null){
                log.info("user has already registered");
                throw new mmallException(ResponseEnum.USERNAME_EXIST);
            }

            //email check
            if(!RegexValidateUtil.checkEmail(userRegisterForm.getEmail())){
                log.info("email format error");
                throw new mmallException(ResponseEnum.EMAIL_ERROR);
            }

            //phone check
            if(!RegexValidateUtil.checkMobile(userRegisterForm.getMobile())){
                log.info("mobile format error");
                throw new mmallException((ResponseEnum.MOBILE_ERROR));
            }

            // store data
            User user = new User();
            BeanUtils.copyProperties(userRegisterForm, user);
            user.setPassword(MD5utils.getSaltMD5(user.getPassword()));
            int insert = this.userMapper.insert(user);
            if(insert != 1){
                log.info("resgister fails");
                throw new mmallException(ResponseEnum.REGISTER_ERROR);
            }
            return user;
        }

    /**
     * login page
     * @param userLoginForm
     * @return
     */
        @Override
        public User login(UserLoginForm userLoginForm){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_name", userLoginForm.getLoginName());
            User one = this.userMapper.selectOne(queryWrapper);
            if(one == null){
                log.info("[login page] username doesn't exist");
                throw new mmallException(ResponseEnum.USERNAME_DOEST_EXIST);
            }

            boolean saltverifyMD5 = MD5utils.getSaltverifyMD5(userLoginForm.getPassword(), one.getPassword());
            if(!saltverifyMD5){
                log.info("[login page] password doesn't match");
                throw new mmallException(ResponseEnum.PASSWORD_DOEST_EXIST);
            }

            return one;
        }
}

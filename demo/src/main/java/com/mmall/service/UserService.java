package com.mmall.service;

import com.mmall.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.form.UserLoginForm;
import com.mmall.form.UserRegisterForm;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hao
 * @since 2023-11-08
 */
public interface UserService extends IService<User> {
    public User register(UserRegisterForm userRegisterForm);
    public User login(UserLoginForm userLoginForm);

}

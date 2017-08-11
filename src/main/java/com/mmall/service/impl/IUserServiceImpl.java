package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("用户名不存在");
        }

        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, MD5Password);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createdBySuccess("登录成功", user);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<String> register(User user) {
        ServiceResponse validResponse = this.checkVaild(user.getUsername(), Const.USERNAME);
        if (validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkVaild(user.getEmail(), Const.EMAIL);
        if (validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("注册失败");
        }
        return ServiceResponse.createdBySuccess("注册成功");
    }

    /**
     * 校验是否成功
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServiceResponse<String> checkVaild(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServiceResponse.createdByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServiceResponse.createdByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServiceResponse.createdByErrorMessage("参数错误");
        }
        return ServiceResponse.createdBySuccess("校验成功");
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    @Override
    public ServiceResponse selectQuestion(String username) {
        ServiceResponse validResponse = this.checkVaild(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServiceResponse.createdByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServiceResponse.createdBySuccess(question);
        }
        return ServiceResponse.createdByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 校验忘记密码问题
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServiceResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServiceResponse.createdBySuccess(forgetToken);
        }
        return ServiceResponse.createdByErrorMessage("回答的问题是错误的");
    }
}

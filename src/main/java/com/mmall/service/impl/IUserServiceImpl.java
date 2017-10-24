package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
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

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
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
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkVaild(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 校验是否有这用户
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
                    System.out.println("用户名已存在");
                    return ServiceResponse.createdByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    System.out.println("邮箱已存在");
                    return ServiceResponse.createdByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServiceResponse.createdByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccessMessage("校验成功");
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
            System.out.println("token:" + TokenCache.getKey(TokenCache.TOKEN_PREFIX + username));
            return ServiceResponse.createdBySuccess(forgetToken);
        }
        return ServiceResponse.createdByErrorMessage("回答的问题是错误的");
    }

    /**
     *未登录忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServiceResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServiceResponse.createdByErrorMessage("参数错误，需要传递token值");
        }

        ServiceResponse validResponse = this.checkVaild(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServiceResponse.createdByErrorMessage("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        System.out.println("token:" + token);
        if (StringUtils.isBlank(token)) {
            return ServiceResponse.createdByErrorMessage("token无效或者过期");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (resultCount > 0) {
                return ServiceResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServiceResponse.createdByErrorMessage("token失效，请重新获取token值");
        }
        return ServiceResponse.createdByErrorMessage("修改密码失败");
    }

    /**
     * 登陆后的重置密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServiceResponse.createdByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServiceResponse.createBySuccessMessage("密码修改成功");
        }
        return ServiceResponse.createdByErrorMessage("密码修改失败");
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<User> updataInfomation(User user) {
        String email = user.getEmail();
        int userId = user.getId();
        System.out.println("userId" + userId);
        int resultCount = userMapper.checkEmailByUserId(email, userId);
        if (resultCount > 0) {
            return ServiceResponse.createdByErrorMessage("email已存在，请更换email在更新");
        }

        User updataUser = new User();
        updataUser.setEmail(user.getEmail());
        updataUser.setPhone(user.getPhone());
        updataUser.setId(user.getId());
        updataUser.setQuestion(user.getQuestion());
        updataUser.setAnswer(user.getAnswer());
        resultCount = userMapper.updateByPrimaryKeySelective(updataUser);
        if (resultCount > 0) {
            return ServiceResponse.createdBySuccess("个人信息更新成功", updataUser);
        }
        return ServiceResponse.createdByErrorMessage("个人信息更新失败");
    }

    /**
     * 查找用户信息
     * @param userId
     * @return
     */
    @Override
    public ServiceResponse<User> getInformation(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createdBySuccess(user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServiceResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServiceResponse.createdBySuccess();
        }
        return ServiceResponse.createdByError();
    }




//    public ServiceResponse checkAdminRole(User user) {
//        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
//            return ServiceResponse.createdBySuccess();
//        }
//        return  ServiceResponse.createdByError();
//    }
}

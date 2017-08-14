package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value="login.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session) {
        System.out.println("登录" + username + "," + password);
        ServiceResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户退出
     * @param session
     * @return
     */
    @RequestMapping(value="logout.do", method= RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createdBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value="register.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 校验是否有这用户
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value="check_valid.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> checkValid(String str, String type) {
        return iUserService.checkVaild(str, type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value="get_user_info.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("用户未登录,无法获取用户信息");
        }
        return ServiceResponse.createdBySuccess(user);
    }

    /**
     * 忘记密码
     * @param username
     * @return
     */
    @RequestMapping(value="forget_get_question.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse forgetGetQuestion (String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 校验忘记密码问题
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value="forget_check_question.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetCheckQuestion(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 未登录忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value="forget_reset_password.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }

    /**
     *登陆后的重置密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping(value="reset_password.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createdByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value="updata_infomation.do", method=RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> updataInfomation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServiceResponse.createdByErrorMessage("用户未登录");
        }
        user.setId(user.getId());
        user.setEmail(user.getEmail());
        ServiceResponse response = iUserService.updataInfomation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

}

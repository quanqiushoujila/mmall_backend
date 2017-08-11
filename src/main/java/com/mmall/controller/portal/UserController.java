package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

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
     * 校验
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value="check_valid.do", method=RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> checkValid(String str, String type) {
        return iUserService.checkVaild(str, type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value="get_user_info.do", method=RequestMethod.GET)
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
    @RequestMapping(value="forget_get_question.do", method=RequestMethod.GET)
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
    @RequestMapping(value="forget_check_question.do", method=RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> forgetCheckQuestion(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }
}

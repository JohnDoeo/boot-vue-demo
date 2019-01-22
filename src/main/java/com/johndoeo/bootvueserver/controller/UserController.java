package com.johndoeo.bootvueserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.johndoeo.bootvueserver.constant.OperationCode;
import com.johndoeo.bootvueserver.module.User;
import com.johndoeo.bootvueserver.service.IUserService;
import com.johndoeo.bootvueserver.utils.OperationUtil;
import com.johndoeo.bootvueserver.websocket.MyWebsocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: JohnDoeo
 * @Date: 2019/1/21 22:03
 * @Description:
 */

/**
 * 解决在springboot项目返回json或者其他数据前台不报404的问题：
 *  方法一：直接使用@RestController注解（本人觉得最简单的用法） @RestController=@Controller+@ResponseBody
 *  方法二：在controller上加注解@ResponseBody并且在@RequestMapping注解中
 *          添加属性produces = "application/json;charset=UTF-8"，指定返回参数为json类型
 *  方法三：（针对json数据格式）如login1方法，直接把返回的数据写入HttpServletResponse对象中，再写出到浏览器
 *          本方法不能返回字段数据为空的字段
 */
@Controller//这个地方如果使用这个注解访问（调用）接口时会一直返回404

@ResponseBody
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")

//@RestController
//@RequestMapping(value = "/user")
@Validated
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private MyWebsocketHandler myWebsocketHandler;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login")
    public JSON login(@NotNull(message = "用户名不能为空")String username,
                      @NotNull(message = "密码不能为空")String password,
                      HttpServletRequest request){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        List<User> users = userService.selectByColumn(user);
        return Optional.ofNullable(users)
                .filter(us->us.size()>0)
                .map(us-> {
                    request.getSession().setAttribute("loginUser",us.get(0));
                    log.info("user is:"+us.get(0));
                    JSONObject object = new JSONObject();
                    object.put("title","测试提醒");
                    object.put("content","这是提醒内容");
                    JSONObject result = new JSONObject();
                    result.put("notice",object);
                    result.put("delay",60);
                    TextMessage textMessage = new TextMessage(result.toJSONString());
                    final boolean b = myWebsocketHandler.sendMessageToAllUsers(textMessage);
                    return OperationUtil.toJSON(OperationCode.GET,us.get(0));
                })
                .orElse(OperationUtil.toJSON(OperationCode.FAILED,-1,"登陆失败"));
    }

    @RequestMapping(value = "/login1")
    public void login1(User user,
                       HttpServletRequest request,
                       HttpServletResponse response){
        List<User> users = userService.selectByColumn(user);
        Optional.ofNullable(users)
                .filter(us->us.size()>0)
                .map(us-> {
                    request.getSession().setAttribute("loginUser",us.get(0));

                    JSON result = OperationUtil.toJSON(OperationCode.GET,us.get(0));
                    this.writeJson(response,result);
                    return 1;
                })
                .orElse(-1);
    }

    /**
     * 创建日期:2019年01月22日<br/>
     * 代码创建:johndoeo<br/>
     * 功能描述:写数据到浏览器上<br/>
     * @param resp
     * @param json
     */
    public void writeJson(HttpServletResponse resp , JSON json ){
        PrintWriter out = null;
        try {
            //设定类容为json的格式
            resp.setContentType("application/json;charset=UTF-8");
            out = resp.getWriter();
            //写到客户端
            out.write(json.toJSONString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(out != null){
                out.close();
            }
        }
    }
}

package com.bcc.web;

import com.bcc.MainLoader;
import com.bcc.helper.BeanHelper;
import com.bcc.helper.ConfigHelper;
import com.bcc.helper.ControllerHelper;
import com.bcc.util.CodecUtil;
import com.bcc.util.JsonUtil;
import com.bcc.util.ReflectionUtil;
import com.bcc.util.StreamUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web 层核心
 * 根据请求  request (method:path) , 找到对应的 handler (controller.action) 处理
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatchServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {

        MainLoader.init();
        ServletContext context = config.getServletContext();

        // jsp
        ServletRegistration registration = context.getServletRegistration("jsp");
        registration.addMapping(ConfigHelper.getAppJspPath() + "*");

        // asset 下静态资源
        ServletRegistration defaultRegistration = context.getServletRegistration("default");
        defaultRegistration.addMapping(ConfigHelper.getAppAssetPath() + "*");

    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<Request, Handler> actionMap = ControllerHelper.getActionMap();

        String method = request.getMethod().toLowerCase();
        String path = request.getPathInfo();
        Request requestAction = new Request(method, path);
        if (!actionMap.containsKey(requestAction)) {
            throw new RuntimeException("请求不存在 requestAction = " + requestAction);
        }
        Handler handler = actionMap.get(requestAction);
        Method actionMethod = handler.getActionMethod();
        Class<?> controllerClass = handler.getController();
        Object controllerBean = BeanHelper.getBean(controllerClass);

        // 获取请求参数 Param
        HashMap<String, Object> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String val = request.getParameter(name);
            paramMap.put(name, val);
        }
        String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
        if (body != null && body.length() > 0) {
            String[] params = body.trim().split("&");
            if (!ArrayUtils.isEmpty(params)) {
                for (String param : params) {
                    String[] arr = param.split("=");
                    if (arr.length == 2) {
                        paramMap.put(arr[0], arr[1]);
                    }
                }
            }
        }
        RequestParam param = new RequestParam(paramMap);

        // 调用 action 方法
        Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);

        // 跳转页面
        if (result instanceof ModelAndView) {
            ModelAndView view = (ModelAndView) result;
            String viewPath = view.getPath();
            if (StringUtils.isEmpty(viewPath)) {
                throw new RuntimeException("跳转路径不能为空");
            }
            if (viewPath.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + viewPath);   //  为什么要把有没有 / 区别对待 ???

            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + viewPath).forward(request, response);
            }

        } else if (result instanceof ServerResponse) {
            // 给浏览器写回 data 数据
            ServerResponse data = (ServerResponse) result;
            Object model = data.getModel();
            if (model != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                String json = JsonUtil.toJson(model);
                writer.write(json);
                writer.flush();
                writer.close();
            }

        } else {
            LOGGER.error("未知的返回类型 result= " + result);
        }

    }
}

package com.gs.framework;

import com.gs.framework.bean.Data;
import com.gs.framework.bean.Handler;
import com.gs.framework.bean.Param;
import com.gs.framework.bean.View;
import com.gs.framework.helper.*;
import com.gs.framework.util.JsonUtil;
import com.gs.framework.util.ReflectionUtil;
import com.gs.framework.util.StringUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
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
 * 请求转发器
 *
 * @author gao shan
 * @since 1.0.0
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        LoaderHelper.init();  //初始化 helper 类

        ServletContext servletContext = servletConfig.getServletContext();

        registerServlet(servletContext);

        //UploadHelper.init(servletContext);
    }

    //todo
    private void registerServlet(ServletContext servletContext) {
        //注册jsp的servlet,把 index.jsp, ./WEB-INF/View/* 下的jsp交给 org.apache.jasper.servlet.JspServlet  处理
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(              "/index.jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        //注册处理静态资源的servlet，把 /ico, /asset/* 下的文件 交给 org.apache.catalina.servlets.DefaultServlet 处理
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper.init(request, response); //todo
        try {
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath); //得到映射关系Handler


            if (handler != null) {
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass); //得到controller 对象实例

                Param param;
                if (UploadHelper.isMultipart(request)) {
                    param = UploadHelper.createParam(request);
                } else {
                    param = RequestHelper.createParam(request);
                }

                Object result;
                Method actionMethod = handler.getActionMethod();

                //反射调用Controller的方法，返回结果
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod,new String[1]);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }

                if (result instanceof View) {  //如果结果是View
                    handleViewResult((View) result, request, response);
                } else if (result instanceof Data) {  //如果结果是json
                    handleDataResult((Data) result, response);
                }
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    /**
     * 处理返回的VIEW
     * @param view
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
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
    }
}

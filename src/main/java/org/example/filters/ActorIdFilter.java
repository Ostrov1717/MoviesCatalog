package org.example.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/actor/*")
public class ActorIdFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String method = request.getMethod();

        if (method.equals("GET")|| method.equals("DELETE")) {
            String strKey = request.getRequestURI().substring(7);
            int intKey;
            try {
                intKey=Integer.parseInt(strKey);
            } catch (NumberFormatException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Введен неправильный id");
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}

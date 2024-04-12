package org.example.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.sql.*;
import java.util.Base64;

@WebFilter("/*")
public class AutentificationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest ServRequest, ServletResponse ServResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) ServRequest;
        HttpServletResponse response = (HttpServletResponse) ServResponse;
        response.setContentType("plain/text; charset=utf-8");
        String method = request.getMethod();

        if (method.equals("POST")|| method.equals("DELETE")) {
            String authHeader =request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String credentials = new String(Base64.getDecoder().decode(authHeader.substring(6)));
                String[] split = credentials.split(":");
                String usernameProvided = split[0];
                String passwordProvided = split[1];
                try {
                    if (authenticatedUser(usernameProvided,passwordProvided,request)) {
                        // Пользователь прошел аутентификацию, продолжаем цепочку фильтров
                       filterChain.doFilter(request, response);
                    } else {
                        // Неправильные аутентификационные данные, отправляем ответ с ошибкой 403 Forbidden
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().println("Пользователь не уполномочен вносить изменения в каталог");
                    }
                } catch (SQLException e) {
                    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                    response.getWriter().println("Ошибка сервера при попытке аутентифицировать пользователя");;
                }
            } else {
                // Если заголовок Authorization отсутствует, отправляем ответ с ошибкой 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("WWW-Authenticate", "Basic realm=\"Restricted Area\"");
                response.getWriter().println("Для проведения операции требуется аутентифкация");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean authenticatedUser(String usernameProvided, String passwordProvided, HttpServletRequest request ) throws SQLException {
            Connection connection=(Connection)request.getServletContext().getAttribute("dbconnection");
                Statement statement = connection.createStatement();
                String sqlQwery="select * from users where username='" + usernameProvided +"'";
                ResultSet resultSet = statement.executeQuery(sqlQwery);
                if (resultSet != null) {
                    while (resultSet.next()) {
                        String userName = resultSet.getString("username");
                        String userPassword = resultSet.getString("password");
                        System.out.println(userName + " -- " + userPassword);
                        if (userPassword.equals(passwordProvided)) return true;
                    }
                } return false;
        }
    }

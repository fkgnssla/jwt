package com.example.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //토큰: "cos" => 지금까진 임의의 토큰을 썼으니 이제 토큰을 만들어줘야함.
        //언제? id,pw가 정상적으로 들어와서 로그인이 되면 토큰 생성 후 응답으로 반환
        //요청할 때 마다 header의 Authorization에 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증하면 된다. (RSA, HS256)
        if(req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨.");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터3");

            if(headerAuth.equals("cos")) {
                chain.doFilter(request, response);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }
    }
}

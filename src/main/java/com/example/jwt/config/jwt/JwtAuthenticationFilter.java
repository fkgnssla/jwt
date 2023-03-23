package com.example.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// (Post, /login, username, password) 요청하면 UsernamePasswordAuthenticationFilter가 동작.
//하지만 FormLogin을 disable했기에 동작 안 하므로 우리가 만들어서 시큐리티 필터에 등록해야함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter 로그인 시도중");

        //1. username, password 받아서
        //2. 정상인지 로그인 시도를 해본다.
        //authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출되어 loadUserByUsername() 실행
        //3. PrincipalDetails를 세션에 담고 => (권한관리를 위한 것, 만약 권한을 안 쓴다면 PrincipalDetails,PrincipalDetailsService 필요 X)
        //4. JWT 토큰을 만들어서 응답해주면 된다.
        return super.attemptAuthentication(request, response);
    }

}

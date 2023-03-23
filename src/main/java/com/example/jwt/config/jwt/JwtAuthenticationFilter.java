package com.example.jwt.config.jwt;

import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            //토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //PrincipalDetailsService의 loadByUsername() 실행
            //정상적으로 Authentication가 반환되면 DB에 있는 데이터와 username, password가 일치한다는 것.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            //authentication 객체를 세션에 저장해야 한다. 그 방법이 return 하는 것
            //권한관리를 security가 해주기에 편해서 세션에 저장하는 것.
            //굳이 JWT를 쓰면서 세션을 만들 이유가 없다. 단지 권한처리때문에 세션에 넣어준다.
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //2. 정상인지 로그인 시도를 해본다.
        //authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출되어 loadUserByUsername() 실행
        //3. PrincipalDetails를 세션에 담고 => (권한관리를 위한 것, 만약 권한을 안 쓴다면 PrincipalDetails,PrincipalDetailsService 필요 X)
        //4. JWT 토큰을 만들어서 응답해주면 된다.
    }

    //attemptAuthentication() 실행후 인증이 정상적으로 되면 successfulAuthentication() 실행
    //여기서 JWT 토큰을 만들어서 request 요청한 클라이언트에게 JWT 토큰을 response해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행: 인증 완료!");
        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();

    }
}

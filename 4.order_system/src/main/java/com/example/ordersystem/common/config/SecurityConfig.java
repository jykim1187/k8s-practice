package com.example.ordersystem.common.config;

import com.example.ordersystem.common.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Array;
import java.util.Arrays;

@Configuration
@EnableMethodSecurity //PreAuthorized사용시 해당 어노테이션 필요
public class SecurityConfig {
    private  final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean//리턴하는 객체에 싱글톤 객체를 생성하는 어노테이션
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//                spring security에서 cors정책 지정
                .cors(c->c.configurationSource(corsConfiguration()))
//                csrf는 쿠키에 담긴 토큰값을 활용하여 사이트를 공격하는 방식(csrf)에 대해 대비하지 않겠다.
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic보안 방식 비활성화(구식 보안방식임)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션로그인방식 x
//                token을 검증하고, token을 통해 authentication객체 생성
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)//username과 password를 통해  authentication객체가 있는지 없는지 검사하는 클래스?
//                .requestMatchers("url").permitAll.=>해당 url에 대해서는 인증없이 허용하겠다(즉 authntication객체가 없어도(=토큰이 없어도))
//                .authenticated() : 모든 요청에 대해서 Authentication객체가 생성되기를 요구
                .authorizeHttpRequests(a->a.requestMatchers("/member/create","member/doLogin","/member/refresh-token","product/list").permitAll().anyRequest().authenticated())


                .build();
    }



    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","https://www.jy1187.shop")); //리스트의 형태로 넣어야하기 때문에 Arrys.asList
        configuration.setAllowedMethods(Arrays.asList("*")); //모든 HTTP메서드(get,post)메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); //모든 헤더 허용
        configuration.setAllowCredentials(true);//자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration); //3000이후의 모든 url패턴에 대해 cors설정을 적용(localhost:3000/abc/bcd 이런식으로 url은 다양하니까)
        return source;
    }

    @Bean
    public PasswordEncoder makePassword(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}

package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.security.JwtAuthenticationFilter;
//<스프링부트 시큐리티> 보안성을 활성화 하기에 필요한 기능들 모음
@Configuration
@EnableWebSecurity//웹 보안을 활성화 하는데 사용한다.
//스프링 시큐리티 설정을 활성화
public class WebSecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean //bean을 직접 만드는 어노테이션
	protected DefaultSecurityFilterChain securityFilterChain(
			HttpSecurity http) throws Exception{
		//HttpSecurity 설정 기능들 
		//웹 보안을 설정하는데 사용되는 클래스
		//인증, 권한부여, csrf 보호 세션관리등을 할 수 있다.
		http
			//WebMvcConfig에서 이미 cors설정을 했으므로 시큐리티에서는 비활성화
			.cors(corsConfigurer -> corsConfigurer.disable())
			//csrf : 사용자가 인증된 상태에서 공격자가 악의적으로 요청을 보내 사용자의 권한을 악용하는 공격기법
			//API 서버가 클라이언트에서 Rest요청을 받을 때 CSRF보호가 불필요할 수 있다.
			.csrf(csrfConfigurer -> csrfConfigurer.disable())
			//JWT(토큰)을 사용하므로 Basic 인증은 비활성화
			.httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())
			//세션을 사용하지 않는다.
			//sessionCreationPolicy() : 세션 정책을 설정하는 메서드
			//SessionCreationPolicy.STATELESS : 세션을 생성하지 않고 stateless 방식으로 동작하도록 설정
			.sessionManagement(sessionManagementConfigurer ->
            	sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	)
			//요청에 대한 인증/인가 설정
			.authorizeHttpRequests(authorizeRequestsConfigurer -> 
				authorizeRequestsConfigurer
				.requestMatchers("/", "/auth/**").permitAll() // 특정 경로는 인증 없이 허용
				.anyRequest().authenticated()
				// 그 외의 요청은 인증 필요
			);
		//filter등록
		//CorsFilter를 실행한 후
		//jwtAuthenticationFilter를 실행하겠다 라는 뜻
		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
		//addFilterAfter()
				//필터 체인에서 첫번째 인자로 제공한 필터를 두번째 인자로 제공한 필터 뒤에 추가하겠다는 의미이다.
				//필터가 A, B, C 순서로 실행되길 원한다면:
				//http.addFilterAfter(B, A.class) → A 다음에 B 실행
				//http.addFilterAfter(C, B.class) → B 다음에 C 실행
		return http.build();
			
	}
}
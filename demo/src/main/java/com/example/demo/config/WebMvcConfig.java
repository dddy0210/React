package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//스프링에서는 해당 클래스를 설정파일로 인식하고 Bean으로 등록한다. 

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
//WebMvcConfigurer
//스프링 mvc동작을 커스터마이징 할 때 사용된다.
//스프링은 기본적으로 mvc동작을 설정해주지만, 때로는 프로젝트 요구사항에 맡게 특정 기능을 추가하거나
//수정해야 할 때 WebMvcConfigurer를 구현하여 원하는 설정을 적용할 수 있다. 
//주요기능
	
	//브라우저가 CORS 요청 결과를 캐싱하는데 최대 시간 설정 
	private final long MAX_AGE_SECS = 3600;
	
//CORS 설정
	@Override
    public void addCorsMappings(CorsRegistry registry) {
		
        //모든 경로에 대해 CORS 설정을 적용 
        registry.addMapping("/**")
        //React 애플리케이션이 실행되는 도메인(출처)에서 오는 요청을 허용 
        .allowedOrigins("http://localhost:3000")
        //http메서드(GET,POST,DELETE를 허용)
        .allowedMethods("GET","POST","PUT","DELETE")
        //모든 헤더를 허용하겠다.
        .allowedHeaders("*")
        //루키나 인증 정보를 포함한 요청을 허용 
        .allowCredentials(true)
        //브라우저가 서버로부터 받은 응답을 일정 시간동안 저장해두고, 그 시간내에 동일한 요청이 있을 경우
        //서버에 다시 요청하지 않고 저장된 응답을 재사용한다. 
        .maxAge(MAX_AGE_SECS);
	
//인터셉터(Interceptors)추가
//특정 http요청이 컨트롤러에 도달하기 전 또는 후에 실행되며, 요청을 가로채어 로깅,인증,권한 확인등의 작업을
//수행할 수 있다. 
}
}
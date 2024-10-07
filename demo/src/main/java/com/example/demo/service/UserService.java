package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;
import com.example.demo.presistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class UserService {
	
	@Autowired//스프링이 userrepository 타입의 bean을 자동으로 주입해준다. 
	private UserRepository repository;
	
	//유저를 생성하는 메서드
	public UserEntity create(UserEntity userEntity) {
		//주어진 userEntity가 null이거나 username이 null인경우 예회 발생 
		if(userEntity == null || userEntity.getUsername() == null) {
			//유효하지 않은 인자에 대해 예외를 발생시킨다. 
			throw new RuntimeException("Invaild arguments");
		}
		//Entity에서 username을 가져와 상수에 저장 
		final String username = userEntity.getUsername();
		
		//주어진 username 이미 존재하는 경우, 경고 로그를 남기고 예외를 던진다.
		if(repository.existsByUsername(username)) {
			//이미 존재하는 username에 대해 로그를 기록한다.
			log.warn("Username already exists {}", username);
			throw new RuntimeException("Username already exists");
		}
		//username이 중복되지 않았다면, UserEntity를 데이터베이스에 저장
		return repository.save(userEntity);
	}
		//주어진 username과 password로 UserEntity 조회하기
		public UserEntity getByCredentials(String username, String password,PasswordEncoder encoder) {
			//DB에 저장된 암호화된 비밀번호와 사용자에게 입력 받아 전달된 암호화된 비밀번호를 비교 
	        UserEntity originalUser = repository.findByUsername(username);
	        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
	    		return originalUser;
		
	}
	        return null;
}
}
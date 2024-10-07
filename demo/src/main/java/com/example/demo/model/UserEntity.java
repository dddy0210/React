package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="username")}) 
//제약조건 (중복X, null만 가능) 테이블에서 username컬럼에 유니크제약조건 설정
//동일한 username을 가진 유저는 생성될 수 없다. 

public class UserEntity {

	@Id // JPA에서 id필드가 테이블의 Primary key임을 나타낸다. 
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name="system-uuid",strategy= "uuid")
	private String id; //유저에게 고유하게 부여되는 id, uuid로 생성됨
	private String username;//아이디로 사용할 유저네임. 이메일 형식으로 만든다 
	private String password;//비밀번호
	private String role;//유저의 권한(관리자,일반사용자 구별) 
	private String authProvider;//OAuth 소셜로그인시 사용할 유저 정보 제공자 
	
	
}

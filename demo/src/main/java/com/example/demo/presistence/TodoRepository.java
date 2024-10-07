package com.example.demo.presistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TodoEntity;

//JpaRepository<T, ID> jpa에서 제공하는 인터페이스
// T : 테이블에 매핑될 Entity클래스
// ID : 기본키의 타입
@Repository//@Component의 자식 컴포넌트, 컴포넌트 스캔시 Bean으로 생성됨
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

	//기본쿼리가 아닌 쿼리는 어떻게 처리해야할까?
	//JPA가 메서드의 이름을 파싱해서 SELECT * FROM Todo WHERE userId = '{userId}'와 같은
	//쿼리를 작성해서 실행해준다. 
	//?1: 메서드의 매개변수 순서 위치 
	@Query("SELECT t FROM TodoEntity t where t.userId=?1")
	List<TodoEntity> findByUserId(String userId);
}

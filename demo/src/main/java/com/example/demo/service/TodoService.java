package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.presistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;

	
	

	//메서드 형태로 비즈니스로직을 구현
	public String testService() {
		//TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		//TodoEntity 저장
		//sava() : insert
		repository.save(entity);
		//TodoEntity 검색
		//findById(entity.getId()) 기본키를 이용해 db에 저장된 TodoEntity찾기
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		return savedEntity.getTitle();
	}

	
	//Controller에서 넘어온 데이터를 검증하고 Todo테이블에 저장
	//데이터를 추가하고, 추가가 잘 됐는지 확인하는 로직
	public List<TodoEntity> create(TodoEntity entity){
		//매개변수로 넘어온 Entity가 유효한지 검사한다.
		validate(entity);
		
		//넘어온 Entity에 문제가 없을 때 db에 추가한다.
		repository.save(entity);
		
        //{}는 SLF4J에서 제공하는 플레이스홀더로, 두 번째 매개변수로 전달된 entity.getId() 값이 여기에 대입되어 출력된다.
		log.info("Entity Id : {} is saved",entity.getId());
		
		//넘어온 entity로부터 userId를 가지고 db에서 조회된 내용을 List에 묶어서 반환
		//SELECT * FROM TodoEntity where userid = 'temporary-user';
		//실행해서 조회된 결과를 List에 묶어서 반환하겠다.
		return repository.findByUserId(entity.getUserId());
	}
	
	public List<TodoEntity> delete(TodoEntity entity){
		//1. 엔티티가 유효한지 확인
		validate(entity);
		try {
			repository.delete(entity);
			//엔티티를 삭제
		} catch (Exception e) {
			//예외 발생시 id와 exception을 로그로 찍는다.
			log.error("Error deleting entity with id: {}", entity.getId(), e);
			
			//컨트롤러로 exception을 날린다.
			//데이터베이스 내부 로직을 캡슐화 하기 위해 e를 반환하지 않고 새 exception객체를 반환한다.
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		//새 Todo 리스트를 가져와 반환한다.
		return retrieve(entity.getUserId());
	}
	
	//Entity를 수정하고, 수정한 entity를 확인할 수 있는 update메서드
	//TodoEntity entity 매개변수는  수정된 값이 들어있다.
	public List<TodoEntity> update(TodoEntity entity){
		//1. 수정할 entity가 유효한지 확인
		validate(entity);
		
		//존재하지 않는 entity는 업데이트 할 수 없음
		//기존에 저장되어있는 데이터를 조회
		Optional<TodoEntity> original = repository.findById(entity.getId());
		
//		original.ifPresent(todo -> {
//			//반환된 todoEntity가 존재하면 값을 새 Entity로 덮어쓴다.
//			todo.setTitle(entity.getTitle());
//			todo.setDone(entity.isDone());
//			
//			//데이터베이스에 새 값을 저장한다.
//			repository.save(todo);	
//		});
		
		if(original.isPresent()) {
			TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			repository.save(todo);
		}
		
		//수정이 잘 됬는지 확인하기 위해 retrieve 메서드를 실행한 결과를 반환
		return retrieve(entity.getUserId());
		
	}
	
	private void validate(TodoEntity entity) {
		//전달된 TodoEntity가 null인지 확인합니다.
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null");
		}
		
		//userId가 안넘어왔을 때
		if(entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
		
	}
	
	//userId를 넘겨받아 해당유저가 추가한 Todo를 모두 조회하는 메서드
	public List<TodoEntity> retrieve(String userId){
		return repository.findByUserId(userId);
	}
	
}
package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDTO;
import com.example.todoapp.entity.TodoEntity;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoDTO createTodo(TodoDTO dto) {
//        validateTitleLength(todo.getTitle());
        validateTitle(dto.getTitle());
        TodoEntity entity = new TodoEntity(
                dto.getTitle(), dto.getContent(), dto.isCompleted()
        );

        TodoEntity saved = todoRepository.save(entity);

        return toDTO(saved);
    }

    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TodoEntity findEntityById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("todo not found : id" + id));
    }


    public TodoDTO getTodoById(Long id) {
        TodoEntity entity = findEntityById(id);
        return toDTO(entity);
    }

    public void deleteTodoById(Long id) {
//        getTodoById(id);
        findEntityById(id);
        todoRepository.deleteById(id);

    }

    public void  validateTitleLength(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어있을 수 없습니다.");
        }
        if (title.length() > 50) {
            throw new IllegalArgumentException("제목은 50자를 초과할 수 없습니다.");
        }
    }

    public TodoDTO updateTodoById(Long id, TodoDTO dto) {
//        validateTitleLength(dto.getTitle());
        validateTitle(dto.getTitle());
        TodoEntity entity = findEntityById(id);

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setCompleted(dto.isCompleted());

        return toDTO(entity);
    }

    public List<TodoDTO> searchTodos(String keyword){
        return todoRepository.findByTitleContaining(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    public List<TodoDTO> getTodosByCompleted(boolean completed){
        return todoRepository.findByCompleted(completed).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TodoDTO toggleCompleted(Long id){
        TodoEntity entity = findEntityById(id);
        entity.setCompleted(!entity.isCompleted());
        return toDTO(entity);
    }

//    public long getTotalCount() {
//        return todoRepository.countAll();
//    }
//
//    public long getCompletedCount() {
//        return todoRepository.countCompleted();
//    }
//
//    public long getActiveCount() {
//        return todoRepository.countActive();
//    }

//    public void deleteCompletedTodos() {
//        todoRepository.deleteCompleted();
//    }

    private void validateTitle(String title) {
        if(title ==  null || title.isBlank() || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if(title.length() > 50) {
            throw new IllegalArgumentException("제목은 50자를 넘을 수 없습니다.");
        }
    }

    public long getTotalCount2() {
        return todoRepository.findAll().size();
    };

    public long getCompletedCount2() {
        return todoRepository.findByCompleted(true).size();
    }

    public long getActiveCount2() {
        return todoRepository.findByCompleted(false).size();
    }

    public void deleteCompletedTodos() {
        todoRepository.deleteByCompleted(true);
    }

    //DB에 접근 할땐 entity, controller로 데이터를 넘겨줄 땐 DTO로 변환 해서 넘겨줘야 함
    private TodoDTO toDTO(TodoEntity entity){
        TodoDTO dto = new TodoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCompleted(entity.isCompleted());
        return dto;
    }


}

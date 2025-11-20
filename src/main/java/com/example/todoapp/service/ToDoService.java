package com.example.todoapp.service;

import com.example.todoapp.dto.ToDoDTO;
import com.example.todoapp.repository.ToDoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public List<ToDoDTO> getAllTodos() {
        return toDoRepository.findAll();
    }

    public ToDoDTO getTodoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("todo not found : id" + id));
    }

    public void deleteTodoById(Long id) {
        getTodoById(id);
        toDoRepository.deleteById(id);

    }

    public void  validateTitleLength(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 비어있을 수 없습니다.");
        }
        if (title.length() > 50) {
            throw new IllegalArgumentException("제목은 50자를 초과할 수 없습니다.");
        }
    }

    public ToDoDTO updateTodoById(Long id, ToDoDTO newTodo) {
//        validateTitleLength(newTodo.getTitle());
        validateTitle(newTodo.getTitle());
        ToDoDTO originTodo = getTodoById(id);

        originTodo.setTitle(newTodo.getTitle());
        originTodo.setContent(newTodo.getContent());
        originTodo.setCompleted(newTodo.isCompleted());

        return toDoRepository.save(originTodo);
    }

    public ToDoDTO createTodo(ToDoDTO todo) {
//        validateTitleLength(todo.getTitle());
        validateTitle(todo.getTitle());
        return toDoRepository.save(todo);
    }

    public List<ToDoDTO> searchTodos(String keyword){
        return toDoRepository.findByTitleContaining(keyword);
    }

    public List<ToDoDTO> getTodosByCompleted(boolean completed){
        return toDoRepository.findByCompleted(completed);
    }

    public ToDoDTO toggleCompleted(Long id){
        ToDoDTO todo = getTodoById(id);
        todo.setCompleted(!todo.isCompleted());
        return toDoRepository.save(todo);
    }

    public long getTotalCount() {
        return toDoRepository.countAll();
    }

    public long getCompletedCount() {
        return toDoRepository.countCompleted();
    }

    public long getActiveCount() {
        return toDoRepository.countActive();
    }

//    public void deleteCompletedTodos() {
//        toDoRepository.deleteCompleted();
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
        return toDoRepository.findAll().size();
    };

    public long getCompletedCount2() {
        return toDoRepository.findByCompleted(true).size();
    }

    public long getActiveCount2() {
        return toDoRepository.findByCompleted(false).size();
    }

    public void deleteCompletedTodos() {
        toDoRepository.deleteCompleted();
    }
}

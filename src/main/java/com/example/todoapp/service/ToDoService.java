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

    public ToDoDTO updateTodoById(Long id, ToDoDTO newTodo) {
        ToDoDTO originTodo = getTodoById(id);
        originTodo.setTitle(newTodo.getTitle());
        originTodo.setContent(newTodo.getContent());
        originTodo.setCompleted(newTodo.isCompleted());

        return toDoRepository.save(originTodo);
    }

    public ToDoDTO createTodo(ToDoDTO todo) {
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

}

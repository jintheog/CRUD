package com.example.todoapp.repository;

import com.example.todoapp.dto.ToDoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToDoRepository {
    private final Map<Long, ToDoDTO> storage = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    public ToDoDTO save(ToDoDTO todo) {
        todo.setId(nextId++);
        storage.put(todo.getId(),todo);
        return todo;
    }

    public List<ToDoDTO> findAll() {
        return new ArrayList<>(storage.values());
    }

    public ToDoDTO findById(Long id) {
        return storage.get(id); //Map 형식이기 때문에 get함수를 씀
    }

}

package com.example.todoapp.repository;

import com.example.todoapp.dto.ToDoDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ToDoRepository {
    private final Map<Long, ToDoDTO> storage = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    public ToDoDTO save(ToDoDTO todo) {
        if(todo.getId() == null){
            todo.setId(nextId++);
        }
        storage.put(todo.getId(),todo);
        return todo;
    }

    public List<ToDoDTO> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<ToDoDTO> findById(Long id) {
        //return storage.get(id); //Map 형식이기 때문에 get함수를 씀
        return Optional.ofNullable(storage.get(id));


    }

    public ToDoDTO deleteById(Long id) {
        return storage.remove(id);
    }

    public List<ToDoDTO> findByTitleContaining(String keyword) {
        return storage.values().stream()
                .filter((todo)->todo.getTitle().contains(keyword))
                .toList();
    }
    public List<ToDoDTO> findByCompleted(boolean completed) {
        return storage.values().stream()
                .filter((todo)->todo.isCompleted() == completed)
                .toList();
    }

    public long countAll() {
        return storage.size();
    }

    public long countCompleted() {
        return storage.values().stream()
                .filter(ToDoDTO::isCompleted)
                .count();
    }

    public long countActive() {
        return storage.values().stream()
                .filter(todo -> !todo.isCompleted())
                .count();
    }

//    public void deleteCompleted() {
//        storage.values().removeIf(ToDoDTO::isCompleted);
//    }

    public void deleteCompleted() {
        storage.entrySet().removeIf(item -> item.getValue().isCompleted()
        );
    }

}

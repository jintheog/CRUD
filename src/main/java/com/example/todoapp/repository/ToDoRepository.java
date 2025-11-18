package com.example.todoapp.repository;

import com.example.todoapp.dto.ToDoDTO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ToDoRepository {
    private final Map<Long, ToDoDTO> storage = new ConcurrentHashMap<>();
    private Long nextId = 1L;
}

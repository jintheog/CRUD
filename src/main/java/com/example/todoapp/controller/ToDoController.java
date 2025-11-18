package com.example.todoapp.controller;

import com.example.todoapp.dto.ToDoDTO;
import com.example.todoapp.repository.ToDoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ToDoController {

    @GetMapping("/todos")
    public String todos() {


        return "todos";
    }

    @GetMapping("/todos/new")
    public String newTodo() {
        return "new";
    }

    @GetMapping("/todos/create")
    public String create(
            @RequestParam String title,
            @RequestParam String content,
            Model model
    ) {
        ToDoDTO toDoDTO = new ToDoDTO(null, title, content, false);
        ToDoRepository toDoRepository = new ToDoRepository();

        ToDoDTO todo = toDoRepository.save(toDoDTO);

        model.addAttribute("todo", todo);

        return "create";
    }


}

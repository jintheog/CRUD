package com.example.todoapp.controller;

import com.example.todoapp.dto.ToDoDTO;
import com.example.todoapp.repository.ToDoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ToDoController {
    private final ToDoRepository toDoRepository = new ToDoRepository();
    @GetMapping("/todos")
    public String todos(Model model) {
        //↓ 이전에 create 함수에서 만든 repository와 다른 객체(저장소)를 새로 만들어서 사용 하면 안됨
        //ToDoRepository toDoRepository = new ToDoRepository();
        // 저장소 객체를 메서드 내부가 아니라 클래스 내부에서 만들어야 함.
        List<ToDoDTO> todos = toDoRepository.findAll();
        model.addAttribute("todos", todos);
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
//        ToDoRepository toDoRepository = new ToDoRepository();
        ToDoDTO todo = toDoRepository.save(toDoDTO);

        model.addAttribute("todo", todo);

//        return "create";
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}")
    public String detail(
            @PathVariable Long id, Model model){
        ToDoDTO todo =  toDoRepository.findById(id);
        model.addAttribute("todo", todo);
        return "detail";
    }


}

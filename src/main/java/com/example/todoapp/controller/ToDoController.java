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
    // repository를 컨트롤러에서도 쓰고 다른 클래스에 (TodoappApplication)서도 같은 저장소를 재사용해서 써야 함.
    //@Repository를 씀
    //private final ToDoRepository toDoRepository = new ToDoRepository();
    private final ToDoRepository toDoRepository;

    public ToDoController(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

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
//        ToDoDTO todo =  toDoRepository.findById(id);
        try{
            ToDoDTO todo =  toDoRepository.findById(id)
                    .orElseThrow(()-> new IllegalArgumentException("todo not found!"));
            model.addAttribute("todo", todo);
            return "detail";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }


    }

    @GetMapping("/todos/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        //삭제로직
        toDoRepository.deleteById(id);
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        try {
            ToDoDTO todo = toDoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!"));
            model.addAttribute("todo", todo);
            return "edit";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/todos/{id}/update")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String content,
                         @RequestParam (defaultValue = "false") Boolean completed,
                         Model model) {
        try {
            ToDoDTO todo = toDoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("todo not found!"));

            todo.setTitle(title);
            todo.setContent(content);
            todo.setCompleted(completed);

            return "redirect:/todos/" + id;
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }


}

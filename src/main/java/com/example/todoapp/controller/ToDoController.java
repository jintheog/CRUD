package com.example.todoapp.controller;

import com.example.todoapp.dto.ToDoDTO;
import com.example.todoapp.repository.ToDoRepository;
import com.example.todoapp.service.ToDoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class ToDoController {
    // repository를 컨트롤러에서도 쓰고 다른 클래스에 (TodoappApplication)서도 같은 저장소를 재사용해서 써야 함.
    //@Repository를 씀
    //private final ToDoRepository toDoRepository = new ToDoRepository();
//    private final ToDoRepository toDoRepository;
//
//    public ToDoController(ToDoRepository toDoRepository) {
//        this.toDoRepository = toDoRepository;
//    }

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public String todos(Model model) {
        //↓ 이전에 create 함수에서 만든 repository와 다른 객체(저장소)를 새로 만들어서 사용 하면 안됨
        //ToDoRepository toDoRepository = new ToDoRepository();
        // 저장소 객체를 메서드 내부가 아니라 클래스 내부에서 만들어야 함.
//        List<ToDoDTO> todos = toDoRepository.findAll();
        List<ToDoDTO> todos = toDoService.getAllTodos();
        model.addAttribute("todos", todos);

//        model.addAttribute("totalCount", toDoService.getTotalCount());
//        model.addAttribute("completedCount", toDoService.getCompletedCount());
//        model.addAttribute("activeCount", toDoService.getActiveCount());

        model.addAttribute("totalCount", toDoService.getTotalCount2());
        model.addAttribute("completedCount", toDoService.getCompletedCount2());
        model.addAttribute("activeCount", toDoService.getActiveCount2());

        return "todos";
    }

    @GetMapping("/new")
    public String newTodo(Model model) {
//        model.addAttribute("todo", new ToDoDTO());
        if (!model.containsAttribute("todo")) {
            model.addAttribute("todo", new ToDoDTO());
        }
        return "form";
    }

    @PostMapping
    public String create(
//            @RequestParam String title,
//            @RequestParam String content,
            @ModelAttribute ToDoDTO todo,
            RedirectAttributes redirectAttributes
//            Model model
    ) {
//        ToDoDTO toDoDTO = new ToDoDTO(null, title, content, false);
//        ToDoRepository toDoRepository = new ToDoRepository();
//        ToDoDTO todo = toDoRepository.save(toDoDTO);
//        toDoRepository.save(todo);
        try{
            toDoService.createTodo(todo);
            redirectAttributes.addFlashAttribute("message", "할 일이 생성되었습니다.");
            return "redirect:/todos";
        } catch(IllegalArgumentException e){
//            redirectAttributes.addFlashAttribute("message", e.getMessage());
//            redirectAttributes.addFlashAttribute("status", "error");
//            redirectAttributes.addFlashAttribute("todo", todo);
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:/todos/new";

        }

//        model.addAttribute("todo", todo);


//        return "create";

    }

    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id, Model model){
//        ToDoDTO todo =  toDoRepository.findById(id);
        try{
//            ToDoDTO todo =  toDoRepository.findById(id)
//                    .orElseThrow(()-> new IllegalArgumentException("todo not found!"));
            ToDoDTO todo = toDoService.getTodoById(id);
            model.addAttribute("todo", todo);
            return "detail";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }


    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        //삭제로직
//        toDoRepository.deleteById(id);
        toDoService.deleteTodoById(id);
        redirectAttributes.addFlashAttribute("message", "할 일이 삭제 되었습니다");
        redirectAttributes.addFlashAttribute("status", "delete");
        return "redirect:/todos";
    }

    @GetMapping("/{id}/update")
    public String edit(@PathVariable Long id, Model model) {
        try {
//            ToDoDTO todo = toDoRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("todo not found!"));
            ToDoDTO todo = toDoService.getTodoById(id);
            model.addAttribute("todo", todo);
            return "form";
        } catch (IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
//                         @RequestParam String title,
//                         @RequestParam String content,
//                         @RequestParam (defaultValue = "false") Boolean completed,
                         @ModelAttribute ToDoDTO todo,
                         RedirectAttributes redirectAttributes
//                         Model model
                        ) {
//        try {
//            toDoService.validateTitleLength(todo.getTitle());
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("message", e.getMessage());
//            redirectAttributes.addFlashAttribute("status", "error");
//            redirectAttributes.addFlashAttribute("todo", todo);
//            return "redirect:/todos/" + id + "/update";
//        }

        try {
//            ToDoDTO todo = toDoRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("todo not found!"));

//            todo.setTitle(title);
//            todo.setContent(content);
//            todo.setCompleted(completed);
//            todo.setId(id);
//            toDoRepository.save(todo);
            toDoService.updateTodoById(id, todo);
            redirectAttributes.addFlashAttribute("message", "할 일이 수정되었습니다.");

            return "redirect:/todos/" + id;
        } catch (IllegalArgumentException e) {
            if(e.getMessage().contains("제목")){
                redirectAttributes.addFlashAttribute("error",e.getMessage());
                return "redirect:/todos"+id+"/update";
            }
            else {
                redirectAttributes.addFlashAttribute("message", "없는 to do 입니다.");
                return "redirect:/todos";
            }

        }
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
//        List<ToDoDTO> todos = toDoRepository.findByTitleContaining(keyword);
        List<ToDoDTO> todos = toDoService.searchTodos(keyword);
        model.addAttribute("todos", todos);

        return "todos";
    }

    @GetMapping("/active")
    public String active(Model model) {
//        List<ToDoDTO> todos = toDoRepository.findByCompleted(false);
        List<ToDoDTO> todos = toDoService.getTodosByCompleted(false);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/completed")
    public String completed(Model model) {
//        List<ToDoDTO> todos = toDoRepository.findByCompleted(true);
        List<ToDoDTO> todos = toDoService.getTodosByCompleted(true);
        model.addAttribute("todos", todos);
        return "todos";
    }

    @GetMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id, Model model) {
        try{
//            ToDoDTO todo = toDoRepository.findById(id)
//                    .orElseThrow(()-> new IllegalArgumentException("todo not found"));
//            todo.setCompleted(!todo.isCompleted());
//            toDoRepository.save(todo);
            toDoService.toggleCompleted(id);
            return "redirect:/todos/" + id;
        } catch(IllegalArgumentException e) {
            return "redirect:/todos";
        }
    }

//    @PostMapping("/delete/completed")
//    public String deleteCompleted(RedirectAttributes redirectAttributes) {
//        toDoService.deleteCompletedTodos();
//        redirectAttributes.addFlashAttribute("message", "완료된 할 일이 모두 삭제되었습니다.");
//        redirectAttributes.addFlashAttribute("status", "delete");
//        return "redirect:/todos";
//    }


    // 1. 제목 검증 추가
    // - 제목이 비어있으면 예외
    // - 제목이 50자 초과시 예외

    // 2. 통계 기능 추가
    // - 전체, 완료된, 미완료 할 일 개수 => /todos 에 표시
    
    // 3. 완료된 할 일 일괄 삭제
    @GetMapping("/delete-completed")
    public String deleteCompleted(RedirectAttributes redirectAttributes) {
        // delete all
        toDoService.deleteCompletedTodos();
        redirectAttributes.addFlashAttribute("message","완료된 할 일 삭제");
        return "redirect:/todos";
    }



}

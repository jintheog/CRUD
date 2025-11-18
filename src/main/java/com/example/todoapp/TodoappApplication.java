package com.example.todoapp;

import com.example.todoapp.dto.ToDoDTO;
import com.example.todoapp.repository.ToDoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoappApplication {

    public static void main(String[] args) { SpringApplication.run(TodoappApplication.class, args);

    }

    @Bean
    public CommandLineRunner init(ToDoRepository toDoRepository) {
        return args -> {
            toDoRepository.save(new ToDoDTO(null, "study", "JAVA", false));
            toDoRepository.save(new ToDoDTO(null, "cook", "ramen", false));
            toDoRepository.save(new ToDoDTO(null, "workout", "running", false));
        };
    }

}

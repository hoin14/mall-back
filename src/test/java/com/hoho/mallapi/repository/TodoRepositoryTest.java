package com.hoho.mallapi.repository;

import com.hoho.mallapi.domain.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1(){
        Assertions.assertNotNull(todoRepository);
        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert(){

        for (int i = 0; i < 100; i++){

            Todo todo = Todo.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .dueDate(LocalDate.of(2024, 1,21))
                    .build();

            Todo result = todoRepository.save(todo);

            log.info(result);
        }

    }

    @Test
    public void testRead(){
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        log.info(todo);
    }

    @Test
    public void testUpdate(){
        Long tno = 1L;
        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        todo.changeTitle("update title");
        todo.changeContent("update content");
        todo.changeComplete(true);

        todoRepository.save(todo);
    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());

        Page<Todo> result =  todoRepository.findAll(pageable);

        log.info(result.getTotalElements());
        log.info(result.getContent());
    }

    /*
    @Test
    public void testSearch1(){

        todoRepository.search1();

    }
    */

}

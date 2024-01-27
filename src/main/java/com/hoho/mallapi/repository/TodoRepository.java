package com.hoho.mallapi.repository;

import com.hoho.mallapi.domain.Todo;
import com.hoho.mallapi.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {

}

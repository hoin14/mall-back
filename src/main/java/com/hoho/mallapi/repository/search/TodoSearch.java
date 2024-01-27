package com.hoho.mallapi.repository.search;

import com.hoho.mallapi.domain.Todo;
import com.hoho.mallapi.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1(PageRequestDTO pageRequestDTO);

}

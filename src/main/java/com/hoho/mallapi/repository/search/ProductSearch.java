package com.hoho.mallapi.repository.search;

import com.hoho.mallapi.dto.PageRequestDTO;
import com.hoho.mallapi.dto.PageResponseDTO;
import com.hoho.mallapi.dto.ProductDTO;


public interface ProductSearch {
    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);

}

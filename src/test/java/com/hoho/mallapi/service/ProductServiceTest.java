package com.hoho.mallapi.service;

import com.hoho.mallapi.dto.PageRequestDTO;
import com.hoho.mallapi.dto.PageResponseDTO;
import com.hoho.mallapi.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@Log4j2
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        log.info((responseDTO.getDtoList()));

    }

    @Test
    public void testRegister(){

        ProductDTO productDTO = ProductDTO.builder()
                .pname("NEW")
                .pdesc("A")
                .price(10000)
                .build();

        productDTO.setUploadFileName(List.of(
                UUID.randomUUID()+"_"+"TEST1.jpg",
                UUID.randomUUID()+"_"+"TEST2.jpg"));

        productService.register(productDTO);
    }
}

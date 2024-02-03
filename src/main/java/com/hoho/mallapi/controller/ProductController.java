package com.hoho.mallapi.controller;

import com.hoho.mallapi.dto.PageRequestDTO;
import com.hoho.mallapi.dto.PageResponseDTO;
import com.hoho.mallapi.dto.ProductDTO;
import com.hoho.mallapi.service.ProductService;
import com.hoho.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    /*
    @PostMapping("/")
    public Map<String, String> register(ProductDTO productDto){
        log.info("register: " + productDto);

        List<MultipartFile> files = productDto.getFiles();

        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDto.setUploadFileName(uploadedFileNames);

        log.info(uploadedFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
    */

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName){
        return fileUtil.getFile(fileName);
    }

    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO){
        return productService.getList(pageRequestDTO);
    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){
        List<MultipartFile> files = productDTO.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadFileName(uploadFileNames);
        log.info(uploadFileNames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno){
        return productService.get(pno);
    }
}

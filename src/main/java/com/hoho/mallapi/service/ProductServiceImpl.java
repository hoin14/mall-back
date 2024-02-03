package com.hoho.mallapi.service;

import com.hoho.mallapi.domain.Product;
import com.hoho.mallapi.domain.ProductImage;
import com.hoho.mallapi.dto.PageRequestDTO;
import com.hoho.mallapi.dto.PageResponseDTO;
import com.hoho.mallapi.dto.ProductDTO;
import com.hoho.mallapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        //Object[] -> 0. product 1 productImage
        List<ProductDTO> dtoList = result.get().map(arr -> {
            ProductDTO productDto = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDto = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDto.setUploadFileName(List.of(imageStr));

            return productDto;

        }).toList();

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);

        log.info("-----------------------------");
        log.info(product);
        log.info(product.getImageList());

        return productRepository.save(product).getPno();
    }
    private Product dtoToEntity(ProductDTO productDTO){

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileName();

        if(uploadFileNames == null || uploadFileNames.isEmpty()){
            return product;
        }

        uploadFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;
    }
    @Override
    public ProductDTO get(Long pno) {
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow();

        return entityToDTO(product);
    }

    private ProductDTO entityToDTO(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()){
            return productDTO;
        }
        List<String> fileNameList = imageList.stream().map(
                ProductImage::getFileName).toList();

        productDTO.setUploadFileName(fileNameList);

        return productDTO;
    }



}

package candidate.test.service.impl;

import candidate.test.dto.product.ProductResponseDto;
import candidate.test.dto.product.RecordRequestDto;
import candidate.test.dto.product.RecordResponseDto;
import candidate.test.mapper.ProductMapper;
import candidate.test.model.Product;
import candidate.test.repository.ProductRepository;
import candidate.test.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public void add(RecordRequestDto requestDto) {
        List<Product> productList = requestDto.records().stream()
                .map(productMapper::toModel)
                .toList();
        productRepository.saveAll(productList);
    }

    @Override
    public RecordResponseDto getAll() {
        List<ProductResponseDto> productList = productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
        return new RecordResponseDto(productList);
    }
}

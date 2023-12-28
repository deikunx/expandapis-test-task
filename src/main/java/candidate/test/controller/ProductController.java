package candidate.test.controller;

import candidate.test.dto.product.RecordRequestDto;
import candidate.test.dto.product.RecordResponseDto;
import candidate.test.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void add(@Valid @RequestBody RecordRequestDto request) {
        productService.add(request);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public RecordResponseDto getAll() {
        return productService.getAll();
    }
}

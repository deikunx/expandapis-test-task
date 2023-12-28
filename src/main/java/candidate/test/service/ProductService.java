package candidate.test.service;

import candidate.test.dto.product.RecordRequestDto;
import candidate.test.dto.product.RecordResponseDto;

public interface ProductService {
    void add(RecordRequestDto requestDto);

    RecordResponseDto getAll();
}

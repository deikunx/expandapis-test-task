package candidate.test.dto.product;

import jakarta.validation.Valid;
import java.util.List;

public record RecordRequestDto(@Valid List<ProductRequestDto> records) {
}

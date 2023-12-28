package candidate.test.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate entryDate;

    private String itemCode;

    private String itemName;

    private int itemQuantity;

    private String status;
}

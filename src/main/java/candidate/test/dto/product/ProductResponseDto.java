package candidate.test.dto.product;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;
    private LocalDate entryDate;
    private String itemCode;
    private String itemName;
    private int itemQuantity;
    private String status;
}

package candidate.test.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductRequestDto {

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate entryDate;

    @NotBlank
    private String itemCode;

    @NotBlank
    private String itemName;

    @NotNull
    @Min(value = 1, message = "Item quantity should be bigger than 0")
    private int itemQuantity;

    @NotBlank
    private String status;
}

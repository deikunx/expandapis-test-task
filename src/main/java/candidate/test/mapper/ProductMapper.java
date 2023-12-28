package candidate.test.mapper;

import candidate.test.config.MapperConfig;
import candidate.test.dto.product.ProductRequestDto;
import candidate.test.dto.product.ProductResponseDto;
import candidate.test.model.Product;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ProductMapper {
    Product toModel(ProductRequestDto requestDto);

    ProductResponseDto toResponseDto(Product product);
}

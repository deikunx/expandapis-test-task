package candidate.test.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import candidate.test.dto.product.ProductRequestDto;
import candidate.test.dto.product.RecordRequestDto;
import candidate.test.dto.product.RecordResponseDto;
import candidate.test.model.Product;
import candidate.test.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Add a list of products")
    @WithMockUser(username = "admin")
    @Sql(scripts = "classpath:db/product/remove-all-products.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addProducts_TwoValidProducts_ShouldSaveProductsToDb() throws Exception {
        ProductRequestDto product1 = new ProductRequestDto()
                .setEntryDate(LocalDate.of(2023, 12, 28))
                .setItemCode("09382")
                .setItemName("iPhone 11")
                .setItemQuantity(20)
                .setStatus("PAID");

        ProductRequestDto product2 = new ProductRequestDto()
                .setEntryDate(LocalDate.of(2023, 12, 28))
                .setItemCode("09385")
                .setItemName("iPhone 13")
                .setItemQuantity(10)
                .setStatus("PAID");

        List<ProductRequestDto> records = List.of(product1, product2);
        RecordRequestDto requestDto = new RecordRequestDto(records);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/products/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Product> productList = productRepository.findAll();

        Assertions.assertFalse(productList.isEmpty());
        Assertions.assertEquals(productList.size(), 2);
    }

    @Test
    @DisplayName("Add a product with invalid quantity should return 400 status")
    @WithMockUser(username = "admin")
    void addProduct_WithNegativeQuantity_ShouldReturn400Status() throws Exception {
        int invalidQuantity = -20;

        ProductRequestDto product1 = createProductRequestDto();
        product1.setItemQuantity(invalidQuantity);

        List<ProductRequestDto> records = List.of(product1);
        RecordRequestDto requestDto = new RecordRequestDto(records);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        mockMvc.perform(post("/products/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add a product by unauthorized user should return 401 status")
    void addProduct_ByUnauthorizedUser_ShouldReturn401Status() throws Exception {
        ProductRequestDto product1 = createProductRequestDto();
        List<ProductRequestDto> records = List.of(product1);
        RecordRequestDto requestDto = new RecordRequestDto(records);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/products/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Get all products with 2 products in table should return list of 2 products")
    @Sql(scripts = "classpath:db/product/add-two-products-to-db.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/product/remove-all-products.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllProducts_WithTwoExistProductsInDb_ShouldReturnListOfTwoProducts() throws Exception {
        int expectedSize = 2;

        MvcResult result = mockMvc.perform(get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RecordResponseDto actual = objectMapper
                    .readValue(result.getResponse().getContentAsString(), RecordResponseDto.class);

        Assertions.assertEquals(actual.records().size(), expectedSize);
    }

    @Test
    @DisplayName("Get all products with empty products table should return empty list")
    @WithMockUser(username = "admin")
    void getAllProducts_WithEmptyProductTable_ShouldReturnLEmptyList() throws Exception {
        int expectedSize = 0;

        MvcResult result = mockMvc.perform(get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RecordResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), RecordResponseDto.class);

        Assertions.assertEquals(actual.records().size(), expectedSize);
    }

    @Test
    @DisplayName("Get all products with empty products table should return empty list")
    void getAllProducts_ByUnauthorizedUsed_ShouldReturn401Status() throws Exception {
        mockMvc.perform(get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private ProductRequestDto createProductRequestDto() {
        return new ProductRequestDto()
                .setEntryDate(LocalDate.of(2023, 12, 28))
                .setItemCode("09382")
                .setItemName("iPhone 11")
                .setItemQuantity(1)
                .setStatus("PAID");
    }
}

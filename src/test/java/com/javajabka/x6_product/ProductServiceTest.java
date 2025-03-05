package com.javajabka.x6_product;

import com.javajabka.x6_product.exception.BadRequestException;
import com.javajabka.x6_product.model.ProductRequest;
import com.javajabka.x6_product.model.ProductResponse;
import com.javajabka.x6_product.repository.ProductRepository;
import com.javajabka.x6_product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void createProduct_valid() {
        ProductRequest productRequest = buildProductRequest("Product", BigDecimal.valueOf(12.5));
        ProductResponse productResponse = buildProductResponse(10L, productRequest);
        Mockito.when(productRepository.insert(productRequest)).thenReturn(productResponse);
        ProductResponse foundResponse = productService.createProduct(productRequest);
        Assertions.assertEquals(productResponse, foundResponse);
        Mockito.verify(productRepository).insert(productRequest);
    }

    @Test
    public void shouldReturnError_whenCreateWithEmptyName() {
        ProductRequest productRequest = buildProductRequest("", BigDecimal.valueOf(12.5));
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> productService.createProduct(productRequest)
        );
        Assertions.assertEquals("Введите имя продукта", exception.getMessage());
    }

    @Test
    public void updateProduct_valid() {
        ProductRequest productRequest = buildProductRequest("Product", BigDecimal.valueOf(12.5));
        ProductResponse productResponse = buildProductResponse(10L, productRequest);
        Mockito.when(productRepository.update(1L, productRequest)).thenReturn(productResponse);
        ProductResponse foundResponse = productService.updateProduct(1L, productRequest);
        Assertions.assertEquals(productResponse, foundResponse);
        Mockito.verify(productRepository).update(1L, productRequest);
    }

    @Test
    public void shouldReturnError_whenUpdateWithEmptyName() {
        ProductRequest productRequest = buildProductRequest("", BigDecimal.valueOf(12.5));
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> productService.createProduct(productRequest)
        );
        Assertions.assertEquals("Введите имя продукта", exception.getMessage());
    }

    @Test
    public void shouldReturnError_whenProductIdNotFound() {
        ProductRequest productRequest = buildProductRequest("Product", BigDecimal.valueOf(12.5));
        Mockito.when(productRepository.update(100L, productRequest)).thenThrow(new BadRequestException("Не удалось найти пользователя с id = 100"));
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class, () -> productService.updateProduct(100L, productRequest)
        );
        Assertions.assertEquals("Не удалось найти пользователя с id = 100", exception.getMessage());
    }

    @Test
    public void existsProduct_valid() {
        List<Long> listProductId = List.of(1L, 2L);
        Mockito.when(productRepository.exist(listProductId)).thenReturn(listProductId);
        List<Long> foundListProductId = productService.exists(List.of(1L, 2L));
        Assertions.assertEquals(listProductId, foundListProductId);
        Mockito.verify(productRepository).exist(listProductId);
    }

    @Test
    public void getProductById_valid() {
        ProductRequest productRequest = buildProductRequest("Product", BigDecimal.valueOf(12.5));
        ProductResponse productResponse = buildProductResponse(1L, productRequest);
        Mockito.when(productRepository.getProductById(100L)).thenReturn(productResponse);
        ProductResponse foundProduct = productService.getProductById(100L);
        Assertions.assertEquals(productResponse, foundProduct);
        Mockito.verify(productRepository).getProductById(100L);
    }

    private ProductResponse buildProductResponse(final Long id, final ProductRequest productRequest) {
        return ProductResponse.builder()
                .id(id)
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
    }

    private ProductRequest buildProductRequest(final String name, final BigDecimal price) {
        return ProductRequest.builder()
                .name(name)
                .price(price)
                .build();
    }

}
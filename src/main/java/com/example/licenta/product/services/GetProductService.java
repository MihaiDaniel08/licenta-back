package com.example.licenta.product.services;

import com.example.licenta.Query;
import com.example.licenta.product.ProductRepository;
import com.example.licenta.product.model.Product;
import com.example.licenta.product.model.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetProductService implements Query<Void,List<Product>> {

    private final ProductRepository productRepository ;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<List<Product>> execute(Void input) {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream().map(ProductDTO::new).toList();

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}

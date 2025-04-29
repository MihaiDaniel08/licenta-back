package com.example.licenta.product.services;

import com.example.licenta.Query;
import com.example.licenta.product.ProductRepository;
import com.example.licenta.product.model.Product;
import com.example.licenta.product.model.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetProductService implements Query<Integer, ProductDTO> {

    private final ProductRepository productRepository;

    public GetProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public ResponseEntity<ProductDTO> execute(Integer input) {
        //optional accounts for when the db returns null
          Optional<Product> productOptional = productRepository.findById(input);
          if(productOptional.isPresent()){
               return ResponseEntity.ok(new ProductDTO(productOptional.get()));
          }

          //here would fit an exception when we cannot find the product => Product Nout Found Exception
        return null;
    }
}

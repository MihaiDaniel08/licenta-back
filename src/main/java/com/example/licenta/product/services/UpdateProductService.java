package com.example.licenta.product.services;

import com.example.licenta.Command;
import com.example.licenta.product.ProductRepository;
import com.example.licenta.product.model.Product;
import com.example.licenta.product.model.ProductDTO;
import com.example.licenta.product.model.UpdateProductCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProductService implements Command<UpdateProductCommand, ProductDTO> {

    private ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<ProductDTO> execute(UpdateProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(command.getId());
        if(optionalProduct.isPresent()){
             Product product = command.getProduct();
             product.setId(command.getId());
             productRepository.save(product); //save can create OR update an entity if it's already existing
             return ResponseEntity.ok(new ProductDTO(product));
        }

        return null;
    }
}

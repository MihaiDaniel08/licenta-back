package com.example.licenta.product;

import com.example.licenta.product.model.Product;
import com.example.licenta.product.model.ProductDTO;
import com.example.licenta.product.services.CreateProductService;
import com.example.licenta.product.services.DeleteProductService;
import com.example.licenta.product.services.GetProductService;
import com.example.licenta.product.services.UpdateProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final CreateProductService createProductService;
    private final GetProductService getProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;


    public ProductController(CreateProductService createProductService,
                             GetProductService getProductService,
                             UpdateProductService updateProductService,
                             DeleteProductService deleteProductService) {
        this.createProductService = createProductService;
        this.getProductService = getProductService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product){
        return createProductService.execute(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(){
        return getProductService.execute(null);
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(){
        return updateProductService.execute(null);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProduct(){
        return deleteProductService.execute(null);
    }

}

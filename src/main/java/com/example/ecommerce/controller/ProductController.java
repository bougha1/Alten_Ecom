package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {



    @Autowired
    private ProductRepository productRepository;

    @Value("${product.images.directory}") // Emplacement où enregistrer les images
    private String imagesDirectory;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProductImage(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarder l'image dans le dossier spécifié
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(imagesDirectory, fileName);
            Files.write(filePath, file.getBytes());

            // Retourner le chemin de l'image
            String imageUrl = "/images/" + fileName;
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du téléchargement.");
        }
    }


    @GetMapping("/products/all")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @CrossOrigin("*")
    @GetMapping("/products")
    public Page<Product> getProducts(
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByCategory(category, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @CrossOrigin("*")
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return productRepository.save(product);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("image") MultipartFile imageFile) {

        try {
            // Save the file locally or upload to cloud storage (e.g., S3, Azure)
            String uploadDir = "uploads/";
            String imagePath = uploadDir + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(imagePath);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());

            // Save product details to the database
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);
            product.setQuantity(quantity);
            product.setImage(imagePath); // Save file path
            Product savedProduct = productRepository.save(product);

            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setCategory(productDetails.getCategory());
            product.setImage(productDetails.getImage());
            product.setInventoryStatus(productDetails.getInventoryStatus());
            product.setRating(productDetails.getRating());
            return ResponseEntity.ok(productRepository.save(product));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
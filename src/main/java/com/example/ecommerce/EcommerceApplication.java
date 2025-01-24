package com.example.ecommerce;

import com.example.ecommerce.enums.ProductCategory;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication(scanBasePackages = "com.example.ecommerce")
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

   //@Bean
    CommandLineRunner start( UserRepository userRepository,
                             ProductRepository productRepository) {

        return args -> {
            for (int i = 0; i < 10; i++) {
                User user = new User();
                user.setPassword(UUID.randomUUID().toString());
                user.setUsername("Admin" + i + new Random().nextInt(10));
                user.setEmail(user.getUsername() + "@gmail.com");
                userRepository.save(user);
                for (int j = 0; j < 10; j++) {
                    Product product = new Product();
                    product.setPrice(new Random().nextDouble() * 100);
                    product.setCategory("Admin" + i + new Random().nextInt(10));
                    product.setCategory(ProductCategory.getProductCategory(j + 1).toString());
                    product.setQuantity(new Random().nextInt(100));
                    product.setCode(UUID.randomUUID().toString());
                    product.setInternalReference(UUID.randomUUID().toString());
                    product.setImage("IMG" + UUID.randomUUID());
                    product.setDescription(ProductCategory.getProductCategory(j + 1).getDescription());
                    product.setRating((double) (j / 2));
                    productRepository.save(product);
                }
            }
        };
    }
}
package com.mycompany.myapp.feign;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycompany.myapp.domain.Category;

@FeignClient(name = "restheart-category", url = "http://localhost:8080", decode404 = true)
public interface CategoryClient {

    // Method to save a new category (POST request)
    @PostMapping("/pro6/category")
    public ResponseEntity<Void> save(@RequestBody Category category) throws URISyntaxException;

    // Method to update an existing category (PUT request)
    @PutMapping("/pro6/category/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Category category) throws URISyntaxException;

    // Method to get a category by id (GET request)
    @GetMapping("/pro6/category/{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") String id);

    // Method to get all categories (GET request)
    @GetMapping("/pro6/category")
    public ResponseEntity<List<Category>> findAll();

    // Method to delete a category by id (DELETE request)
    @DeleteMapping("/pro6/category/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id);

   // Method to partially update an existing post (PATCH request)
   @PatchMapping("/pro6/category/{id}")
   public ResponseEntity<Void> patchUpdate(@PathVariable("id") String id, @RequestBody Category category) throws URISyntaxException;
}

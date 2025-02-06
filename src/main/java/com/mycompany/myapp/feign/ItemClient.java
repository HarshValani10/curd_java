package com.mycompany.myapp.feign;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mycompany.myapp.domain.Item;

// import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "restheart-add-item", url = "http://localhost:8080", decode404 = true)
public interface ItemClient {

    // Method to save a new comment (POST request)
    @PostMapping("/pro6/item")
    public ResponseEntity<Void> save(@RequestBody Item item) throws URISyntaxException;

    // Method to update an existing comment (PUT request)
    @PutMapping("/pro6/item/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Item item) throws URISyntaxException;

    // Method to get a comment by id (GET request)
    @GetMapping("/pro6/item/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") String id);

    // Method to get all comments (GET request)
    @GetMapping("/pro6/item")
    public ResponseEntity<List<Item>> findAll();

    // Method to delete a comment by id (DELETE request)
    @DeleteMapping("/pro6/item/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id);

     // Method to partially update an existing post (PATCH request)
   @PatchMapping("/pro6/item/{itemid}")
   public ResponseEntity<Void> patchUpdate(@PathVariable("itemid") String itemid, @RequestBody Item item) throws URISyntaxException;
}

package com.mycompany.myapp.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.domain.CreateInfo;
import com.mycompany.myapp.domain.RefType;
import com.mycompany.myapp.domain.UpdateInfo;
import com.mycompany.myapp.domain.RefType.RefTo;
import com.mycompany.myapp.feign.CategoryClient;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryClient categoryClient;
    private final UserRepository userRepository;

    public CategoryController(CategoryClient categoryClient, UserRepository userRepository) {
        this.categoryClient = categoryClient;
        this.userRepository = userRepository;
    }

    // Create a new category
    @PostMapping("/pro6/category")
    public ResponseEntity<?> createCategory(@RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);

        category.setItem(new ArrayList<>());
        // Ensure the ID is null before saving
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", "Category", "idexists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        category.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        category.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());

        // Call Feign client to save the category
        return categoryClient.save(category);
    }

    // Update an existing category
    @PutMapping("/pro6/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") String id, @RequestBody Category category)
            throws URISyntaxException {
        log.debug("REST request to update Category : {}", category);
        return categoryClient.update(id, category);
    }

    // Get a category by ID
    @GetMapping("/pro6/category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") String id) {
        log.debug("REST request to get Category : {}", id);
        return categoryClient.getById(id);
    }

    // Get all categories
    @GetMapping("/pro6/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categoryClient.findAll();
    }

    // Delete a category by ID
    @DeleteMapping("/pro6/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") String id) {
        log.debug("REST request to delete Category : {}", id);
        return categoryClient.delete(id);
    }

    @PatchMapping("/pro6/category/{id}")
    public ResponseEntity<Void> patchUpdatePost(@PathVariable String id, @RequestBody Category category) throws URISyntaxException {
        // Forward the patch update request to Feign client
        return categoryClient.patchUpdate(id, category);
    }
}

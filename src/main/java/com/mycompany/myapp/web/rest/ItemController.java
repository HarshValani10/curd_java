package com.mycompany.myapp.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.domain.CreateInfo;
import com.mycompany.myapp.domain.Item;
import com.mycompany.myapp.domain.RefType;
import com.mycompany.myapp.domain.UpdateInfo;
import com.mycompany.myapp.domain.RefType.RefTo;
import com.mycompany.myapp.feign.CategoryClient;
import com.mycompany.myapp.feign.ItemClient;
import com.mycompany.myapp.repository.ItemRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemClient itemClient;
    private final UserRepository userRepository;
    private final CategoryClient categoryClient;
    @Autowired
    private  ItemRepository itemRepository;


    public ItemController(ItemClient itemClient, UserRepository userRepository, CategoryClient categoryClient) {
        this.userRepository = userRepository;
        this.itemClient = itemClient;
        this.categoryClient = categoryClient;
    }

    // Create a new item
    @PostMapping("/pro6/item")
    public ResponseEntity<?> createItem(@RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to save Item : {}", item);

        // Ensure the ID is null before saving
        if (item.getId() != null) {
            throw new BadRequestAlertException("A new item cannot already have an ID", "Item", "idexists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        item.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        item.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());

        // Call Feign client to save the item
        return itemClient.save(item);
    }

    // Update an existing item
    @PutMapping("/pro6/item/{id}")
    public ResponseEntity<?> updateItem(@PathVariable("id") String id, @RequestBody Item item)
            throws URISyntaxException {
        log.debug("REST request to update Item : {}", item);
        return itemClient.update(id, item);
    }

    // Get an item by ID
    @GetMapping("/pro6/item/{id}")
    public ResponseEntity<Item> getItem(@PathVariable("id") String id) {
        log.debug("REST request to get Item : {}", id);
        return itemClient.getById(id);
    }

    // Get all items
    @GetMapping("/pro6/item")
    public ResponseEntity<List<Item>> getAllItems() {
        log.debug("REST request to get all Items");
        return itemClient.findAll();
    }

    // Delete an item by ID
    @DeleteMapping("/pro6/item/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") String id) {
        log.debug("REST request to delete Item : {}", id);
        return itemClient.delete(id);
    }

    // Add an item to a category
    @PostMapping("/pro6/category/{categoryId}/item")
    public ResponseEntity<?> addItemToCategory(@PathVariable("categoryId") String categoryId, @RequestBody Item item)
            throws URISyntaxException {
        log.debug("REST request to add Item : {} to Category : {}", item, categoryId);

        item.setId(new ObjectId().toHexString());
        item.setCategory(new RefType(new ObjectId(categoryId).toHexString(), RefTo.Category));

        Category cat = categoryClient.getById(categoryId).getBody();
        cat.getItem().add(new RefType(new ObjectId(item.getId()).toHexString(), RefTo.Item));
        categoryClient.update(categoryId, cat);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        item.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        item.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());

        return itemClient.save(item);
    }
    @GetMapping("/pro6/category/{categoryId}/item")
    public ResponseEntity<?> getallcommnetbyId(@PathVariable("categoryId") String categoryId){
        List<Item> allCommentsByPostId = itemRepository.findAllItemByCategoryId(new ObjectId(categoryId));
        return ResponseEntity.ok().body(allCommentsByPostId);
    }

    @PatchMapping("/pro6/item/{itemid}")
    public ResponseEntity<Void> patchUpdatePost(@PathVariable String itemid, @RequestBody Item item) throws URISyntaxException {

        // Forward the patch update request to Feign client

        
        return itemClient.patchUpdate(itemid, item);
    }
}

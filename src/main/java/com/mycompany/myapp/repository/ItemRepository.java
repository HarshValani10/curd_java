package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Item;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Item entity.
 */
@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    List<Item> findAllItemByCategoryId(ObjectId objectId);}

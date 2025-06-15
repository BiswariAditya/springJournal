package com.biswariAdi.FirstSpringProject.repository;

import com.biswariAdi.FirstSpringProject.Entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepo extends MongoRepository<Users, ObjectId> {

    Users findByUserName(String userName);
}

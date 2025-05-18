package com.proj.bootiestrappi.Database.repo

import com.proj.bootiestrappi.Database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface userRepo : MongoRepository<User,ObjectId> {
    fun findUserByEmail(email:String):User?
}
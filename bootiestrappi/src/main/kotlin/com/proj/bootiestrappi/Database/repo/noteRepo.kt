package com.proj.bootiestrappi.Database.repo

import com.proj.bootiestrappi.Database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface noteRepo:MongoRepository<Note,ObjectId> {
    fun findByOwnerId(ownerId: ObjectId):List<Note?>
}
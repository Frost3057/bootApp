package com.proj.bootiestrappi.Database.repo

import com.proj.bootiestrappi.Database.model.refreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface refreshtokenRepo:MongoRepository<refreshToken,ObjectId> {

    fun findByUserIdAndHashedToken(userId:ObjectId,hashedToken: String):refreshToken?
    fun deleteTokenByUserIdAndHashedToken(userId:ObjectId,hashedToken: String)
}
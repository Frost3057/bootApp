package com.proj.bootiestrappi.Database.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant

@Document("refreshTokens")
data class refreshToken (
    val userId:ObjectId,
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant,
    val hashedToken:String,
    val createdAt:Instant = Instant.now()
)
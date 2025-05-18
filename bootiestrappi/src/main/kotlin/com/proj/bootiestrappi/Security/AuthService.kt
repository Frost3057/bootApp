package com.proj.bootiestrappi.Security

import com.proj.bootiestrappi.Database.model.User
import com.proj.bootiestrappi.Database.model.refreshToken
import com.proj.bootiestrappi.Database.repo.refreshtokenRepo
import com.proj.bootiestrappi.Database.repo.userRepo
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.*
import org.apache.tomcat.util.net.openssl.ciphers.MessageDigest as MessageDigest1

@Service
class AuthService(
    private val jwtService: jwtService,
    private val userRepo: userRepo,
    private val hashEncoder: hashEncoder,
    private val refreshtokenRepo: refreshtokenRepo
) {
    data class TokenPair(
        val accessToken:String,
        val refreshToken:String
    )
    fun register(email:String,password:String):User{
        return userRepo.save(
            User(
            email = email,
                password = hashEncoder.encode(password)
        )
        )
    }

    fun login(email:String,password:String):TokenPair{
        val user = userRepo.findUserByEmail(email)?:throw BadCredentialsException("Wrong Credentials!")
        if(!hashEncoder.matches(password,user.password)){
            throw BadCredentialsException("Wrong Credentials!")
        }
        val accessToken = jwtService.generateAccessToken(user.id.toHexString())
        val refreshToken = jwtService.generateRefreshToken(user.id.toHexString())
        addRefreshToken(refreshToken,user.id)
        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
    private fun addRefreshToken(rawToken:String,userId:ObjectId){
        val hashed =hashToken(rawToken)
        val expiry = jwtService.refreshTokenDuration
        val expiresAt = Instant.now().plusMillis(expiry)
        refreshtokenRepo.save(refreshToken(
            expiresAt = expiresAt,
            hashedToken = hashed,
            userId = userId
        ))
    }

    private fun hashToken(token:String):String{
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(bytes)
    }
    @Transactional
    fun refreshToken(refreshToken:String):TokenPair{
        if(!jwtService.validateRefreshToken(refreshToken)){
            throw IllegalArgumentException("Invalid refresh token")
        }
        val userId = jwtService.getOwnerId(refreshToken)
        val user = userRepo.findById(ObjectId(userId)).orElseThrow { IllegalArgumentException("Invalid Refresh Token") }
        val hashedToken = hashToken(refreshToken)
        refreshtokenRepo.findByUserIdAndHashedToken(user.id,hashedToken) ?: throw IllegalArgumentException("RefreshToken not found(It might have expired")
        refreshtokenRepo.deleteTokenByUserIdAndHashedToken(user.id,hashedToken)
        val token = jwtService.generateRefreshToken(userId)
        val atoken = jwtService.generateAccessToken(userId)
        return TokenPair(
            accessToken = atoken,
            refreshToken = token
        )
    }
}
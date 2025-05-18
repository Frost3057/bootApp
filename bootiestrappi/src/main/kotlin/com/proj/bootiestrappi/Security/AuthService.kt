package com.proj.bootiestrappi.Security

import com.proj.bootiestrappi.Database.model.User
import com.proj.bootiestrappi.Database.repo.userRepo
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: jwtService,
    private val userRepo: userRepo,
    private val hashEncoder: hashEncoder
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
        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
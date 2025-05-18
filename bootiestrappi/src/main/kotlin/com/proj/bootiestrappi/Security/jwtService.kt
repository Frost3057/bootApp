package com.proj.bootiestrappi.Security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class jwtService (
){
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode("YWRtaW5pc3RyYXRvcg"))
    private val accessTokenDuration = 15L*60*1000L
    private val refreshTokenDuration = 30L*24*60*60*1000L
    private fun generateToken(
         userId:String,
         type:String,
         liveTime:Long
    ):String{
        val now = Date()
        val expiryDate = Date(now.time+liveTime)
        return Jwts.builder()
            .subject(userId)
            .claim("type",type)
            .issuedAt(now)
            .expiration(expiryDate).signWith(secretKey,Jwts.SIG.HS256).compact()
    }

    fun generateAccessToken(userId: String):String{
        return generateToken(userId,"accessToken",accessTokenDuration)
    }
    fun generateRefreshToken(userId: String):String{
        return generateToken(userId,"refreshToken",refreshTokenDuration)
    }
    fun validateAccessToken(token:String):Boolean{
        val claim = parseAllclaims(token)?: return false
        val tokenType = claim.subject as? String ?: return false
        return tokenType == "accessToken"
    }

    fun validateRefreshToken(token:String):Boolean{
        val claim = parseAllclaims(token)?: return false
        val tokenType = claim.subject as? String ?: return false
        return tokenType == "refreshToken"
    }

    fun getObjectId(token:String):String{
        val raw = if(token.startsWith("Bearer ")){
            token.removePrefix("Bearer ")
        }else{
            token
        }
        val claim = parseAllclaims(raw) ?: throw IllegalArgumentException("Invalid Token")
        return claim.subject
    }

    private fun parseAllclaims(token:String): Claims?{
        return try{
             Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload
        }catch (e:Exception){
             null
        }
    }
}
package com.proj.bootiestrappi.Security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class hashEncoder {
    private val bcrypt = BCryptPasswordEncoder();

    fun encode(password:String):String = bcrypt.encode(password)
    fun matches(password:String,hasedPass : String):Boolean = bcrypt.matches(password,hasedPass)

}
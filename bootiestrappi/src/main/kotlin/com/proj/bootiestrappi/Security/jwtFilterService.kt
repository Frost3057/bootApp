package com.proj.bootiestrappi.Security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
@Component
class jwtFilterService(
    private val jwtService: jwtService
) :OncePerRequestFilter(){
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authentication")
        if(authHeader.startsWith("Bearer ") &&  authHeader !=null){
            if(jwtService.validateAccessToken(authHeader)){
                val userId = jwtService.getOwnerId(authHeader)
                val auth = UsernamePasswordAuthenticationToken(userId,authHeader)
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        filterChain.doFilter(request,response)
    }
}
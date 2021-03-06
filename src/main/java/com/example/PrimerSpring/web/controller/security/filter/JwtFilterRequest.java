package com.example.PrimerSpring.web.controller.security.filter;

import com.example.PrimerSpring.domain.service.UserService;
import com.example.PrimerSpring.web.controller.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Fitro de peticiones JWT
@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader=request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
             String jwt = authorizationHeader.substring(7);
             String username=jwtUtil.extractUsername(jwt);

             if (username != null && SecurityContextHolder.getContext().getAuthentication() ==null){
                 UserDetails userDetails = userService.loadUserByUsername(username);

                 if (jwtUtil.validateToken(jwt,userDetails)){
                     UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                 }

             }


        }
        filterChain.doFilter(request,response);
    }
}

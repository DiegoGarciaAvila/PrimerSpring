package com.example.PrimerSpring.web.controller.security;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    private static final String KEY="Diego";

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,KEY).compact();
    }
    //Comprabar si el usuario de la peticion es el mismo del token y que el token no este expirado
    public boolean validateToken(String token,UserDetails userDetails){
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    //Extrae el usuario
    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }

    //Comprueba que el token no este expirado
    public boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

    //Verifica que la firma se correcta o exista, y obtiene el token
    public Claims getClaims (String token){
        return  Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}

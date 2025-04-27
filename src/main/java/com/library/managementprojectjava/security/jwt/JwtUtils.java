package com.library.managementprojectjava.security.jwt;


import com.library.managementprojectjava.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;


@Component
public class JwtUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);


  @Value("${backendapi.app.jwtExpirationMs}")
  private long jwtExpirations;

  @Value("${backendapi.app.jwtSecret}")
  private String jwtSecret;

  public String generateToken(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return buildTokenFromUserDetails(userDetails);

  }



  private String buildTokenFromUserDetails(UserDetailsImpl userDetails) {
    return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim("roles", userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + jwtExpirations))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }
  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      LOGGER.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;

  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }


}

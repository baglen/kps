package com.kps.backend.security.util;

import com.kps.backend.employee.repository.EmployeeRepository;
import com.kps.backend.security.model.EmployeeModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;


@Component
public class JwtUtil {

    private final EmployeeRepository employeeRepository;

    public static final long JWT_TOKEN_VALIDITY = 2 * 7 * 24;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public JwtUtil(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Claims> parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(body);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String generateToken(EmployeeModel employee) {
        Claims claims = Jwts.claims().setSubject(employee.getEmployeeRecord().getLogin());
        claims.put("userId", employee.getEmployeeRecord().getId());
        claims.put("companyId", employee.getEmployeeRecord().getCompanyId());
        LocalDateTime now = LocalDateTime.now();
        Date expirationDate = Date.from(
                now.plus(JWT_TOKEN_VALIDITY, ChronoUnit.HOURS)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public boolean validateToken(Claims claims, EmployeeModel userDetails) {
        if(employeeRepository.getEmployeeToken(claims.get("userId", Long.class)).getToken() != null){
            return claims.getSubject().equals(userDetails.getEmployeeRecord().getLogin());
        } else {
            return false;
        }
    }

    public boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}

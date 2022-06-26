package com.kps.backend.security.filter;

import com.example.jooq.models.tables.records.EmployeeTokenRecord;
import com.kps.backend.employee.repository.EmployeeRepository;
import com.kps.backend.security.model.EmployeeModel;
import com.kps.backend.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public JwtAuthenticationFilter(@Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
                                   JwtUtil jwtUtil, EmployeeRepository employeeRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String uri = request.getRequestURI();
        final String requestTokenHeader = request.getHeader("Authorization");
        Claims claims = null;
        String jwtToken;
        if (requestTokenHeader != null) {
            if(requestTokenHeader.startsWith("Bearer ")){
                jwtToken = requestTokenHeader.substring(7);
            } else {
                jwtToken = requestTokenHeader;
            }
            Optional<Claims> claimsOptional = jwtUtil.parseToken(jwtToken);
            if (claimsOptional.isPresent()) {
                claims = claimsOptional.get();
            }
        }
        if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            EmployeeTokenRecord userToken = employeeRepository.getEmployeeToken(claims.get("userId", Long.class));
            if(userToken != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(claims.getSubject());
                if (jwtUtil.validateToken(claims, (EmployeeModel) userDetails)) {
                    if (jwtUtil.isTokenExpired(claims)) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                        return;
                    }
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorize first");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}

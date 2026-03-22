package com.example.APIGateway.security.filter;

import com.example.APIGateway.exception.InvalidTokenException;
import com.example.APIGateway.security.model.AuthUser;
import com.example.APIGateway.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtFilter implements WebFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (header != null && header.startsWith("Bearer")) {
            String token = header.substring(7);

            try {
                validateAccessToken(token);
                Long userId = jwtService.extractUserId(token);
                String role = "ROLE_" + jwtService.extractRole(token);

                AuthUser authUser = new AuthUser(userId, role);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                authUser,
                                null,
                                Collections.singleton(new SimpleGrantedAuthority(role))
                        );

                SecurityContext context = new SecurityContextImpl(auth);

                return chain.filter(exchange).contextWrite(
                        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));

            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }

    private void validateAccessToken(String token) {
        if (!jwtService.validateToken(token) ||
                !"access".equals(jwtService.extractTokenType(token))) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}

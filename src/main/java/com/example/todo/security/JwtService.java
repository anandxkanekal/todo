package com.example.todo.security;

import com.example.todo.config.JwtConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    private final JwtConfig jwtConfig;

    public JwtService(JwtEncoder jwtEncoder, JwtConfig jwtConfig) {
        this.jwtEncoder = jwtEncoder;
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserDetails username) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("todo-app")
                .issuedAt(now)
                .expiresAt(now.plusMillis(jwtConfig.getExpiration()))
                .subject(username.getUsername())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}

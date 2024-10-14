package kg.mir.Mirproject.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kg.mir.Mirproject.entities.User;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Component
public class JwtService {
    private final UserRepository userRepository;

    @Value("${spring.jwt.secret_key}")
    private String secretKey;

    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        return JWT.create()
                .withClaim("email", user.getEmail())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(ZonedDateTime.now().toInstant())
                .withExpiresAt(ZonedDateTime.now().plusMonths(1).toInstant())
                .sign(algorithm);
    }

    public String verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJwt = jwtVerifier.verify(token);
        return decodedJwt.getClaim("email").asString();
    }

    public User getAuthenticationUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.getUserByEmail(email).orElseThrow(() ->
                new NotFoundException("Пользователь с почтой %s не найден!".formatted(email)));
    }
}
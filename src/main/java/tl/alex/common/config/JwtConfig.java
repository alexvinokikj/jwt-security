package tl.alex.common.config;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tl.alex.security.JwtCertificate;

@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public JwtCertificate getCertificate() {
        JwtCertificate certificate = new JwtCertificate();
        certificate.setAlgorithm(SignatureAlgorithm.HS256);
        certificate.setSecret(jwtSecret);
        return certificate;
    }
}

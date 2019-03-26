package tl.alex.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    http://svlada.com/jwt-token-authentication-with-spring-boot/
    https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
    http://www.baeldung.com/spring-security-oauth-jwt

*/
@Service
@Slf4j
public class JwtService {
    public static final String BEARER = "Bearer ";
    private static final String USER_NAME = "userName";
    private static final String USER_ID = "id";
    private static final String PERMISSIONS = "permissions";

    private static final long EXPIRATION_TIME = 36000000;

    @Autowired
    JwtCertificate jwtCertificate;

    public String createToken(JwtUser jwtUser) {
               try {
            Claims claims = Jwts.claims().setSubject("service");
            claims.put(USER_NAME, jwtUser.getUserName());
            claims.put(USER_ID, jwtUser.getUserId());
            claims.put(PERMISSIONS,jwtUser.getPermissions());

            if (jwtCertificate.getAlgorithm() == null) {
                String errorMessage = "JWT certificate algorithm null";
                log.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            String token =
                    Jwts.builder()
                            .setClaims(claims)
                            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                            .signWith(jwtCertificate.getAlgorithm(), jwtCertificate.getSecret())
                            .compact();
            token = BEARER + token;
            log.debug("Token created");
            return token;
        } catch (Exception exception) {
            log.error("Error parsing token", exception);
            throw exception;
        }
    }

    public JwtUser getUser(String token) {
        JwtUser jwtUser = new JwtUser();
        Claims claims = getClaims(token);

        Optional.ofNullable(claims.get(USER_ID))
                .ifPresent(x -> jwtUser.setUserId(Long.valueOf(x.toString())));

        Optional.ofNullable(claims.get(USER_NAME))
                .ifPresent(x -> jwtUser.setUserName(x.toString()));

        Optional.ofNullable(claims.get(PERMISSIONS))
                .ifPresent(
                        x ->  jwtUser.setPermissions((List<String>) x));
        return jwtUser;
    }

    private Claims getClaims(String token) {
        Jws<Claims> jwsClaims =
                Jwts.parser().setSigningKey(jwtCertificate.getSecret()).parseClaimsJws(token);

        return jwsClaims.getBody();
    }

}

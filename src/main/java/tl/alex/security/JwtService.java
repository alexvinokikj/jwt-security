package tl.alex.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class JwtService {
    public static final String BEARER = "Bearer ";
    private static final String USER_NAME = "userName";
    private static final String USER_ID = "id";
    private static final String PERMISSIONS = "permissions";
    /*
    http://svlada.com/jwt-token-authentication-with-spring-boot/
    https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
    http://www.baeldung.com/spring-security-oauth-jwt

    */
    private static final long EXPIRATION_TIME = 36000000;

    @Autowired
    JwtCertificate jwtCertificate;

    public String createToken(JwtUser jwtUser) {
               try {
            Claims claims = Jwts.claims().setSubject("service");
            claims.put(USER_NAME, jwtUser.getUserName());
            claims.put(USER_ID, jwtUser.getUserId());
            claims.put(PERMISSIONS, getJwtPermissions(jwtUser.getPermissions()));

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
        Optional.ofNullable(claims.get(PERMISSIONS))
                .ifPresent(
                        x -> {

                                jwtUser.setPermissions(parseJwtPermissions((Map<String, List<String>>) x));

                        });
        return jwtUser;
    }

    private Claims getClaims(String token) {
        Jws<Claims> jwsClain =
                Jwts.parser().setSigningKey(jwtCertificate.getSecret()).parseClaimsJws(token);

        return jwsClain.getBody();
    }

    private List<String> parseJwtPermissions(Map<String, List<String>> jwtPermissions) {
        List<String> permissions = new ArrayList<>();
        if (jwtPermissions == null) {
            return permissions;
        }

        for (Map.Entry<String, List<String>> permission : jwtPermissions.entrySet()) {
            String module = permission.getKey();
            List<String> actions = permission.getValue();
            if (actions.isEmpty()) {
                permissions.add(module);
                continue;
            }
            List<String> moduleActions =
                    actions
                            .stream()
                            .map(action -> String.format("%s.%s", module, action))
                            .collect(Collectors.toList());
            permissions.addAll(moduleActions);
        }
        return permissions;
    }

    private Map<String, List<String>> getJwtPermissions(List<String> permissions) {
        Map<String, List<String>> jwtPermissions = new HashMap<>();

        if (permissions == null) {
            return jwtPermissions;
        }

        for (String permission : permissions) {
            int separator = permission.lastIndexOf('.');
            if (separator == -1) {
                jwtPermissions.put(permission, new ArrayList<>());
                continue;
            }
            String module = permission.substring(0, separator);
            String action = permission.substring(separator + 1);
            Optional<List<String>> existingActions = Optional.ofNullable(jwtPermissions.get(module));

            if (existingActions.isPresent()) {
                existingActions.get().add(action);
            } else {
                List<String> moduleActions = new ArrayList<>();
                moduleActions.add(action);
                jwtPermissions.put(module, moduleActions);
            }
        }
        return jwtPermissions;
    }

}

package tl.alex.security;


import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.security.PublicKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtCertificate {

    private PublicKey publicKey;

    private Key privateKey;

    private SignatureAlgorithm algorithm;

   private String secret;
}

package tl.alex.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.context.SecurityContextHolder;

@ToString
@Getter
@Setter
public class JwtUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String userName;
    private List<String> permissions = new ArrayList<>();

    public static JwtUser current() {
        JwtAuthenticationToken auth =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return auth.getPrincipal();
    }
}

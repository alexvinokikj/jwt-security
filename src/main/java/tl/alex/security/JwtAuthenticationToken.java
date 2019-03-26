package tl.alex.security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtAuthenticationToken implements Authentication {

    private static final long serialVersionUID = 1L;
    private boolean isAuthenticated;
    private JwtUser user;

    public JwtAuthenticationToken(JwtUser user) {
        this.user = user;
        isAuthenticated = user != null;
    }

    @Override
    public String getName() {
        return this.user.getUserName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        setPermissionsFromToken(grantedAuthorities);

        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public JwtUser getPrincipal() { return user; }

    @Override
    public boolean isAuthenticated() { return isAuthenticated; }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    private void setPermissionsFromToken(ArrayList<GrantedAuthority> grantedAuthorities) {
        List<String> usrPermissions = this.user.getPermissions();

        Optional.ofNullable(usrPermissions)
                .ifPresent(
                        permissions -> {
                            if (permissions.isEmpty()) {
                                return;
                            }
                            grantedAuthorities.addAll(
                                    permissions
                                            .stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toList()));
                        });
    }
}

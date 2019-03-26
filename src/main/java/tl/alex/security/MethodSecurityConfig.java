package tl.alex.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();
        // Remove the ROLE_ prefix from RoleVoter for @Secured checks on methods
        accessDecisionManager
                .getDecisionVoters()
                .stream()
                .filter(RoleVoter.class::isInstance)
                .map(RoleVoter.class::cast)
                .forEach(it -> it.setRolePrefix(""));
        return accessDecisionManager;
    }
}
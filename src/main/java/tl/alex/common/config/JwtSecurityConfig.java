package tl.alex.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tl.alex.common.ApiValues;
import tl.alex.security.JwtAuthenticationFilter;
import tl.alex.security.JwtService;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

    static final String SECURITY_REALM = " JWT  Realm";
    @Autowired
    JwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui.html", "/auth/**")
                .permitAll()
                .and()
                .antMatcher(ApiValues.API_VERSION + "/**")
                .addFilterBefore(new JwtAuthenticationFilter(jwtService), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .and()
                .httpBasic()
                .realmName(SECURITY_REALM)
                .and()
                .csrf()
                .disable();
    }
}

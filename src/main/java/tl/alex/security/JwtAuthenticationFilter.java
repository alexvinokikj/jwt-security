package tl.alex.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tl.alex.common.ApiError;
import tl.alex.common.ApiErrors;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String X_AUTHORIZATION = "X-Authorization";
    public static final String NOT_AUTHORIZED = "Access is denied";
    public static final String TOKEN_IS_NOT_VALID = "Token is not valid";
    public static final String HEADER_IS_NOT_VALID = "Header is not valid";

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getMethod().equals("OPTIONS")) {

            String xAuth = request.getHeader(X_AUTHORIZATION);

            if (isValid(xAuth)) {
                try {
                    JwtAuthenticationToken auth = getAuthenticationFromToken(xAuth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (Exception e) {
                    log.error("Error parsing token", e);
                    setResponse(response, TOKEN_IS_NOT_VALID, e);
                    return;
                }
            } else {
                log.error("Bearer not valid");
                setResponse(response, HEADER_IS_NOT_VALID, null);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setResponse(HttpServletResponse response, String s, Exception e) throws IOException {
        ApiErrors errors = new ApiErrors();
        ApiError error = new ApiError(s, "");
        if (e != null) {
            error.setDebugMessage(e.getMessage());
        }
        errors.setError(error);
        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        String errorJson = mapper.writeValueAsString(errors);
        writer.write(errorJson);
        response.addHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        CorsFilter.setHeaders(response);
        writer.flush();
        writer.close();
    }

    private boolean isValid(String xAuth) {
        if (StringUtils.isEmpty(xAuth)) {
            return false;
        }
        return xAuth.length() >= JwtService.BEARER.length();
    }

    private JwtAuthenticationToken getAuthenticationFromToken(String xAuth) {
        String token = xAuth.substring(JwtService.BEARER.length(), xAuth.length());
        JwtUser user = jwtService.getUser(token);
        return new JwtAuthenticationToken(user);
    }
}

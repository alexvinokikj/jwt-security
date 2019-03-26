package tl.alex.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tl.alex.api.controllers.AdminController;
import tl.alex.security.JwtAuthenticationFilter;
import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig implements WebMvcConfigurer {

    @Autowired
    Environment enviroment;
    @Autowired protected ObjectMapper mapper;

    @Bean
    public Docket swaggerSettings() {
        boolean showInternalControllers =
                Boolean.parseBoolean(enviroment.getProperty("swagger.showInternalControllers"));
        Predicate<RequestHandler> apisPredicate =
                showInternalControllers
                        ? RequestHandlerSelectors.any()
                        : Predicates.not(
                        RequestHandlerSelectors.basePackage(AdminController.class.getPackage().getName()));

        Docket build =
                new Docket(DocumentationType.SWAGGER_2)
                        .groupName("_rest")
                        .select()
                        .apis(apisPredicate)
                        .paths(PathSelectors.any())
                        .build();
        build
                .securitySchemes(newArrayList(apiKey()))
                .securityContexts(newArrayList(securityContext()));
        return build;
    }

    private ApiKey apiKey() {
        return new ApiKey(
                JwtAuthenticationFilter.X_AUTHORIZATION, JwtAuthenticationFilter.X_AUTHORIZATION, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(JwtAuthenticationFilter.X_AUTHORIZATION, authorizationScopes));
    }
}

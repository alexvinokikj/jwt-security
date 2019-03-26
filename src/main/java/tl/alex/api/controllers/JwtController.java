package tl.alex.api.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tl.alex.common.ApiValues;
import tl.alex.security.JwtService;
import tl.alex.security.JwtUser;

@RestController
@RequestMapping(ApiValues.JWT_ROUTE)
public class JwtController {

    @Autowired
    JwtService jwtService;

    @GetMapping
    @ApiOperation(value = "Generate jwt token based on claims", response = String.class)
    public String generateJwt(JwtUser user){
        return jwtService.createToken(user);
    }
}

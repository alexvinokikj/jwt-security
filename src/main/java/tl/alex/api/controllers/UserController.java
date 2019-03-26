package tl.alex.api.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tl.alex.common.ApiValues;
import tl.alex.security.Permissions;

@RestController
@RequestMapping(ApiValues.USER_ROUTE)
public class UserController {

    @Secured(Permissions.USER)
    @ApiOperation(value = "Get welcome message for regular user", response = String.class)
    @GetMapping
    public ResponseEntity<String> getWelcomeMessage(){
        return new ResponseEntity<>("Say Hello, with user permission", HttpStatus.OK);
    }
}

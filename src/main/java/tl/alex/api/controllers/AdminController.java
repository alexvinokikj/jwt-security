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
@RequestMapping(ApiValues.ADMIN_ROUTE)
public class AdminController {

    @Secured(Permissions.ADMIN)
    @ApiOperation(value = "Get welcome message for admin", response = String.class)
    @GetMapping
    public ResponseEntity<String> getWelcomeMessage(){
        return new ResponseEntity<>("Hello for admin user.",HttpStatus.OK);
    }

}

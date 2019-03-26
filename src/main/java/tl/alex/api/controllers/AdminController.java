package tl.alex.api.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tl.alex.common.ApiValues;

@RestController
@RequestMapping(ApiValues.ADMIN_ROUTE)
public class AdminController {

 //   @PreAuthorize("ADMIN")
    @ApiOperation(value = "Get welcome message ", response = String.class)
    @GetMapping
    public ResponseEntity<String> getWelcomeMessage(){
        return new ResponseEntity<>("Hello",HttpStatus.OK);
    }

}

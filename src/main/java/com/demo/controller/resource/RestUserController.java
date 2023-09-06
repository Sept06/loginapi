package com.demo.controller.resource;

import com.demo.model.response.BaseResponse;
import com.demo.service.IUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/user")
public class RestUserController {

    @Autowired
    private IUserServices iUserServices;

    @GetMapping()
    public ResponseEntity<?> users(){
        return ResponseEntity.ok(BaseResponse.builder().message(HttpStatus.OK.name()).code(HttpStatus.OK.value()).data(iUserServices.getUsers()).build());
    }
}

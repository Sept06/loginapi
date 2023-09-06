package com.demo.controller.resource;

import com.demo.model.request.LoginRequest;
import com.demo.model.response.BaseResponse;
import com.demo.service.IUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthenticationController {

    @Autowired
    private IUserServices iUserServices;


    @PostMapping("login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(iUserServices.login(loginRequest));
    }
}

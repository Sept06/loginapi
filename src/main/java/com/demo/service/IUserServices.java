package com.demo.service;


import com.demo.model.dto.UserDto;
import com.demo.model.request.LoginRequest;
import com.demo.model.response.BaseResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface IUserServices {

    List<UserDto> getUsers();

    BaseResponse login(LoginRequest request);

}

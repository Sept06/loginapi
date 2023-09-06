package com.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.demo.entity.RoleEntity;
import com.demo.entity.UserEntity;
import com.demo.model.dto.UserDto;

import com.demo.model.request.LoginRequest;
import com.demo.model.response.BaseResponse;
import com.demo.repositories.UserRepository;
import com.demo.security.JwtTokenProvider;
import com.demo.service.IRoleService;
import com.demo.service.IUserServices;
import com.demo.utils.Constants;
import com.demo.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements IUserServices {

    private final UserRepository userRepository;
    private final IRoleService iRoleService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    public UserServiceImpl(UserRepository userRepository, IRoleService iRoleService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.iRoleService = iRoleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;
    }

    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> userDtos = userRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());

        Object userLogin = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogin instanceof UserDetails){
            String userName = ((UserDetails)userLogin).getUsername();
            System.out.println("user request: " + userName);
        }
        return userDtos;
    }



    @Override
    public BaseResponse login(LoginRequest request) {
        BaseResponse response = new BaseResponse();
        // query username, password trong database
        UserEntity userEntity = userRepository.findByUsername(request.getUsername());

        // nếu không tồn tại => return response bad request
        if (userEntity == null){
            response.setMessage("Username or password not exits!");
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        Boolean validUser = passwordEncoder.matches(request.getPassword(),userEntity.getPassword());
        if (!validUser){
            response.setMessage("Username or password not exits!");
            response.setCode(HttpStatus.BAD_REQUEST.value());
            return response;
        }

        // nếu tồn tại user
        // set security context: xác định user naò đang thực hiện request
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // tạo ra token
        Date currentDate = new Date();
        String token = tokenProvider.createToken(userEntity,currentDate);

        // set token vào response và trả ra
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode modelToken = objectMapper.createObjectNode();
        modelToken.put("accessToken",token);

        response.setData(modelToken);
        response.setMessage("Success!");
        response.setCode(HttpStatus.OK.value());
        return response;
    }



    private UserDto convertEntityToDto(UserEntity data) {
        UserDto userDto = modelMapper.map(data,UserDto.class);
        try {
            userDto.setBirthdayStr(DateUtils.convertDateToString(data.getBirthday(),DateUtils.FORMAT_DATE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.getRoleEntities() != null){
            String[] roleNames = data.getRoleEntities().stream().map(RoleEntity::getName).toArray(String[]::new);
            userDto.setRoleName(roleNames);
        }
        if (Constants.MALE.equals(data.getGender())){
            userDto.setGenderStr(Constants.MALE_STR);
        }else {
            userDto.setGenderStr(Constants.FEMALE_STR);
        }
        return userDto;
    }
}

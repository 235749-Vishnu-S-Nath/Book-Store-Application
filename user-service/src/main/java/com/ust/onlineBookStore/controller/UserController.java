package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Role;
import com.ust.onlineBookStore.domain.User;
import com.ust.onlineBookStore.dto.LoginDto;
import com.ust.onlineBookStore.dto.RegisterDto;
import com.ust.onlineBookStore.dto.UserDto;
import com.ust.onlineBookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto){
        final var user = userService.checkLogin(loginDto.username(),loginDto.password());
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(EntityToDto(user.get()));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto){
        long id = userService.findCount()+1;
        final var user = RegisterDtoToEntity(registerDto,id);
        final var response = userService.findByUsername(user.getUsername());
        if(response.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityToDto(userService.save(user)));
    }

    public User DtoToEntity(UserDto userDto){
        return new User(userDto.userId(), userDto.username(), userDto.email(), userDto.password(), userDto.role());
    }

    public UserDto EntityToDto(User user){
        return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
    }

    public User RegisterDtoToEntity(RegisterDto registerDto,long id){
        return new User(id,registerDto.username(), registerDto.email(), registerDto.password(), Role.USER);
    }
}

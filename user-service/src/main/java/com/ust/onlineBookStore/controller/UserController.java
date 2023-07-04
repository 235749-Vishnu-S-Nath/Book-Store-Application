package com.ust.onlineBookStore.controller;

import com.ust.onlineBookStore.domain.Role;
import com.ust.onlineBookStore.domain.User;
import com.ust.onlineBookStore.dto.LoginDto;
import com.ust.onlineBookStore.dto.RegisterDto;
import com.ust.onlineBookStore.dto.UserDto;
import com.ust.onlineBookStore.jwt.JwtConfig;
import com.ust.onlineBookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto){
        final var user = userService.findByUsername(loginDto.username());
        if (user.isEmpty() || !bCryptPasswordEncoder.matches(loginDto.password(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(EntityToDto(user.get(),jwtConfig.generateToken(user.get().getUsername(),user.get().getRole())));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto){
        long id = userService.findCount()+1;
        final var user = RegisterDtoToEntity(registerDto,id);
        final var response = userService.findByUsername(user.getUsername());
        if(response.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityToDto(userService.save(user),"No token generated"));
    }

    public UserDto EntityToDto(User user,String token){
        return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(),token, user.getRole());
    }

    public User RegisterDtoToEntity(RegisterDto registerDto,long id){
        return new User(id,registerDto.username(), registerDto.email(), registerDto.password(), Role.ROLE_USER);
    }
}

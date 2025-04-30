package com.project.ecommerce.controller;

import com.project.ecommerce.entity.user.*;
import com.project.ecommerce.infra.security.TokenService;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();

        var  acessToken = tokenService.generateAccessToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);


        ApiResponse<LoginResponseDTO> response = new ApiResponse<>(
                true,
                "Login feito com sucesso",
                new LoginResponseDTO(acessToken,refreshToken)
        );
        return ResponseEntity.ok(response);

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid RegisterDTO data){

        if(repository.findByLogin(data.login())!=null) {
           ApiResponse<Void> response = new ApiResponse<>(
                   false,
                   "Já existe um usuario com esse nome",
                   null
           );
           return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(),encryptedPassword,data.role());


        this.repository.save(newUser);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Usuario criado com sucesso",
                null
        );


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestBody @Valid RefreshRespondeDTO data){
        String refreshToken = data.refreshToken();
        String subject = tokenService.validateToken(data.refreshToken());

        if (subject.isEmpty()){
            return ResponseEntity.status(401).build();
        }

        User user  = repository.findByLogin(subject);

        if (user == null){
            return ResponseEntity.status(401).build();
        }

        var newAcessToken = tokenService.generateAccessToken(user);
        var newRefreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(newAcessToken,newRefreshToken));
    }
}

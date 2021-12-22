package com.example.student_management.controller;

import com.example.student_management.domain.Role;
import com.example.student_management.domain.User;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.request.ForgotPasswordRequest;
import com.example.student_management.request.LoginRequest;
import com.example.student_management.request.ResetPasswordRequest;
import com.example.student_management.request.SignUpRequest;
import com.example.student_management.response.LoginResponse;
import com.example.student_management.response.RefreshTokenResponse;
import com.example.student_management.security.jwt.JwtProvider;
import com.example.student_management.service.MailService;
import com.example.student_management.service.RoleService;
import com.example.student_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@SecurityRequirements
@ApiResponses(value = {
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed")
})
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public AuthController(UserService userService, RoleService roleService, JwtProvider jwtProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200", description = "Login success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad credentials", content = @Content)
    public ResponseEntity<Object> authenticateLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtProvider.generateLoginToken(userDetails);
        String refresh_token = jwtProvider.generateRefreshToken(jwt);
        return new ResponseEntity<>(new LoginResponse(jwt, "Bearer", refresh_token), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up")
    @ApiResponse(responseCode = "201", description = "User created", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
    public ResponseEntity<Object> signup(@RequestBody SignUpRequest request) {
        Role role = roleService.findByRoleName("user");
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .loginFailedCount(0)
                .role(role)
                .build();

        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password")
    @ApiResponse(responseCode = "200", description = "Sent mail success", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordRequest request, HttpServletRequest httpServletRequest) throws MessagingException, UnsupportedEncodingException {
        User user = userService.findByEmail(request.getEmail());
        // TODO: add expiration into token
        String token = RandomString.make(100);
        log.info("Forgot password token: {}", token);
        user.setForgotPasswordToken(token);
        userService.save(user);
        String requestURL = httpServletRequest.getRequestURL().toString();
        String siteURL = requestURL.replace(httpServletRequest.getServletPath(), "");
        String link = siteURL + "/reset-password?token=" + token;
        String template = "forgot-password-mail";
        mailService.sendMail(user.getEmail(), link, template);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    @ApiResponse(responseCode = "200", description = "Reset password success", content = @Content)
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail());
        String userToken = user.getForgotPasswordToken();
        if (userToken != null && userToken.equals(request.getToken())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            throw new DataInvalidException(ExceptionMessage.TOKEN_INVALID.message);
        }
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> logout(
            @RequestParam(value = "grant_type") String grantType,
            @RequestParam(value = "refresh_token") String refreshToken,
            @RequestParam(value = "access_token") String accessToken
    ) {
        if (!grantType.equals("refresh_token")) {
            throw new DataInvalidException("Grant type invalid");
        }
        if (!accessToken.equals(jwtProvider.getSubjectFromToken(refreshToken))) {
            throw new DataInvalidException("Access token mismatch");
        }
        if (!jwtProvider.validateToken(accessToken)) {
            throw new DataInvalidException("Access token invalid");
        }

        String newAccessToken = jwtProvider.generateLoginToken(userService.loadUserByUsername(jwtProvider.getSubjectFromToken(accessToken)));
        return new ResponseEntity<>(new RefreshTokenResponse(newAccessToken), HttpStatus.OK);
    }
}

package com.capstone.pazaak.controller;

import com.capstone.pazaak.model.Request.LoginRequest;
import com.capstone.pazaak.model.User;
import com.capstone.pazaak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth/users")
public class UserController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public User createUser(@RequestBody User userObject) {
        return userService.createUser(userObject);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("calling loginUser");
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/list")
    public Iterable<User> listUsers() {
        return userService.listUsers();
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/user")
    public User getUserByJWT() {
        return userService.getUserByJWT();
    }

    @PutMapping("/changepassword")
    public User changePassword(@RequestBody User userObject) {
        return userService.changePassword(userObject);
    }

    @PutMapping("/wins")
    public User updateUserWins(@RequestBody User userObject) {
        return userService.updateUserWins(userObject);
    }

    @PutMapping("/losses")
    public User updateUserLosses(@RequestBody User userObject) {
        return userService.updateUserLosses(userObject);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        userService.deleteUserById();
        return ResponseEntity.ok().body("Deleting user...");
    }
}

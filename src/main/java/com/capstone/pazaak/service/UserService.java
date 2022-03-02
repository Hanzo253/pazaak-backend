package com.capstone.pazaak.service;

import com.capstone.pazaak.exceptions.InformationExistsException;
import com.capstone.pazaak.exceptions.InformationNotFoundException;
import com.capstone.pazaak.model.Request.LoginRequest;
import com.capstone.pazaak.model.Response.LoginResponse;
import com.capstone.pazaak.model.User;
import com.capstone.pazaak.repository.UserRepository;
import com.capstone.pazaak.security.MyUserDetails;
import com.capstone.pazaak.security.jwt.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JWTUtils jwtUtils;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // create new user and save it in the database
    public User createUser(User userObject) {
        if (!userRepository.existsByEmailAddress(userObject.getEmailAddress())) {
            userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(userObject);
        } else {
            // throw exception if the user already exists
            throw new InformationExistsException("user with email address " + userObject.getEmailAddress() + " already exists.");
        }
    }

    // checks if user login information is correct and returns the jwt key
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        System.out.println(userDetails);
        final String JWT = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(JWT));
    }

    // changes the user's password
    public User changePassword(@RequestBody User userObject) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUserName(userDetails.getUser().getUserName());
        if (user == null) {
            // throw exception if the user does not exist
            throw new InformationNotFoundException("User not found.");
        } else {
//            System.out.println(user);
//            System.out.println(userObject.getPassword());
            user.setPassword(passwordEncoder.encode(userObject.getPassword()));
            return userRepository.save(user);
        }
    }

    // locates user by their email address
    public User findUserByEmailAddress(String email) {
        return userRepository.findUserByEmailAddress(email);
    }

    // getter for the user
//    @Override
    public User getUser(String username) {
        if (userRepository.findUserByUserName(username) == null) {
            // throw exception if the user does not exist
            throw new InformationNotFoundException("User not found.");
        } else {
            return userRepository.findUserByUserName(username);
        }
    }
    public User getUserByJWT() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUserName(userDetails.getUser().getUserName());
        if (user == null) {
            // throw exception if the user does not exist
            throw new InformationNotFoundException("User not found.");
        } else {
            return user;
        }
    }

    // returns a list of all users in the users database
    public Iterable<User> listUsers() {
        if (userRepository.findAll().size() == 0) {
            // throw exception if there are no users in the list
            throw new InformationNotFoundException("No users found.");
        } else {
            return userRepository.findAll();
        }
    }

    public User updateUserWins(@RequestBody User userObject) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUserName(userDetails.getUser().getUserName());

        if (user == null) {
            // throw exception if the user does not exist
            throw new InformationNotFoundException("User not found.");
        } else {
            user.setWins(userObject.getWins());
            return userRepository.save(user);
        }
    }

    public User updateUserLosses(@RequestBody User userObject) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUserName(userDetails.getUser().getUserName());

        if (user == null) {
            // throw exception if the user does not exist
            throw new InformationNotFoundException("User not found.");
        } else {
            user.setLosses(userObject.getLosses());
            return userRepository.save(user);
        }
    }

    // deletes the user by their id
    public void deleteUserById() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getUser().getId());
        if (user.isEmpty()) {
            // throw exception if the user does not exist
            throw new InformationNotFoundException("User not found.");
        } else {
//            if (!user.get().getPokemonList().isEmpty()) {
//                // throw exception if the user does not have an empty pokemonList
//                throw new InformationExistsException("User needs to empty their pokemonList and favoritePokemonList in order to be deleted");
//            } else {
//                userRepository.deleteById(userDetails.getUser().getId());
//            }
            userRepository.deleteById(userDetails.getUser().getId());
        }
    }
}

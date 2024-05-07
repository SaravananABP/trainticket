package com.booking.trainticket.Authentication.Controller;

import com.booking.trainticket.Authentication.Pojo.AuthRequest;
import com.booking.trainticket.Authentication.Pojo.UserDetailsInfo;

import com.booking.trainticket.Authentication.Repository.UserDetailsRepo;
import com.booking.trainticket.Authentication.Service.JwtService;
import jakarta.websocket.server.PathParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class RestAPI {
    @Autowired
    UserDetailsRepo userDetailsRepo;
    @Autowired
    JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/create/newUser")
    public JSONObject createNewUser(@RequestBody UserDetailsInfo userDetailsInfo) {
        String regexPassword = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{4,}$";
        String regexMail = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
        String regexMobileNo = "^[0-9]{10}$"; // 10-digit number

        Pattern passwordPattern = Pattern.compile(regexPassword);
        Pattern mailPattern = Pattern.compile(regexMail);
        Pattern mobileNoPattern = Pattern.compile(regexMobileNo);

        JSONObject response = new JSONObject();

        if (userDetailsRepo.findByUserName(userDetailsInfo.getUserName()) == null) {
            if (!passwordPattern.matcher(userDetailsInfo.getPassword()).matches()) {
                response.put("status", "Invalid password !!!");
                response.put("password", "Make a strong password");
                response.put("match_with", "Minimum length 8, at least 1 letter (uppercase, lowercase), and a special character");
                response.put("SamplePassword", "Qwerty@123");
            } else if (!mailPattern.matcher(userDetailsInfo.getEmailId()).matches()) {
                response.put("status", "Invalid email address !!!");
            } else if (!mobileNoPattern.matcher(String.valueOf(userDetailsInfo.getMobileNo())).matches()) {
                response.put("status", "Invalid mobile number !!!");
            } else {
                // Set default role as "user" if role is null
                if (userDetailsInfo.getRole() == null) {
                    userDetailsInfo.setRole("USER");
                }

                // Parse date of birth string into LocalDate
//                String[] parts = userDetailsInfo.getDateOfbirth().split("-");
//                int day = Integer.parseInt(parts[0]);
//                int month = Integer.parseInt(parts[1]);
//                int year = Integer.parseInt(parts[2]);
//                userDetailsInfo.setDoB(LocalDate.of(year, month, day));

                // Encode password
                userDetailsInfo.setPassword(passwordEncoder.encode(userDetailsInfo.getPassword()));

                // Save UserDetailsInfo
                userDetailsRepo.save(userDetailsInfo);

                response.put("Status", "User created successfully");
            }
        } else {
            response.put("status", "Failed !!!");
            response.put("message", "User already exists");
        }

        return response;
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@PathVariable String email) {
        return "deleted successfully" + email;
    }

    @GetMapping("/allUser")
    public UserDetailsInfo getallUserDetails() {
        return (UserDetailsInfo) userDetailsRepo.findAll();
    }

    @GetMapping("/particular/user")
    public UserDetailsInfo getparticularUserDetails(@PathParam("email") String email) {
        return userDetailsRepo.findByEmailId(email);
    }

    @PostMapping("/authenticate")
    public JSONObject authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            JSONObject response = jwtService.generateToken(authRequest.getUsername());
            return response;
        } else {
            System.out.println("invalid user");
        }
        return null;
    }
    @GetMapping("/test/")
    public String test(@PathParam("name") String name){
        return "Success"+ name;
    }
}

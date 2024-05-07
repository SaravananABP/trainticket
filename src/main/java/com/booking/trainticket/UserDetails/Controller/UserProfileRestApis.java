package com.booking.trainticket.UserDetails.Controller;

import com.booking.trainticket.Authentication.Pojo.UserDetailsInfo;
import com.booking.trainticket.Authentication.Repository.UserDetailsRepo;
//import com.booking.trainticket.UserDetails.Service.AuthenticationService;
import com.booking.trainticket.Authentication.Service.UserInfoDetailsService;
import com.booking.trainticket.UserDetails.PojoClass.UpdatePassword;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserProfileRestApis {
    @Autowired
    UserDetailsRepo userDetailsRepo;
    @Autowired
    UserInfoDetailsService userInfoDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/myProfile")
    public JSONObject userProfile(){
        String emailId=userInfoDetailsService.getUserEmail();
        JSONObject response = new JSONObject();
        if(emailId!=null) {
            UserDetailsInfo userDetails = userDetailsRepo.findByEmailId(emailId);
            if (!userDetails.equals(null)) {
                response.put("name", userDetails.getUserName());
                response.put("Email", userDetails.getEmailId());
                response.put("mobile no", userDetails.getMobileNo());
                response.put("Address", userDetails.getAddress());
                response.put("Date of Birth", userDetails.getDateOfbirth());
            }
        }
        else {
            response.put("status","User Details not available");
        }
        return response;
    }
    @PostMapping("/edit/profile")
    public JSONObject profileEdit(@RequestBody UserDetailsInfo userDetailsInfo){
        JSONObject response = new JSONObject();
        String emailId=userInfoDetailsService.getUserEmail();
        if(!(userDetailsInfo ==null)){
            UserDetailsInfo userDetails = userDetailsRepo.findByEmailId(emailId);
            if(userDetails.getUserName()!=null){
                userDetailsRepo.UpdateUserName(emailId,userDetails.getUserName());
            }
            if(userDetails.getAddress()!=null){
                userDetailsRepo.UpdateUserAddress(emailId,userDetails.getAddress());
            }
            if(userDetails.getDateOfbirth()!=null){
                userDetailsRepo.UpdateUserDateOfBirth(emailId,userDetails.getDateOfbirth());
            }
            if(userDetails.getMobileNo()!=null){
                userDetailsRepo.UpdateUserMobile(emailId,userDetails.getMobileNo());
            }
            response.put("Status","successfully Updated!!!");
        }else {
            response.put("status","No data found to Update");
        }
        return response;
    }

    @PostMapping("/forgot/password")
    public JSONObject UpdatePassword(@RequestBody UpdatePassword updatePassword){
        JSONObject response = new JSONObject();
        String TokenEmailId=userInfoDetailsService.getUserEmail();
        if(updatePassword!=null) {
            if (TokenEmailId != null) {
                UserDetailsInfo userDetails = userDetailsRepo.findByEmailId(TokenEmailId);
                if (updatePassword.getPassword().equals(updatePassword.getConformPassword()) && updatePassword.getEmailId() != null) {
                    updatePassword.setPassword(passwordEncoder.encode(updatePassword.getPassword()));
                    userDetailsRepo.UpdateUserPassword(updatePassword.getEmailId(), updatePassword.getPassword());
                }
            } else {
                UserDetailsInfo userDetails = userDetailsRepo.findByEmailId(updatePassword.getEmailId());
                if (updatePassword.getPassword().equals(updatePassword.getConformPassword()) && updatePassword.getEmailId() != null) {
                    updatePassword.setPassword(passwordEncoder.encode(updatePassword.getPassword()));
                    userDetailsRepo.UpdateUserPassword(updatePassword.getEmailId(), updatePassword.getPassword());
                }
            }
            response.put("Status","successfully Updated!!!");
        }else {
            response.put("status","No data found to Update");
        }
        return response;
    }

}

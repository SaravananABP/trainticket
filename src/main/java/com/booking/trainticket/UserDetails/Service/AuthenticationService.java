//package com.booking.trainticket.UserDetails.Service;
//
//import com.booking.trainticket.Authentication.Filter.JwtAuthFilter;
//import com.booking.trainticket.Authentication.Service.UserInfoUserDetails;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticationService {
//    @Autowired
//    JwtAuthFilter jwtAuthFilter;
//    @Autowired
//    UserInfoUserDetails userInfoUserDetails;
//
//
//    public  String currentToken(){
//        return jwtAuthFilter.getToken();
//    }
//    public String getUsernameFromToken() {
//        String token =currentToken();
//        String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();
//        System.out.println(claims.getSubject());
//        return claims.getSubject();
//
//
//    }
//}
//

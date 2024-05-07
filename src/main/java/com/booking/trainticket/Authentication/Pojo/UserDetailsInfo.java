package com.booking.trainticket.Authentication.Pojo;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.LocalDate;


@Data
@Getter
@Node("User")
public class UserDetailsInfo {
    @Id
    @GeneratedValue
    private Long id;


    @Property(name = "userName")
    private String userName;

    @Property(name = "password")
    private String password;

    @Property(name = "role")
    private String role;

//    @Property(name = "dateOfBirth")
////    private LocalDate DoB;
//    private String DoB;

    @Property(name = "Address")
    private String address;

    @Property(name = "contactNo")
    private Long mobileNo;

    @Property(name = "emailId")
    private String emailId;

    private  String DateOfbirth;

}

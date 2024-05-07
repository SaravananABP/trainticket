package com.booking.trainticket.UserDetails.PojoClass;

import lombok.Data;

@Data
public class UpdatePassword {
    private String emailId;
    private String password;
    private String conformPassword;
}

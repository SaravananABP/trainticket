package com.booking.trainticket.Booking.PojoClass;

import lombok.Data;

@Data
public class CreateTicket {
    public String arrival;
    public String  departure;
    public String type;
    public String classType;
}

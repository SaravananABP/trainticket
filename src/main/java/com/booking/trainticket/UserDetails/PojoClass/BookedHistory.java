package com.booking.trainticket.UserDetails.PojoClass;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("BookedHistory")
public class BookedHistory {
    @Id
    @GeneratedValue
    Long id;
    String arrival;
    String departure;
    String Paid;
    String TrainType;
    String distance;
}

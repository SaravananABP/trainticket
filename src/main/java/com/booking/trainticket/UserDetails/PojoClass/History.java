package com.booking.trainticket.UserDetails.PojoClass;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Data
@Node("LocalHistory")
public class History {
    @Id
    @GeneratedValue
    Long id;
    String localHistory;
    String walletHistory;
    String BookingHistory;
    String transectionHistory;
}

package com.booking.trainticket.UserDetails.PojoClass;

import lombok.Data;
import org.json.simple.JSONObject;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;
import java.util.Map;

@Data
@Node("Wallet")
public class Wallet {
    @Id
    @GeneratedValue
    Long id;
    Float availableAmt;
    String walletHistory;
}

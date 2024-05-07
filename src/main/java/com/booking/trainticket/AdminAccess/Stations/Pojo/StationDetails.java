package com.booking.trainticket.AdminAccess.Stations.Pojo;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Node("Station")
public class StationDetails {
    @Id
    @GeneratedValue
    private Long id;

    @Property("coordinates")
    public List<Double> coordinates;
    @Property("longitude")
    public Double longitude;
    @Property("latitude")
    public Double latitude;
    @Property("state")
    public String  state;
    @Property("code")
    public String  code;
    @Property("name")
    public String  name;
    @Property("zone")
    public String  zone;
    @Property("address")
    public String address;

    @Relationship(type = "towards",direction = Relationship.Direction.INCOMING)
    public ArrayList<Relationship_properties> relationshipProperties;

//    public Relationship_properties relationshipProperties;

//    @Property("arrival")
//    public String arrival;
//    @Property("departure")
//    public String departure;
//    @Property("flag")
//    public String flag;
//    @Property("distance")
//    public Double distance;

}

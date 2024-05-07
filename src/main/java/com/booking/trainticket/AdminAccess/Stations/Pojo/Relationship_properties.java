package com.booking.trainticket.AdminAccess.Stations.Pojo;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalTime;

@Data
public class Relationship_properties {
    @Id
    @GeneratedValue
    private Long id;
    @Property("stationStops")
    public Long stationStops;
    @Property("flag")
    public String flag;
    @Property("arrival")
    public LocalTime arrival;
    @Property("departure")
    public LocalTime departure;
    String trainName;
//    @Property("distance")
//    public String distance;
}

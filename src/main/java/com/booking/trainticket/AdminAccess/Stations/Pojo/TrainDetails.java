package com.booking.trainticket.AdminAccess.Stations.Pojo;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@Node("Train")
public class TrainDetails {
    @Id
    @GeneratedValue
    private Long id;

    @Property("third_ac")
    public String third_ac;
    @Property("arrival")
    public String arrival;
    @Property("from_station_code")
    public String from_station_code;
    @Property("name")
    public String name;
    @Property("zone")
    public String zone;
    @Property("chair_car")
    public String chair_car;
    @Property("first_class")
    public String first_class;
    @Property("duration_m")
    public Integer duration_m;
    @Property("sleeper")
    public String sleeper;
    @Property("from_station_name")
    public String from_station_name;
    @Property("number")
    public long number;
    @Property("departure")
    public String  departure;
    @Property("return_train")
    public Long return_train;
    @Property("to_station_code")
    public String to_station_code;
    @Property("second_ac")
    public String second_ac;
    @Property("classes")
    public String classes;
    @Property("to_station_name")
    public String to_station_name;
    @Property("duration_h")
    public Integer duration_h;
    @Property("type")
    public String type;
    @Property("first_ac")
    public String first_ac;
    @Property("distance")
    public Integer distance;

    @Property("coordinates_stop")
    public List<String> coordinates_stop;
    @Property("coordinates_AllStop")
    public List<String> coordinates_AllStop;
    @Property("coordinatesStopTime")
    public List<String> coordinatesStopTime;
    @Property("coordinatesAllStopTime")
    public List<String> coordinatesAllStopTime;
    @Relationship(type = "towards")
    public ArrayList<Relationship_properties> relationshipProperties;

//    @Property("code")
//    public String code;

    @Override
    public String toString() {
        return "TrainDetails{" +
                "id=" + id +
                ", third_ac='" + third_ac + '\'' +
                ", arrival=" + arrival +
                ", from_station_code='" + from_station_code + '\'' +
                ", name='" + name + '\'' +
                ", zone='" + zone + '\'' +
                ", chair_car='" + chair_car + '\'' +
                ", first_class='" + first_class + '\'' +
                ", duration_m=" + duration_m +
                ", sleeper='" + sleeper + '\'' +
                ", from_station_name='" + from_station_name + '\'' +
                ", number=" + number +
                ", departure=" + departure +
                ", return_train=" + return_train +
                ", to_station_code='" + to_station_code + '\'' +
                ", second_ac='" + second_ac + '\'' +
                ", classes='" + classes + '\'' +
                ", to_station_name='" + to_station_name + '\'' +
                ", duration_h=" + duration_h +
                ", type='" + type + '\'' +
                ", first_ac='" + first_ac + '\'' +
                ", distance=" + distance +
                ", coordinates_stop=" + coordinates_stop +
                ", coordinates_AllStop=" + coordinates_AllStop +
                ", coordinatesStopTime=" + coordinatesStopTime +
                ", coordinatesAllStopTime=" + coordinatesAllStopTime +
                ", relationshipProperties=" + relationshipProperties +
                '}';
    }
}

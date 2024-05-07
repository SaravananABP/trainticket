package com.booking.trainticket.AdminAccess.Stations.Repository;

import com.booking.trainticket.AdminAccess.Stations.Pojo.StationDetails;
import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableNeo4jRepositories

public interface StationRepo extends Neo4jRepository<StationDetails,Long> {

    @Query("MERGE (s:Station {coordinates: $coordinates, state: $state, code: $code, name: $name, zone: $zone, address: $address, longitude: $longitude, latitude: $latitude}) RETURN s.name")

    StationDetails createStation(List<Double> coordinates,
                                 String state,
                                 String code,
                                 String name,
                                 String zone,
                                 String address,
                                 Double longitude,
                                 Double latitude);
    //
//    "coordinates","state","code","name","zone","address",longitude,latitude
    @Query("match(n:Station{code:$code})set n.state=$state return n.state")
    String updateState(String code,String state);
    @Query("MATCH (s:Station {code: $code})" +
            "RETURN COUNT(s) = 0")
    boolean checkStationPresence(String code);


//    @Query("merge (S:Station{coordinates:$source}-[:to_station]->(d:Station{coordinates:$destination})return d.name")
    @Query("MATCH (s:Source_Station {coordinates: $source}), (d:Destination_Station {coordinates: $destination}) " +
        "MERGE (s)-[:to_station]->(d) " +
        "MERGE (d)-[:to_station]->(s) " +
        "WITH s, d " +
        "MATCH (s)-[r1:to_station]->(d) " +
        "MATCH (d)-[r2:to_station]->(s) " +
        "SET r1.km = $distance, r2.km = $distance " +
        "RETURN r1, r2")
    StationDetails Make_source_to_destination(String source,String destination,String distance);

    @Query("MATCH (s:Source_Station {coordinates: $source}), (d:Destination_Station {coordinates: $destination}) " +
            "MERGE (s)-[:to_station]->(d) " +
            "MERGE (d)-[:to_station]->(s) " +
            "WITH s, d " +
            "MATCH (d)-[r1:to_station]->(s) " +
            "MATCH (s)-[r2:to_station]->(d) " +
            "SET r1.km = $distance, r2.km = $distance " +
            "RETURN r1, r2")
    StationDetails Make_destination_to_source(String source,String destination,String distance);

    @Query("MATCH (s:Station {code: $source1}), (d:Station {code: $source2}) " +
            "CREATE (s)-[r:to_all_station]->(d) " +
            "SET r.distance = $distance, " +
            " r.arrival = $arrival, " +
            " r.departure = $departure, " +
            " r.flag = $flag, " +
            " r.number = $number " +
            "RETURN s")
    StationDetails relationshipWithBetweenTwoStations(String source1, String source2, Double distance, String arrival, String departure, String flag, String number);

    @Query("MATCH (s:Station {code: $source1}), (d:Station {code: $source2}) " +
            "CREATE (s)-[r:to_station]->(d) " +
            "SET r.distance = $distance, " +
            " r.arrival = $arrival, " +
            " r.departure = $departure, " +
            " r.flag = $flag, " +
            " r.number = $number " +
            "RETURN s")
    StationDetails relationshipWithBetweenTwoStationsFlagGreen(String source1, String source2, Double distance, String arrival, String departure, String flag, String number);



    @Query("Match(n:Station{code:$name})return n")
    StationDetails coordinatesLocation(String name);

    @Query("Match(n:Station{code:$name})return n.latitude")
    Double getLatitude(String name);
    @Query("Match(n:Station{code:$name})return n.longitude")
    Double getLongitude(String name);

//    @Query("MATCH (s:Station {coordinates: $source1}), (d:Station {coordinates: $source2}), (t:Train {number :$number} " +
//
//            "create (s)-[r1:to_station]->(d) " +
//            "merge (t)-[r2:towards]->(s)"+
//            "SET r2.arrival = $arrival " +
//            "SET r2.departure = $departure " +
//            "SET r1.distance = $distance " +
//            "SET r1.number = $number " +
//            "SET r1.flag = $flag " +
//            "SET r1.arrival = $arrival " +
//            "SET r1.departure = $departure " +
//            "RETURN r1")
//    StationDetails relationship (String source1, String source2,String number,String arrival,String departure,String flag, String distance);
//
//    @Query("MATCH (s:Station {coordinates: $source1}), (t:Train {number :$number} " +
//
//            "merge (t)-[r2:towards]->(s)"+
//            "SET r2.arrival = $arrival " +
//            "SET r2.departure = $departure " +
//            "SET r2.number = $number " +
//            "SET r2.flag = $flag " +
//            "RETURN r2")
//    StationDetails relationshipTrainToStation (String source1 ,String number,String arrival,String departure,String flag);
//
//
//





}

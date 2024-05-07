package com.booking.trainticket.AdminAccess.Stations.Repository;

import com.booking.trainticket.AdminAccess.Stations.Pojo.Relationship_properties;
import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.*;

@EnableNeo4jRepositories
public interface TrainInfoRepo extends Neo4jRepository<TrainDetails, Long> {
    @Query("MERGE (t:Train {third_ac: $thirdAc, arrival: $arrival, from_station_code: $fromStationCode, name: $name, zone: $zone, chair_car: $chairCar, first_class: $firstClass, duration_m: $durationM, sleeper: $sleeper, from_station_name: $fromStationName, number: $number, departure: $departure, return_train: $returnTrain, to_station_code: $toStationCode, second_ac: $secondAc, classes: $classes, to_station_name: $toStationName, duration_h: $durationH, type: $type, first_ac: $firstAc, distance: $distance, coordinates_stop: $coordinates_stop, coordinates_AllStop: $coordinates_AllStop, coordinatesStopTime: $coordinatesStopTime, coordinatesAllStopTime: $coordinatesAllStopTime})")
    void createTrain(
            @Param("thirdAc") String thirdAc,
            @Param("arrival") String arrival,
            @Param("fromStationCode") String fromStationCode,
            @Param("name") String name,
            @Param("zone") String zone,
            @Param("chairCar") String chairCar,
            @Param("firstClass") String firstClass,
            @Param("durationM") Integer durationM,
            @Param("sleeper") String sleeper,
            @Param("fromStationName") String fromStationName,
            @Param("number") Long number,
            @Param("departure") String departure,
            @Param("returnTrain") Long returnTrain,
            @Param("toStationCode") String toStationCode,
            @Param("secondAc") String secondAc,
            @Param("classes") String classes,
            @Param("toStationName") String toStationName,
            @Param("durationH") Integer durationH,
            @Param("type") String type,
            @Param("firstAc") String firstAc,
            @Param("distance") Integer distance,
            @Param("coordinates_stop") List<String> coordinates_stop,
            @Param("coordinates_AllStop") List<String>  coordinates_AllStop,
            @Param("coordinatesStopTime") List<String> coordinatesStopTime,
            @Param("coordinatesAllStopTime") List<String> coordinatesAllStopTime
    );
    @Query("Match(t:Train{number:$number} return t.coordinates_stop)")
    String getCoordinates_stop(String number);
    @Query("Match(t:Train{number:$number} return t.coordinates_AllStop)")
    String getCoordinates_AllStop(String number);
    @Query("Match(t:Train{number:$number} return t.coordinatesStopTime)")
    String getCoordinatesStopTime(String number);
    @Query("Match(t:Train{number:$number} return t.coordinatesAllStopTime)")
    String getCoordinatesAllStopTime(String number);

    @Query("MATCH (t:Train {number: $number}), (s:Station {code: $code}) " +
            "MERGE (t)-[r:towards]->(s) " +
            "SET r.arrival = localtime($arrival), " +
            "r.departure = localtime($departure), " +
            "r.stationStops = $count, " +
            "r.flag = $flag, " +
            "r.trainName = $name," +
            "r.type = $type," +
            "r.number = $number " +
            "RETURN r.flag LIMIT 1")
    String relationshipTrainToStation(Long number, String code, LocalTime arrival, LocalTime  departure, Long count, String flag,String name,String type);
    @Query("MATCH (t:Train {number: $number}), (s:Station {code: $code}) " +
            "MERGE (t)-[r:towards]->(s) " +
            "SET r.arrival = localtime($arrival), " +
            "r.departure = localtime($departure), " +
            "r.stationStops = $count, " +
            "r.flag = $flag " +
            "RETURN r.flag LIMIT 1")
    String finalRelationshipTrainToStation(Long number, String code, LocalTime arrival, LocalTime  departure, int count, String flag);

    @Query("MATCH (n:Train {number: $number}) RETURN COUNT(n) = 0")
    boolean findTheTrainExistOrNot(@Param("number") Long number);

    @Query("match (t:Train) return t")
    List<TrainDetails> getAllTrainInfo();
    @Query("match(t:Train) return t.name")
    List<String> getAllTrainNames();
    @Query("match(n:Station{code:$code})<-[r:towards]-(t:Train) where r.flag='green' return t.name;")
    List<String> getAllTrainStopInStation(String code);
//    getAllTrainStopInStationwithTime
    @Query("MATCH (n:Station {code:$code})<-[r:towards {flag: 'green'}]-(t:Train) " +
            " RETURN r")
    List<HashMap<String, Object>> getAllTrainStopInStationwithTime(String code);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train)-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrains(String station1,String station2);
//    [DEMU, SF, Pass, Exp, Mail, JShtb, GR, MEMU, , Raj, Shtb, SKr, Drnt, Klkt, Hyd, Toy, Del]
//    {"getBetweenToStationTrainsWithExpress","getBetweenToStationTrainsWithDEMU","getBetweenToStationTrainsWithSF",
//    "getBetweenToStationTrainsWithPass","getBetweenToStationTrainsWithMail","getBetweenToStationTrainsWithJShtb",
//    "getBetweenToStationTrainsWithGR","getBetweenToStationTrainsWithMEMU","getBetweenToStationTrainsWithRaj",
//    "getBetweenToStationTrainsWithShtb","getBetweenToStationTrainsWithSKr","getBetweenToStationTrainsWithDrnt",
//    "getBetweenToStationTrainsWithKlkt","getBetweenToStationTrainsWithHyd","getBetweenToStationTrainsWithToy",
//    "getBetweenToStationTrainsWithDel"}
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Exp'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithExpress(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'DEMU'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithDEMU(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'SF'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithSF(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Pass'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithPass(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Mail'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithMail(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'JShtb'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithJShtb(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'GR'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithGR(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'MEMU'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithMEMU(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Raj'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithRaj(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Shtb'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithShtb(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'SKr'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithSKr(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Drnt'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithDrnt(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Hyd'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithHyd(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Toy'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithToy(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Del'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithDel(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train{type:'Klkt'})-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) " +
            "RETURN train.number")
    List<Long> getBetweenToStationTrainsWithKlkt(String station1,String station2);
    @Query("match(n:Train{number:$number}) return n")
    TrainDetails getTrainDetails(Long number);
    @Query("MATCH (t:Train)-[:towards]->(s:Station{code:$code}) RETURN distinct t.type ")
    List<String> getAllTypeInStation(String code);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards{flag:'green'}]-(train:Train)-[r2:towards{flag:'green'}]->(arrival:Station {code:$station2}) RETURN distinct train.type")
    List<String> getAllTypeBetweenStation(String station1,String station2);
    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards {flag:'green'}]-(train:Train)-[r2:towards {flag:'green'}]->(arrival:Station {code:$station2})" +
            "WHERE r1.arrival <= LocalTime($time)" +
            "RETURN  distinct train.type")
    List<String>getAllTypeBetweenStationWithTime(String station1,String station2,String time);

    @Query("MATCH (departure:Station {code:$station1})<-[r1:towards {flag:'green'}]-(train:Train{type:$type})-[r2:towards {flag:'green'}]->(arrival:Station {code:$station2})" +
            "RETURN  train limit 1")
    TrainDetails getTrainByType(String station1,String station2,String type);

}

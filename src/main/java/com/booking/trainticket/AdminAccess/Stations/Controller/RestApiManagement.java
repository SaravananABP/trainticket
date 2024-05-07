package com.booking.trainticket.AdminAccess.Stations.Controller;

import com.booking.trainticket.AdminAccess.Stations.Pojo.StationDetails;
import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.booking.trainticket.AdminAccess.Stations.Repository.StationRepo;
import com.booking.trainticket.AdminAccess.Stations.Repository.TrainInfoRepo;
import com.booking.trainticket.AdminAccess.Stations.Service.DbConnectionService;
import com.booking.trainticket.AdminAccess.Stations.Service.NewJsonWriter;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

public class RestApiManagement {
    @Autowired
    StationRepo stationRepo;
    @Autowired
    TrainInfoRepo trainInfoRepo;
    @Autowired
    NewJsonWriter newJsonWriter;
    @Autowired
    DbConnectionService dbConnectionService;

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km

        return distance;
    }

//    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/createStation")
    @PreAuthorize("hasRole('ADMIN')")//total size 8989
    public String createStation() {
        try {
            // Specify the path to your JSON file
            String filePath = "src/main/resources/templates/Stations.json";

            // Read JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filePath));
            int count = 0;
            for (JsonNode value : rootNode.path("features")) {
                System.out.println(count);
                count += 1;
                JsonNode geometryNode = value.path("geometry");
                List<Double> coordinates = List.of((double) 0, (double) 0);
                Double longitude = (double)0;
                Double latitude = (double)0;

                if (!geometryNode.isNull() && geometryNode.has("coordinates")) {
                    JsonNode coordinatesNode = geometryNode.path("coordinates");
                    if (coordinatesNode.isArray() && coordinatesNode.size() >= 2) {
                        longitude = coordinatesNode.get(0).asDouble();
                        latitude = coordinatesNode.get(1).asDouble();
                        coordinates = List.of(longitude,latitude);
                    }
                }
                String state = value.path("properties").path("state").asText();
                String code = value.path("properties").path("code").asText();
                String name = value.path("properties").path("name").asText();
                String zone = value.path("properties").path("zone").asText();
                String address = value.path("properties").path("address").asText();
                StationDetails stationDetails=new StationDetails();
                stationDetails.setState(state);
                stationDetails.setCode(code);
                stationDetails.setAddress(address);
                stationDetails.setName(name);
                stationDetails.setZone(zone);
                stationDetails.setCoordinates(coordinates);
                stationDetails.setLatitude(latitude);
                stationDetails.setLongitude(longitude);
                stationRepo.save(stationDetails);

//                stationRepo.createStation(coordinates, state, code, name, zone, address, longitude, latitude);
            }
            System.out.println("created successfully!!!");
            return "Stations created successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to create stations. Error: " + e.getMessage();
        }
    }

    @PostMapping("/relationship")

    public String relationship() throws IOException {
        String filePath = "src/main/resources/templates/trains.json";

        // Read JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        for (JsonNode value : rootNode.path("features")) {
            int count = 0;
            JsonNode geometryNode = value.path("geometry");

            if (geometryNode != null && geometryNode.has("coordinates")) {
                JsonNode coordinatesNode = geometryNode.path("coordinates");
                if (coordinatesNode.isArray() && coordinatesNode.size() >= 2) {
                    for (int i = 0; i < coordinatesNode.size() - 1; i++) {
//                        List<String> pair = new ArrayList<>();
                        Double longitude1 = coordinatesNode.get(i).get(0).asDouble();
                        Double latitude1 = coordinatesNode.get(i).get(1).asDouble();
                        Double longitude2 = coordinatesNode.get(i + 1).get(0).asDouble();
                        Double latitude2 = coordinatesNode.get(i + 1).get(1).asDouble();
//                        pair.add(longitude1+", "+latitude1);
//                        pair.add(longitude2+", "+latitude2);
//                        innerList.add(pair);
                        double distance = calculateDistance(latitude1, longitude1, latitude2, longitude2);
//                        if (flag) {
//                            stationRepo.Make_source_to_destination(longitude1 + ", " + latitude1, longitude2 + ", " + latitude2, String.valueOf(distance));
//                            flag=false;
//                        }else{
//                            stationRepo.Make_destination_to_source(longitude1 + ", " + latitude1, longitude2 + ", " + latitude2, String.valueOf(distance));
//                            flag=true;
//                        }
//                        *****stationRepo.sampleCase(longitude1 + ", " + latitude1, longitude2 + ", " + latitude2, String.valueOf(distance));
                        System.out.println(count += 1);
//                        List<String> nextPair = new ArrayList<>();
//                        nextPair.add(String.valueOf(longitude2));
//                        nextPair.add(String.valueOf(latitude2));
//                        innerList.add(nextPair);
//                        System.out.println("******"+String.valueOf(pair.toString()));
                    }
                }
            }
        }
        return null;
    }
//    @RequestMapping(method = RequestMethod.GET,path = "/getAllTrainDetails")
//    public String allvalues(){
//        return String.valueOf(trainInfoRepo.findAll());
//
//    }
    @GetMapping("/getAllTrainDetails")

    public List<TrainDetails> find(){
        return trainInfoRepo.getAllTrainInfo();

    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/final/writeJsonToFile")
    @PreAuthorize("hasRole('ADMIN')")
    public JSONObject writeJsonToFile1() {
        try {
            JSONObject finalJsonResult = newJsonWriter.processFinalResult();
            newJsonWriter.writeJsonToFile(finalJsonResult);
            return finalJsonResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    @PostMapping("/create/train")
    @PreAuthorize("hasRole('ADMIN')")
    public String createTrain() throws IOException {
        String filePath = "C:\\Users\\saravanan.ap\\Downloads\\trainticket\\trainticket\\schedules_output.json";

        // Read JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(filePath));
//        "11041","11043","11044","12253","12254","56041",
        Long[] trainNumber={16057L,16058L,43501L,43506L,11041L,11043L, 11044L,12253L,12254L,56041L};


        for (JsonNode value : rootNode.path("rawData")) {
            if (value != null ) {
                    String thirdAc = value.path("third_ac").asText();
                    String  arrival = value.path("arrival").asText();
                    String fromStationCode = value.path("from_station_code").asText();
                    String name = value.path("name").asText();
                    String zone = value.path("zone").asText();
                    String chairCar = value.path("chair_car").asText();
                    String firstClass = value.path("first_class").asText();
                    Integer durationM = value.path("duration_m").asInt();
                    String sleeper = value.path("sleeper").asText();
                    String fromStationName = value.path("from_station_name").asText();
                    Long number = value.path("number").asLong();
                    String  departure = value.path("departure").asText();
                    Long returnTrain = value.path("return_train").asLong();
                    String toStationCode = value.path("to_station_code").asText();
                    String secondAc = value.path("second_ac").asText();
                    String classes = value.path("classes").asText();
                    String toStationName = value.path("to_station_name").asText();
                    Integer durationH = value.path("duration_h").asInt();
                    String type = value.path("type").asText();
                    String firstAc = value.path("first_ac").asText();
                    Integer distance = value.path("distance").asInt();
                    List<String> coordinates_stop=dbConnectionService.jsonToListStr(value.path("coordinates_stop"));

                    List<String> coordinates_AllStop=dbConnectionService.jsonToListStr(value.path("coordinates_AllStop"));
//                List<String> coordinatesStopTime=dbConnectionService.stringToList(value.path("coordinatesStopTime").toString());
//
//                List<String> coordinatesAllStopTime=dbConnectionService.stringToList(value.path("coordinatesAllStopTime").toString());


                // Use TypeReference to specify the type of the list
//                    List<Map<String, String>> coordinatesStopTime =objectMapper.readValue(value.path("coordinatesStopTime").toString(), new TypeReference<List<Map<String, String>>>() {});
//                    List<Map<String, String>> coordinatesAllStopTime =objectMapper.readValue(value.path("coordinatesAllStopTime").toString(), new TypeReference<List<Map<String, String>>>() {});
                    List<String> coordinatesStopTime=dbConnectionService.stringToList(value.path("coordinatesStopTime").toString());
                    List<String> coordinatesAllStopTime=dbConnectionService.stringToList(value.path("coordinatesAllStopTime").toString());

//                    if(Arrays.asList(trainNumber).contains(number)) {
                if(dbConnectionService.tamilNaduJunction(coordinates_stop)){
                        TrainDetails train = new TrainDetails();
                        train.setThird_ac(thirdAc);
                        if(!arrival.contains("None")){
//                            train.setArrival(LocalTime.parse(arrival));
                            train.setArrival((arrival));
                        }

                        train.setFrom_station_code(fromStationCode);
                        train.setName(name);
                        train.setZone(zone);
                        train.setChair_car(chairCar);
                        train.setFirst_class(firstClass);
                        train.setDuration_m(durationM);
                        train.setSleeper(sleeper);
                        train.setFrom_station_name(fromStationName);
                        train.setNumber(number);
                        if(!departure.contains("None")){
//                            train.setDeparture(LocalTime.parse(departure));
                            train.setDeparture((departure));
                        }
                        train.setReturn_train(returnTrain);
                        train.setTo_station_code(toStationCode);
                        train.setSecond_ac(secondAc);
                        train.setClasses(classes);
                        train.setTo_station_name(toStationName);
                        train.setDuration_h(durationH);
                        train.setType(type);
                        train.setFirst_ac(firstAc);
                        train.setDistance(distance);
                        // Set coordinates
                        train.setCoordinates_stop(coordinates_stop);
                        train.setCoordinates_AllStop(coordinates_AllStop);
                        train.setCoordinatesStopTime(coordinatesStopTime);
                        train.setCoordinatesAllStopTime(coordinatesAllStopTime);

                        boolean check=trainInfoRepo.findTheTrainExistOrNot(number);
                        // Save the train details using the service
                        if(check){
                            trainInfoRepo.createTrain(
                                    thirdAc, arrival, fromStationCode, name, zone, chairCar, firstClass, durationM, sleeper, fromStationName,
                                    number, departure, returnTrain, toStationCode, secondAc, classes, toStationName, durationH, type, firstAc,
                                    distance, coordinates_stop, coordinates_AllStop, coordinatesStopTime, coordinatesAllStopTime
                            );
//                            trainInfoRepo.save(train);
                        }
//                        dbConnectionService.finalRelationShipEstablishment(number,coordinates_stop, coordinates_AllStop, coordinatesStopTime, coordinatesAllStopTime);
//                        List<String> coordinates_stop1=dbConnectionService.jsonToList(value.path("coordinatesStopTime"));
//                        List<String> coordinates_AllStop1=dbConnectionService.jsonToList(value.path("coordinatesAllStopTime"));
                        coordinates_stop=new ArrayList<>();
                        coordinates_stop=dbConnectionService.stringsToList(value.path("coordinates_stop").toString());
                        coordinates_AllStop=new ArrayList<>();
                        coordinates_AllStop=dbConnectionService.stringsToList(value.path("coordinates_AllStop").toString());
                        dbConnectionService.finalRelationShipEstablishment(number,coordinates_stop,coordinates_AllStop,coordinatesStopTime,coordinatesAllStopTime,arrival,departure,name,type);

                    }

            }
        }
        return "Train details created successfully.";
    }


//    @PostMapping("/create/train")
//    public String CreateTrain() throws IOException {
//        String filePath = "C:\\Users\\saravanan.ap\\Downloads\\trainticket\\trainticket\\schedules_output.json";
//
//        // Read JSON file
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(new File(filePath));
//
//        for (JsonNode value : rootNode.path("rawData")) {
//            if (value != null && value.has("coordinates")) {
//                if (value.isArray()) {
//                    String thirdAc = value.path("properties").path("third_ac").asText();
//                    String arrival = value.path("properties").path("arrival").asText();
//                    String fromStationCode = value.path("properties").path("from_station_code").asText();
//                    String name = value.path("properties").path("name").asText();
//                    String zone = value.path("properties").path("zone").asText();
//                    String chairCar = value.path("properties").path("chair_car").asText();
//                    String firstClass = value.path("properties").path("first_class").asText();
//                    String durationM = value.path("properties").path("duration_m").asText();
//                    String sleeper = value.path("properties").path("sleeper").asText();
//                    String fromStationName = value.path("properties").path("from_station_name").asText();
//                    String number = value.path("properties").path("number").asText();
//                    String departure = value.path("properties").path("departure").asText();
//                    String returnTrain = value.path("properties").path("return_train").asText();
//                    String toStationCode = value.path("properties").path("to_station_code").asText();
//                    String secondAc = value.path("properties").path("second_ac").asText();
//                    String classes = value.path("properties").path("classes").asText();
//                    String toStationName = value.path("properties").path("to_station_name").asText();
//                    String durationH = value.path("properties").path("duration_h").asText();
//                    String type = value.path("properties").path("type").asText();
//                    String firstAc = value.path("properties").path("first_ac").asText();
//                    String distance = value.path("properties").path("distance").asText();
//                    TrainDetails train=new TrainDetails();
//
////                    String third_ac;
////                    String arrival;
////                    String from_station_code;
////                    String name;
////                    String zone;
////                    String chair_car;
////                    String first_class;
////                    String duration_m;
////                    String sleeper;
////                    String from_station_name;
////                    String number;
////                    String departure;
////                    String return_train;
////                    String to_station_code;
////                    String second_ac;
////                    String classes;
////                    String to_station_name;
////                    String duration_h;
////                    String type;
////                    String first_ac;
////                    String distance;
//
//
////                    List coordinates_stop ;
////                    List coordinates_AllStop ;
////                    List coordinatesStopTime ;
////                    List coordinatesAllStopTime ;
//                    String coordinates_stop =value.path("coordinates_stop").asText();
//                    String coordinates_AllStop =value.path("coordinates_AllStop").asText();
//                    String coordinatesStopTime =value.path("coordinatesStopTime").asText();
//                    String coordinatesAllStopTime =value.path("coordinatesAllStopTime").asText();
//                }
//            }
//
//            if (value != null && value.has("coordinates")) {
//                JsonNode coordinatesNode = value.path("coordinates");
//                if (coordinatesNode.isArray() && coordinatesNode.size() >= 2) {
//                    for (int i = 0; i < coordinatesNode.size() - 1; i++) {
////                        List<String> pair = new ArrayList<>();
//                        Double longitude1 = coordinatesNode.get(i).get(0).asDouble();
//                        Double latitude1 = coordinatesNode.get(i).get(1).asDouble();
//                        Double longitude2 = coordinatesNode.get(i + 1).get(0).asDouble();
//                        Double latitude2 = coordinatesNode.get(i + 1).get(1).asDouble();
////                        pair.add(longitude1+", "+latitude1);
////                        pair.add(longitude2+", "+latitude2);
////                        innerList.add(pair);
//                        double distance = calculateDistance(latitude1, longitude1, latitude2, longitude2);
////                        if (flag) {
////                            stationRepo.Make_source_to_destination(longitude1 + ", " + latitude1, longitude2 + ", " + latitude2, String.valueOf(distance));
////                            flag=false;
////                        }else{
////                            stationRepo.Make_destination_to_source(longitude1 + ", " + latitude1, longitude2 + ", " + latitude2, String.valueOf(distance));
////                            flag=true;
////                        }
//                        stationRepo.sampleCase(longitude1 + ", " + latitude1, longitude2 + ", " + latitude2, String.valueOf(distance));
//                        System.out.println(count += 1);
////                        List<String> nextPair = new ArrayList<>();
////                        nextPair.add(String.valueOf(longitude2));
////                        nextPair.add(String.valueOf(latitude2));
////                        innerList.add(nextPair);
////                        System.out.println("******"+String.valueOf(pair.toString()));
//                    }
//                }
//            }
//        }
//        return null;
//    }

}
//    @GetMapping("/writeJsonToFile")
//    public JSONObject writeJsonToFile() throws IOException {
//        String filePath = "src/main/resources/templates/schedules.json";
//        String trainsPath = "src/main/resources/templates/trains.json";
//
//        // Read JSON file
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(new File(filePath));
//        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));
//
//
//        JSONObject schedulingTrainDetails = new JSONObject();
//        HashMap<String, List<String>> coordinates_stop = new HashMap<>();
//        HashMap<String, List<String>> coordinates_allStop = new HashMap<>();
//        HashMap<String, List<HashMap<String, HashMap>>> coordinatesStopTime = new HashMap<>();
//        HashMap<String, List<HashMap<String, HashMap>>> coordinatesAllStopTime = new HashMap<>();
//        int count = 0;
//        if (rootNode.isArray()) {
//            for (JsonNode i : rootNode) {
//
//                String departure = i.path("departure").asText();
//                String arrival = i.path("arrival").asText();
//                String trainName = i.path("train_name").asText();
//                String train_number = i.path("train_number").asText();
//                String station_name = i.path("station_name").asText();
//                String station_code = i.path("station_code").asText();
//                if (!coordinates_allStop.containsKey(train_number)) {
//                    coordinates_allStop.put(train_number, new ArrayList<>());
//
//                }
//                if (!coordinatesAllStopTime.containsKey(train_number)) {
//                    coordinatesAllStopTime.put(train_number, new ArrayList<>());
//
//                }
//                coordinates_allStop.get(train_number).add(station_name);
//
//                HashMap stopDetails = new HashMap<>();
//                stopDetails.put("departure", departure);
//                stopDetails.put("arrival", arrival);
//                HashMap<String, HashMap> stationDetails = new HashMap<>();
//                stationDetails.put(station_name, stopDetails);
//                stationDetails.put(station_name, stopDetails);
//                coordinatesAllStopTime
//                        .computeIfAbsent(train_number, k -> new ArrayList<>())
//                        .add(stationDetails);
//                if (!departure.contains(arrival)) {
//                    if (!coordinates_stop.containsKey(train_number)) {
//                        coordinates_stop.put(train_number, new ArrayList<>());
//
//                    }
//                    if (!coordinatesStopTime.containsKey(train_number)) {
//                        coordinatesStopTime.put(train_number, new ArrayList<>());
//
//                    }
//                    coordinates_stop.get(train_number).add(station_name);
//
//
//                    HashMap stopDetail = new HashMap<>();
//                    stopDetail.put("departure", departure);
//                    stopDetail.put("arrival", arrival);
//                    HashMap<String, HashMap> stationDetail = new HashMap<>();
//                    stationDetail.put(station_name, stopDetail);
//                    coordinatesStopTime
//                            .computeIfAbsent(train_number, k -> new ArrayList<>())
//                            .add(stationDetail);
//
//
//                    schedulingTrainDetails.put("coordinatesStopTime", coordinatesStopTime);
//                    schedulingTrainDetails.put("coordinates_stop", coordinates_stop);
//
//
//                }
//                schedulingTrainDetails.put("coordinates_AllStop", coordinates_allStop);
//                schedulingTrainDetails.put("coordinatesAllStopTime", coordinatesAllStopTime);
//
//            }
//        }
//
//
//        JSONObject finalResult = new JSONObject();
//        List allData = new ArrayList<>();
//        for (JsonNode value : trainPath.path("features")) {
//
//            JsonNode properties = value.path("properties");
//
//            String third_ac = properties.path("third_ac").asText();
//            String arrival = properties.path("arrival").asText();
//            String from_station_code = properties.path("from_station_code").asText();
//            String name = properties.path("name").asText();
//            String zone = properties.path("zone").asText();
//            String chair_car = properties.path("chair_car").asText();
//            String first_class = properties.path("first_class").asText();
//            String duration_m = properties.path("duration_m").asText();
//            String sleeper = properties.path("sleeper").asText();
//            String from_station_name = properties.path("from_station_name").asText();
//            String number = properties.path("number").asText();
//            String departure = properties.path("departure").asText();
//            String return_train = properties.path("return_train").asText();
//            String to_station_code = properties.path("to_station_code").asText();
//            String second_ac = properties.path("second_ac").asText();
//            String classes = properties.path("classes").asText();
//            String to_station_name = properties.path("to_station_name").asText();
//            String duration_h = properties.path("duration_h").asText();
//            String type = properties.path("type").asText();
//            String first_ac = properties.path("first_ac").asText();
//            String distance = properties.path("distance").asText();
//
//            //            "third_ac": 0, "arrival": "12:15:00", "from_station_code": "JAT", "name": "Jammu Tawi Udhampur Special", "zone": "NR", "chair_car": 0, "first_class": 0, "duration_m": 35, "sleeper": 0, "from_station_name": "JAMMU TAWI", "number": "04601", "departure": "10:40:00", "return_train": "04602", "to_station_code": "UHP", "second_ac": 0, "classes": "", "to_station_name": "UDHAMPUR", "duration_h": 1, "type": "DEMU", "first_ac": 0, "distance"
//            JSONObject data = new JSONObject();
//            data.put("third_ac", third_ac);
//            data.put("arrival", arrival);
//            data.put("from_station_code", from_station_code);
//            data.put("name", name);
//            data.put("zone", zone);
//            data.put("chair_car", chair_car);
//            data.put("first_class", first_class);
//            data.put("duration_m", duration_m);
//            data.put("sleeper", sleeper);
//            data.put("from_station_name", from_station_name);
//            data.put("number", number);
//            data.put("departure", departure);
//            data.put("return_train", return_train);
//            data.put("to_station_code", to_station_code);
//            data.put("second_ac", second_ac);
//            data.put("classes", classes);
//            data.put("to_station_name", to_station_name);
//            data.put("duration_h", duration_h);
//            data.put("type", type);
//            data.put("first_ac", first_ac);
//            data.put("distance", distance);
//            JsonNode jsonObject = objectMapper.readTree(String.valueOf(schedulingTrainDetails));
//            String all_coordinates = jsonObject.path("coordinates_stop").path(number).toString();
//            ;
//            data.put("coordinates_stop", all_coordinates);
//            String allStops_coordinates = jsonObject.path("coordinates_AllStop").path(number).toString();
//            ;
//            data.put("coordinates_AllStop", allStops_coordinates);
//            String track_coordinates = jsonObject.path("coordinatesStopTime").path(number).toString();
//            data.put("coordinatesStopTime", track_coordinates);
//            String track_all_coordinates = jsonObject.path("coordinatesStopTime").path(number).toString();
//            data.put("coordinatesStopTime", track_all_coordinates);
//            String track_allStop_coordinates = jsonObject.path("coordinatesAllStopTime").path(number).toString();
//            data.put("coordinatesAllStopTime", track_allStop_coordinates);
//
//            allData.add(data);
//            System.out.println(allData.toString());
//        }
//        finalResult.put("rawData", allData);
//        System.out.println(finalResult);
//
//        // Write JSON data to a file
//        ObjectMapper objectMapper1 = new ObjectMapper();
//        try {
//            objectMapper1.writeValue(new File("schedules_output.json"), finalResult);
//            System.out.println("completed");
//            return finalResult;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return finalResult;
//        }
//    }

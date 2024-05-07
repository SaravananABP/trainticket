package com.booking.trainticket.AdminAccess.Stations.Service;

import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class NewJsonWriter {


//    public JSONObject processSchedulingTrainDetails() throws IOException {
//        String filePath = "src/main/resources/templates/schedules.json";
//        String trainsPath = "src/main/resources/templates/trains.json";
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(new File(filePath));
//        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));
//
//        JSONObject schedulingTrainDetails = new JSONObject();
//        HashMap<String, List<String>> coordinates_stop = new HashMap<>();
//        HashMap<String,List<String>> coordinates_allStop=new HashMap<>();
//        HashMap<String ,List<HashMap<String,HashMap>>> coordinatesStopTime=new HashMap<>();
//        HashMap<String ,List<HashMap<String,HashMap>>> coordinatesAllStopTime=new HashMap<>();
//
//        if (rootNode.isArray()){
//            for(JsonNode i:rootNode){
//
//                String departure=i.path("departure").asText();
//                String arrival=i.path("arrival").asText();
//                String trainName=i.path("train_name").asText();
//                String train_number=i.path("train_number").asText();
//                String station_name=i.path("station_name").asText();
//                String station_code=i.path("station_code").asText();
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
//                if(!departure.contains(arrival)){
//                    stopDetails.put("flag","red");
//                }else{
//                    stopDetails.put("flag","green");
//                }
//                stopDetails.put("departure", departure);
//                stopDetails.put("arrival", arrival);
//                HashMap<String, HashMap> stationDetails = new HashMap<>();
//                stationDetails.put(station_name, stopDetails);
//                stationDetails.put(station_name, stopDetails);
//
//                coordinatesAllStopTime
//                        .computeIfAbsent(train_number, k -> new ArrayList<>())
//                        .add(stationDetails);
//                if(!departure.contains(arrival)){
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
//                    stopDetail.put("flag","green");
//                    HashMap<String, HashMap> stationDetail = new HashMap<>();
//                    stationDetail.put(station_name, stopDetail);
//                    coordinatesStopTime
//                            .computeIfAbsent(train_number, k -> new ArrayList<>())
//                            .add(stationDetail);
//
//
//                    schedulingTrainDetails.put("coordinatesStopTime",coordinatesStopTime);
//                    schedulingTrainDetails.put("coordinates_stop",coordinates_stop);
//
//
//                }
//                schedulingTrainDetails.put("coordinates_AllStop",coordinates_allStop);
//                schedulingTrainDetails.put("coordinatesAllStopTime",coordinatesAllStopTime);
//
//            }
//        }
//        return schedulingTrainDetails;
//    }
//
//    public JSONObject processFinalResult(JSONObject schedulingTrainDetails) throws IOException {
//        String trainsPath = "src/main/resources/templates/trains.json";
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));
//
//        JSONObject finalResult = new JSONObject();
//        List<JSONObject> allData = new ArrayList<>();
//        int count=0;
//        for (JsonNode value : trainPath.path("features")) {
//            JsonNode properties = value.path("properties");
//            JSONObject data = new JSONObject();
//
//            // Extract all properties
//            String thirdAc = properties.path("third_ac").asText();
//            String arrival = properties.path("arrival").asText();
//            String fromStationCode = properties.path("from_station_code").asText();
//            String name = properties.path("name").asText();
//            String zone = properties.path("zone").asText();
//            String chairCar = properties.path("chair_car").asText();
//            String firstClass = properties.path("first_class").asText();
//            String durationM = properties.path("duration_m").asText();
//            String sleeper = properties.path("sleeper").asText();
//            String fromStationName = properties.path("from_station_name").asText();
//            String number = properties.path("number").asText();
//            String departure = properties.path("departure").asText();
//            String returnTrain = properties.path("return_train").asText();
//            String toStationCode = properties.path("to_station_code").asText();
//            String secondAc = properties.path("second_ac").asText();
//            String classes = properties.path("classes").asText();
//            String toStationName = properties.path("to_station_name").asText();
//            String durationH = properties.path("duration_h").asText();
//            String type = properties.path("type").asText();
//            String firstAc = properties.path("first_ac").asText();
//            String distance = properties.path("distance").asText();
//
//            // Add extracted properties to JSONObject
//            data.put("third_ac", thirdAc);
//            data.put("arrival", arrival);
//            data.put("from_station_code", fromStationCode);
//            data.put("name", name);
//            data.put("zone", zone);
//            data.put("chair_car", chairCar);
//            data.put("first_class", firstClass);
//            data.put("duration_m", durationM);
//            data.put("sleeper", sleeper);
//            data.put("from_station_name", fromStationName);
//            data.put("number", number);
//            data.put("departure", departure);
//            data.put("return_train", returnTrain);
//            data.put("to_station_code", toStationCode);
//            data.put("second_ac", secondAc);
//            data.put("classes", classes);
//            data.put("to_station_name", toStationName);
//            data.put("duration_h", durationH);
//            data.put("type", type);
//            data.put("first_ac", firstAc);
//            data.put("distance", distance);
//
//            // Add all_coordinates and track_coordinates
//            JsonNode jsonObject = objectMapper.readTree(String.valueOf(schedulingTrainDetails));
//            String all_coordinates=jsonObject.path("coordinates_stop").path(number).toString();;
//            data.put("coordinates_stop", all_coordinates);
//            String allStops_coordinates=jsonObject.path("coordinates_AllStop").path(number).toString();;
//            data.put("coordinates_AllStop", allStops_coordinates);
//            String track_coordinates=jsonObject.path("coordinatesStopTime").path(number).toString();
//            data.put("coordinatesStopTime",track_coordinates);
//            String track_allStop_coordinates=jsonObject.path("coordinatesAllStopTime").path(number).toString();
//            data.put("coordinatesAllStopTime",track_allStop_coordinates);
//            count+=1;
//            System.out.println(count);
//            allData.add(data);
//        }
//
//        finalResult.put("rawData", allData);
//        System.out.println("Over");
//        return finalResult;
//    }

    List<String > typeTrain=new ArrayList<>();

private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    DbConnectionService dbConnectionService;

    public final HashMap<String, HashMap> processData;

    {
        try {
            processData = processSchedulingTrainDetails();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, HashMap> processSchedulingTrainDetails() throws IOException {
        String filePath = "src/main/resources/templates/schedules.json";
        String trainsPath = "src/main/resources/templates/trains.json";

        JsonNode rootNode = objectMapper.readTree(new File(filePath));
        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));

        JSONObject schedulingTrainDetails = new JSONObject();
        HashMap<String, List<String>> coordinates_stop = new HashMap<>();
        HashMap<String, List<String>> coordinates_allStop = new HashMap<>();
        HashMap<String, List<HashMap<String, HashMap>>> coordinatesStopTime = new HashMap<>();
        HashMap<String, List<HashMap<String, HashMap>>> coordinatesAllStopTime = new HashMap<>();

        if (rootNode.isArray()) {
            for (JsonNode i : rootNode) {

                String departure = i.path("departure").asText();
                String arrival = i.path("arrival").asText();
                String trainName = i.path("train_name").asText();
                String train_number = i.path("train_number").asText();
                String station_name = i.path("station_name").asText();
                String station_code = i.path("station_code").asText();
                if (!coordinates_allStop.containsKey(train_number)) {
                    coordinates_allStop.put(train_number, new ArrayList<>());
                }
                if (!coordinatesAllStopTime.containsKey(train_number)) {
                    coordinatesAllStopTime.put(train_number, new ArrayList<>());
                }
                coordinates_allStop.get(train_number).add(station_code);

                HashMap stopDetails = new HashMap<>();
                stopDetails.put("departure", departure);
                stopDetails.put("arrival", arrival);
                if (!departure.contains(arrival)) {
                    stopDetails.put("flag", "green");
                } else {
                    stopDetails.put("flag", "red");
                }
                HashMap<String, HashMap> stationDetails = new HashMap<>();
                stationDetails.put(station_code, stopDetails);

                coordinatesAllStopTime
                        .computeIfAbsent(train_number, k -> new ArrayList<>())
                        .add(stationDetails);
                if (!departure.contains(arrival)) {
                    if (!coordinates_stop.containsKey(train_number)) {
                        coordinates_stop.put(train_number, new ArrayList<>());
                    }
                    if (!coordinatesStopTime.containsKey(train_number)) {
                        coordinatesStopTime.put(train_number, new ArrayList<>());
                    }
                    coordinates_stop.get(train_number).add(station_code);

                    HashMap stopDetail = new HashMap<>();
                    stopDetail.put("departure", departure);
                    stopDetail.put("arrival", arrival);
                    stopDetail.put("flag", "green");
                    HashMap<String, HashMap> stationDetail = new HashMap<>();
                    stationDetail.put(station_code, stopDetail);
                    coordinatesStopTime
                            .computeIfAbsent(train_number, k -> new ArrayList<>())
                            .add(stationDetail);
                }
            }
        }
        schedulingTrainDetails.put("coordinatesStopTime", coordinatesStopTime);
        schedulingTrainDetails.put("coordinates_stop", coordinates_stop);
        schedulingTrainDetails.put("coordinates_AllStop", coordinates_allStop);
        schedulingTrainDetails.put("coordinatesAllStopTime", coordinatesAllStopTime);

        return schedulingTrainDetails;
    }

    public JSONObject processFinalResult() throws IOException {
        String trainsPath = "src/main/resources/templates/trains.json";
        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));

        JSONObject finalResult = new JSONObject();
        List<JSONObject> allData = new ArrayList<>();
//        JSONObject allData=new JSONObject();
        int count = 0;
        typeTrain.add("TRT");
        for (JsonNode value : trainPath.path("features")) {
            JSONObject data = processTrainNode(value);
//            allData.put(value.path("properties").path("number").asText().trim(),data);
            allData.add(data);
            count += 1;
            System.out.println(count);
        }

        finalResult.put("rawData", allData);
        return finalResult;
    }
//    private final HashMap<String, HashMap> processData;
//
//    {
//        try {
//            processData = processSchedulingTrainDetails();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private JSONObject processTrainNode(JsonNode value) throws IOException {

        String trainNumber = value.path("properties").path("number").asText();
        JSONObject data = new JSONObject();

        // Extract data from train node
        String thirdAc = value.path("properties").path("third_ac").asText();
        String arrival = value.path("properties").path("arrival").asText();
        String fromStationCode = value.path("properties").path("from_station_code").asText();
        String name = value.path("properties").path("name").asText();
        String zone = value.path("properties").path("zone").asText();
        String chairCar = value.path("properties").path("chair_car").asText();
        String firstClass = value.path("properties").path("first_class").asText();
        String durationM = value.path("properties").path("duration_m").asText();
        String sleeper = value.path("properties").path("sleeper").asText();
        String fromStationName = value.path("properties").path("from_station_name").asText();
        String number = value.path("properties").path("number").asText();
        String departure = value.path("properties").path("departure").asText();
        String returnTrain = value.path("properties").path("return_train").asText();
        String toStationCode = value.path("properties").path("to_station_code").asText();
        String secondAc = value.path("properties").path("second_ac").asText();
        String classes = value.path("properties").path("classes").asText();
        String toStationName = value.path("properties").path("to_station_name").asText();
        String durationH = value.path("properties").path("duration_h").asText();
        String type = value.path("properties").path("type").asText();
        String firstAc = value.path("properties").path("first_ac").asText();
        String distance = value.path("properties").path("distance").asText();

        // Combine more data...


        // Use ObjectMapper to convert extracted data to JSONObject
        data.put("third_ac", thirdAc);
        data.put("arrival", arrival);
        data.put("from_station_code", fromStationCode);
        data.put("name", name);
        data.put("zone", zone);
        data.put("chair_car", chairCar);
        data.put("first_class", firstClass);
        data.put("duration_m", durationM);
        data.put("sleeper", sleeper);
        data.put("from_station_name", fromStationName);
        data.put("number", number);
        data.put("departure", departure);
        data.put("return_train", returnTrain);
        data.put("to_station_code", toStationCode);
        data.put("second_ac", secondAc);
        data.put("classes", classes);
        data.put("to_station_name", toStationName);
        data.put("duration_h", durationH);
        data.put("type", type);
        data.put("first_ac", firstAc);
        data.put("distance", distance);
        data.put("coordinates_AllStop", processData.get("coordinates_AllStop").get(number));
        data.put("coordinates_stop", processData.get("coordinates_stop").get(number));
        data.put("coordinatesStopTime", processData.get("coordinatesStopTime").get(number));
        data.put("coordinatesAllStopTime", processData.get("coordinatesAllStopTime").get(number));

        if(typeTrain.contains(fromStationCode)) {
            typeTrain.add(name);
        }
        return data;
    }

    public void writeJsonToFile(JSONObject finalResult) throws IOException {

        objectMapper.writeValue(new File("schedules_output.json"), finalResult);
    }

//    public HashMap<String, HashMap> processSchedulingTrainDetails() throws IOException {
//        String filePath = "src/main/resources/templates/schedules.json";
//        String trainsPath = "src/main/resources/templates/trains.json";
//
//        JsonNode rootNode = objectMapper.readTree(new File(filePath));
//        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));
//
//        JSONObject schedulingTrainDetails = new JSONObject();
//        HashMap<String, List<String>> coordinates_stop = new HashMap<>();
//        HashMap<String, List<String>> coordinates_allStop = new HashMap<>();
//        HashMap<String, List<HashMap<String, HashMap>>> coordinatesStopTime = new HashMap<>();
//        HashMap<String, List<HashMap<String, HashMap>>> coordinatesAllStopTime = new HashMap<>();
//
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
//                }
//                if (!coordinatesAllStopTime.containsKey(train_number)) {
//                    coordinatesAllStopTime.put(train_number, new ArrayList<>());
//                }
//                coordinates_allStop.get(train_number).add(station_code);
//
//                HashMap stopDetails = new HashMap<>();
//                stopDetails.put("departure", departure);
//                stopDetails.put("arrival", arrival);
//                if (!departure.contains(arrival)) {
//                    stopDetails.put("flag", "green");
//                } else {
//                    stopDetails.put("flag", "red");
//                }
//                HashMap<String, HashMap> stationDetails = new HashMap<>();
//                stationDetails.put(station_code, stopDetails);
//
//                coordinatesAllStopTime
//                        .computeIfAbsent(train_number, k -> new ArrayList<>())
//                        .add(stationDetails);
//                if (!departure.contains(arrival)) {
//                    if (!coordinates_stop.containsKey(train_number)) {
//                        coordinates_stop.put(train_number, new ArrayList<>());
//                    }
//                    if (!coordinatesStopTime.containsKey(train_number)) {
//                        coordinatesStopTime.put(train_number, new ArrayList<>());
//                    }
//                    coordinates_stop.get(train_number).add(station_code);
//
//                    HashMap stopDetail = new HashMap<>();
//                    stopDetail.put("departure", departure);
//                    stopDetail.put("arrival", arrival);
//                    stopDetail.put("flag", "green");
//                    HashMap<String, HashMap> stationDetail = new HashMap<>();
//                    stationDetail.put(station_code, stopDetail);
//                    coordinatesStopTime
//                            .computeIfAbsent(train_number, k -> new ArrayList<>())
//                            .add(stationDetail);
//                }
//            }
//        }
//        schedulingTrainDetails.put("coordinatesStopTime", coordinatesStopTime);
//        schedulingTrainDetails.put("coordinates_stop", coordinates_stop);
//        schedulingTrainDetails.put("coordinates_AllStop", coordinates_allStop);
//        schedulingTrainDetails.put("coordinatesAllStopTime", coordinatesAllStopTime);
//
//        return schedulingTrainDetails;
//    }




}
//    public HashMap<String,HashMap> processSchedulingTrainDetails() throws IOException {
//        String filePath = "src/main/resources/templates/schedules.json";
//        String trainsPath = "src/main/resources/templates/trains.json";
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(new File(filePath));
//        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));
//
//        JSONObject schedulingTrainDetails = new JSONObject();
//        HashMap<String, List<String>> coordinates_stop = new HashMap<>();
//        HashMap<String,List<String>> coordinates_allStop=new HashMap<>();
//        HashMap<String ,List<HashMap<String,HashMap>>> coordinatesStopTime=new HashMap<>();
//        HashMap<String ,List<HashMap<String,HashMap>>> coordinatesAllStopTime=new HashMap<>();
//
//        if (rootNode.isArray()){
//            for(JsonNode i:rootNode){
//
//                String departure=i.path("departure").asText();
//                String arrival=i.path("arrival").asText();
//                String trainName=i.path("train_name").asText();
//                String train_number=i.path("train_number").asText();
//                String station_name=i.path("station_name").asText();
//                String station_code=i.path("station_code").asText();
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
//                if(!departure.contains(arrival)){
//                    stopDetails.put("flag","red");
//                }else{
//                    stopDetails.put("flag","green");
//                }
//                stopDetails.put("departure", departure);
//                stopDetails.put("arrival", arrival);
//                HashMap<String, HashMap> stationDetails = new HashMap<>();
//                stationDetails.put(station_name, stopDetails);
//                stationDetails.put(station_name, stopDetails);
//
//                coordinatesAllStopTime
//                        .computeIfAbsent(train_number, k -> new ArrayList<>())
//                        .add(stationDetails);
//                if(!departure.contains(arrival)){
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
//                    stopDetail.put("flag","green");
//                    HashMap<String, HashMap> stationDetail = new HashMap<>();
//                    stationDetail.put(station_name, stopDetail);
//                    coordinatesStopTime
//                            .computeIfAbsent(train_number, k -> new ArrayList<>())
//                            .add(stationDetail);
//
//
//                    schedulingTrainDetails.put("coordinatesStopTime",coordinatesStopTime);
//                    schedulingTrainDetails.put("coordinates_stop",coordinates_stop);
//
//
//                }
//                schedulingTrainDetails.put("coordinates_AllStop",coordinates_allStop);
//                schedulingTrainDetails.put("coordinatesAllStopTime",coordinatesAllStopTime);
//
//            }
//        }
//        return schedulingTrainDetails;
//    }
//
//    public JSONObject processFinalResult() throws IOException {
//        String trainsPath = "src/main/resources/templates/trains.json";
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode trainPath = objectMapper.readTree(new File(trainsPath));
//
//        JSONObject finalResult = new JSONObject();
//        List<JSONObject> allData = new ArrayList<>();
//        int count=0;
//        for (JsonNode value : trainPath.path("features")) {
//            JSONObject data = processTrainNode(value);
//            allData.add(data);
////            System.out.println(data);
//            count+=1;
//            System.out.println(count);
//        }
//
//        finalResult.put("rawData", allData);
//        return finalResult;
//    }
//    private JSONObject processTrainNode(JsonNode value) throws IOException {
//        String trainNumber = value.path("properties").path("number").asText();
//        JSONObject data = new JSONObject();
//
//        // Extract data from train node
//        String thirdAc = value.path("properties").path("third_ac").asText();
//        String arrival = value.path("properties").path("arrival").asText();
//        String fromStationCode = value.path("properties").path("from_station_code").asText();
//        String name = value.path("properties").path("name").asText();
//        String zone = value.path("properties").path("zone").asText();
//        String chairCar = value.path("properties").path("chair_car").asText();
//        String firstClass = value.path("properties").path("first_class").asText();
//        String durationM = value.path("properties").path("duration_m").asText();
//        String sleeper = value.path("properties").path("sleeper").asText();
//        String fromStationName = value.path("properties").path("from_station_name").asText();
//        String number = value.path("properties").path("number").asText();
//        String departure = value.path("properties").path("departure").asText();
//        String returnTrain = value.path("properties").path("return_train").asText();
//        String toStationCode = value.path("properties").path("to_station_code").asText();
//        String secondAc = value.path("properties").path("second_ac").asText();
//        String classes = value.path("properties").path("classes").asText();
//        String toStationName = value.path("properties").path("to_station_name").asText();
//        String durationH = value.path("properties").path("duration_h").asText();
//        String type = value.path("properties").path("type").asText();
//        String firstAc = value.path("properties").path("first_ac").asText();
//        String distance = value.path("properties").path("distance").asText();
//
//
//        // Combine more data...
//        HashMap<String,HashMap> processData=processSchedulingTrainDetails();
//
//
//        // Use ObjectMapper to convert extracted data to JSONObject
//        data.put("third_ac", thirdAc);
//        data.put("arrival", arrival);
//        data.put("from_station_code", fromStationCode);
//        data.put("name", name);
//        data.put("zone", zone);
//        data.put("chair_car", chairCar);
//        data.put("first_class", firstClass);
//        data.put("duration_m", durationM);
//        data.put("sleeper", sleeper);
//        data.put("from_station_name", fromStationName);
//        data.put("number", number);
//        data.put("departure", departure);
//        data.put("return_train", returnTrain);
//        data.put("to_station_code", toStationCode);
//        data.put("second_ac", secondAc);
//        data.put("classes", classes);
//        data.put("to_station_name", toStationName);
//        data.put("duration_h", durationH);
//        data.put("type", type);
//        data.put("first_ac", firstAc);
//        data.put("distance", distance);
//        data.put("coordinates_AllStop",processData.get("coordinates_AllStop").get(number));
//        data.put("coordinates_stop",processData.get("coordinates_stop").get(number));
//        data.put("coordinatesStopTime",processData.get("coordinatesStopTime").get(number));
//        data.put("coordinatesAllStopTime",processData.get("coordinatesAllStopTime").get(number));
//
//
//
//        return data;
//    }
//
//    public void writeJsonToFile(JSONObject finalResult) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(new File("schedules_output.json"), finalResult);
//        System.out.println("completed");
//    }
package com.booking.trainticket.AdminAccess.Stations.Service;

import com.booking.trainticket.AdminAccess.Stations.Pojo.Relationship_properties;
import com.booking.trainticket.AdminAccess.Stations.Pojo.StationDetails;
import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.booking.trainticket.AdminAccess.Stations.Repository.StationRepo;
import com.booking.trainticket.AdminAccess.Stations.Repository.TrainInfoRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Service
public class DbConnectionService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    StationRepo stationRepo;
    @Autowired
    TrainInfoRepo trainInfoRepo;

    public static List<String> stringToList(String jsonString) throws IOException {
        // Remove the square brackets and split the string by comma and space to get individual coordinates
        String[] coordinatesArray = jsonString.substring(1, jsonString.length() - 1).split(", ");

        // Convert the array to a list
        return Arrays.asList(coordinatesArray);
    }

    public static List<String> jsonToListStr(JsonNode value) {
        List<String> result = new ArrayList<>();
        if (value.isArray()) {
            ArrayNode coordinatesArray = (ArrayNode) value;
            for (JsonNode node : coordinatesArray) {
                result.add(String.valueOf(node.asText()));

            }
        }
        return result;
    }



    public static List<String> stringsToList(String jsonString) {
        List<String> list = new ArrayList<>();

        try {
            // Parse the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);


            // Iterate through the array and add each object as a string to the list
            for (JsonNode node : jsonNode) {
                list.add(node.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

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

    public JSONObject finalRelationShipEstablishment(Long number, List<String> coordinates_stop, List<String> coordinates_AllStop, List<String> coordinatesStopTime, List<String> coordinatesAllStopTime, String arrival, String departure, String name, String type) {

        JsonNode coordinates_AllStopJson = stringToJsonNode((coordinates_AllStop).toString());
        JsonNode coordinatesAllStopTimeJson = stringToJsonNode(coordinatesAllStopTime.toString());
        JsonNode coordinates_stopJson = stringToJsonNode(coordinates_stop.toString());
        JsonNode coordinatesStopTimeJson = stringToJsonNode(coordinatesStopTime.toString());
        /*
        change the parameter for the function call
         */
//        finalRelationShip(number, coordinates_AllStopJson, coordinatesAllStopTimeJson, arrival, departure, coordinates_stopJson, coordinatesStopTimeJson, name, type);

        finalRelationShip(number, coordinates_stopJson, coordinatesStopTimeJson, arrival, departure, coordinates_AllStopJson, coordinatesAllStopTimeJson, name, type);
        return null;
    }

    public JsonNode stringToJsonNode(String jsonString) {
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the string into a JsonNode object
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            return jsonNode;
        } catch (Exception e) {
            // Handle exception if parsing fails
            e.printStackTrace();
            return null;
        }
    }

    public String relationShip(String number, JsonNode coordinates_AllStopJson, JsonNode coordinatesAllStopTime, String arrival, String departure, JsonNode coordinates_stopJson, JsonNode coordinatesStopTimeJson) {
        coordinatesAllStopTime=coordinates_stopJson;
        coordinates_AllStopJson=coordinatesStopTimeJson;
        if (coordinates_AllStopJson.isArray() && coordinates_AllStopJson.size() >= 2) {
            for (int i = 0; i < coordinates_AllStopJson.size(); i++) {
                String station1 = coordinates_AllStopJson.get(i).asText();
                String arrivalData = coordinatesAllStopTime.get(i).path(station1).path("arrival").asText();
                String departureData = coordinatesAllStopTime.get(i).path(station1).path("departure").asText();
                String flag = coordinatesAllStopTime.get(i).path(station1).path("flag").asText();

                if (arrivalData.contains("None") || departureData.contains("None")) {
                    arrivalData = arrival;
                    departureData = departure;
                }

                if (flag.equals("green")) {
//                    String arv=trainInfoRepo.relationshipTrainToStation(number, station1, arrivalData, departureData,0,null);

                } else {

                }
                if (i < coordinates_AllStopJson.size() - 1) {

//                    String station2=coordinates_AllStopJson.get(i+1).asText();
//                    StationDetails details1=stationRepo.coordinatesLocation(station1);
//                    StationDetails details2=stationRepo.coordinatesLocation(station2);
//                    System.out.println("#####"+details1.getLongitude());
//                    System.out.println("%%%%%"+details1);
//                    System.out.println("&&&&"+details2.getLatitude());
//                    Double longitude1 = Double.valueOf(details1.getLongitude());
//                    Double latitude1 = Double.valueOf(details1.getLatitude());
//                    Double longitude2 = Double.valueOf(details2.getLongitude());
//                    Double latitude2 = Double.valueOf(details2.getLatitude());
//                    double distance = calculateDistance(latitude1, longitude1, latitude2, longitude2);
//                    System.out.println(distance);
//                    stationRepo.relationshipWithBetweenTwoStations(station1, station2, distance,arrivalData, departureData, flag, number);
                    String station2 = coordinates_AllStopJson.get(i + 1).asText();

                    StationDetails details1 = stationRepo.coordinatesLocation(station1);
                    StationDetails details2 = stationRepo.coordinatesLocation(station2);

                    if (details1 != null && details2 != null) {

                        try {
                            Double longitude1 = Double.valueOf(details1.getLongitude());
                            Double latitude1 = Double.valueOf(details1.getLatitude());
                            Double longitude2 = Double.valueOf(details2.getLongitude());
                            Double latitude2 = Double.valueOf(details2.getLatitude());

                            double distance = calculateDistance(latitude1, longitude1, latitude2, longitude2);

                            stationRepo.relationshipWithBetweenTwoStations(station1, station2, distance, arrivalData, departureData, flag, number);

                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing longitude/latitude values: " + e.getMessage());
                        }
                    } else {
                        System.err.println("Station details not found for station1 or station2.");
                    }
                }
            }
        }
        return null;
    }

    public String finalRelationShip(Long number, JsonNode coordinates_AllStopJson, JsonNode coordinatesAllStopTime, String arrival, String departure, JsonNode coordinates_stopJson, JsonNode coordinatesStopTimeJson, String name, String type) {
        Long stationCount = 0L;
        if (coordinates_AllStopJson.isArray()) {
            for (int i = 0; i != coordinates_AllStopJson.size() - 1; i++) {
                String station1 = coordinates_AllStopJson.get(i).asText();
                String arrivalData = coordinatesAllStopTime.get(i).path(station1).path("arrival").asText();
                String departureData = coordinatesAllStopTime.get(i).path(station1).path("departure").asText();
                String flag = coordinatesAllStopTime.get(i).path(station1).path("flag").asText();

                // Parse arrival and departure times to LocalTime
                if (arrivalData.contains("None") && departureData.contains("None")) {
                    arrivalData = "00:00:00";
                    departureData = "00:00:00";
                } else {
                    if (arrivalData.contains("None") || arrivalData.isEmpty()) {
                            arrivalData = "00:00:00";
                    }
                    if (departureData.contains("None")||departureData.isEmpty()) {
                            departureData = "00:00:00";
                    }
                }
                if(arrival.equals("")) {
                    arrivalData = "00:00:00";
                }
                if(departure.equals("")) {
                    departureData = "00:00:00";
                }
                LocalTime parsedDeparture = LocalTime.parse(departureData);

                LocalTime parsedArrival = LocalTime.parse(arrivalData);
                if(arrivalData.equals(departure)){
                 flag="red";
                }

//                Relationship_properties relationshipProperties=new Relationship_properties();
//                relationshipProperties.setArrival(parsedArrival);
//                relationshipProperties.setDeparture(parsedDeparture);
//                relationshipProperties.setFlag(flag);
//                relationshipProperties.setStationStops(stationCount);
//                System.out.println(number+"%%%%%%%%%"+station1+"@@@@@@@@@@"+ parsedArrival+"##########"+ parsedDeparture+"^^^^^^^^^^^"+stationCount+"{{{{{{{{{{"+ flag+"}}}}}}}}}}}"+ name+"||||||"+ type+"PPPPPPPPP");
                // Store relationship in the database
                String arv = trainInfoRepo.relationshipTrainToStation(number, station1, parsedArrival, parsedDeparture, stationCount, flag, name, type);
                stationCount += 1;
                System.out.println(stationCount);
//                System.out.println(arv);
            }
        }
        return null;
    }
    List<String> tamilNaduJunctionStations = Arrays.asList(
            "MAS", "MS", "CBE", "MDU", "TPJ", "SA", "ED", "TEN", "KPD", "TUP",
            "NCJ", "TJ", "VLR", "DG", "KRR", "VM", "RMM", "TRL", "VPT", "TBM",
            "KIK", "TCN", "MV", "TPTN", "NMKL", "MDRD", "TP", "TRT", "VMV", "UD",
            "PLNI", "CUPJ", "PTJ", "TJ", "MMK", "SHU", "PV", "TRK", "SVPR", "VRI",
            "CYN", "AYN", "PWL", "CTRE", "ARL", "TNGM", "TPM", "PDY", "VDM", "KKDI",
            "VGM", "MAF", "AMT", "KAVM", "KALL", "PLMD", "MNC", "PUP", "MLPM", "KLYM",
            "TND", "MUT", "MLMR", "VEL", "MNM", "PMK", "PJ", "PKT", "NLA", "TBM",
            "TO", "GY", "STM", "KGY", "VYA", "MEP", "NPKM", "MIH", "WST", "BMBL",
            "KMU", "NRE", "MLND", "TNG", "SNC", "EL", "AIPP", "VKT", "CSDR",
            "TDN", "VVN", "TCN", "SMD", "SNO", "VRI", "VOH", "KMBM", "DG", "DK",
            "VLT", "UKL", "BBN", "MRV", "KUZ", "VRI", "TBMU", "TJL", "MAOL", "PAZ",
            "KTVM", "DTVA", "KTTR", "MEJ", "PUDI", "KOV", "VDL", "MPU", "MPE", "SCA",
            "TY", "PBKS", "OOR", "KVKM", "SVM", "SOL", "SCT", "UVD", "KKPM",
            "TOY", "MSR", "NML", "YGL", "MUR", "AKM", "JTY", "UK", "APR", "PNT",
            "OAC", "SBGA", "VLCA", "SKLT", "KZC", "MPLM", "VM", "VN", "PRT", "ODD",
            "AMB", "TPM", "MEP", "VC", "TUP", "SBV", "CSD", "AAY", "TPM", "MLJ",
            "PTJ", "CTRE", "VGM", "WJR", "KQN", "PDM", "TVR", "TPTN", "TVP", "TLDI",
            "TSD", "TNM", "VEI", "VO", "PND", "AAY", "TLR", "TAE", "PML", "NPK",
            "PDY", "KIK", "VDY", "GOC", "RU", "PLX", "MAO", "TBR", "CBE", "MGS",
            "KRPU", "SBC", "ASR", "APDJ", "LTT", "YPR", "TVC", "ADI", "PNBE", "PURI","CSTM",
            "DR", "KYN", "KJT", "LNL", "KK", "PUNE", "DD", "BGVN", "JEUR", "KEM", "KWV", "MA",
            "MO", "SUR", "HG", "AKOR", "DUD", "GUR", "GR", "SDB", "WADI", "NW", "YG", "NRPD",
            "KSN", "RC", "MTU", "MALM", "KO", "KGL", "AD", "NRR", "GTL", "GY", "RLO", "TU",
            "KDP", "MOO", "YA", "KKM", "HX", "NRE", "RJP", "KOU", "RU", "PUT", "TRT", "AJJ",
            "TRL", "PER", "MAS","TPTY", "RU", "PUDI", "TDK", "PUT", "VGA", "EKM", "NG", "VKZ",
            "POI", "TRT", "AJJ", "PLMG", "MSU", "TO", "MAF", "SPAM", "KBT", "EGT", "TRL", "PTLR",
            "SVR", "VEU", "TI", "NEC", "PAB", "HC", "AVD", "ANNR", "TMVL", "ABU", "PVM", "KOTR",
            "VLK", "PEW", "PCW", "PER", "VJM", "BBQ"
    );

    public boolean tamilNaduJunction(List<String> list){
        for(String i :list){
            return tamilNaduJunctionStations.contains(i);
        }
        return false;
    }


//    public void setTrainDetailsFromJson(JsonNode value, TrainDetails trainDetails) {
//        trainDetails.setThird_ac(value.path("properties").path("third_ac").asText());
//        trainDetails.setArrival(LocalTime.parse(value.path("properties").path("arrival").asText()));
//        trainDetails.setFrom_station_code(value.path("properties").path("from_station_code").asText());
//        trainDetails.setName(value.path("properties").path("name").asText());
//        trainDetails.setZone(value.path("properties").path("zone").asText());
//        trainDetails.setChair_car(value.path("properties").path("chair_car").asText());
//        trainDetails.setFirst_class(value.path("properties").path("first_class").asText());
//        trainDetails.setDuration_m(value.path("properties").path("duration_m").asText());
//        trainDetails.setSleeper(value.path("properties").path("sleeper").asText());
//        trainDetails.setFrom_station_name(value.path("properties").path("from_station_name").asText());
//        trainDetails.setNumber(value.path("properties").path("number").asText());
//        trainDetails.setDeparture(value.path("properties").path("departure").asText());
//        trainDetails.setReturn_train(value.path("properties").path("return_train").asText());
//        trainDetails.setTo_station_code(value.path("properties").path("to_station_code").asText());
//        trainDetails.setSecond_ac(value.path("properties").path("second_ac").asText());
//        trainDetails.setClasses(value.path("properties").path("classes").asText());
//        trainDetails.setTo_station_name(value.path("properties").path("to_station_name").asText());
//        trainDetails.setDuration_h(value.path("properties").path("duration_h").asText());
//        trainDetails.setType(value.path("properties").path("type").asText());
//        trainDetails.setFirst_ac(value.path("properties").path("first_ac").asText());
//        trainDetails.setDistance(value.path("properties").path("distance").asText());
//
//    }
}

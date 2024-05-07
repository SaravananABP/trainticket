package com.booking.trainticket.WhereIsMyTrain.Service;

import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.booking.trainticket.AdminAccess.Stations.Repository.StationRepo;
import com.booking.trainticket.AdminAccess.Stations.Repository.TrainInfoRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainDetailsFetchService {
    @Autowired
    StationRepo stationRepo;
    @Autowired
    TrainInfoRepo trainInfoRepo;
    public static List<String> getElementsBetween(List<String> input, String start, String end) {
        // Find the indices of start and end elements
        int startIndex = input.indexOf(start);
        int endIndex = input.indexOf(end);

        // Return an empty list if start or end element is not found
        if (startIndex == -1 || endIndex == -1) {
            return new ArrayList<>();
        }

        // Ensure endIndex is greater than startIndex
        if (startIndex > endIndex) {
            int temp = startIndex;
            startIndex = endIndex;
            endIndex = temp;
        }
        // Return the sublist between startIndex and endIndex (exclusive)
        return input.subList(startIndex, endIndex + 1);
    }

    public JSONObject filterStationsTime(List<String> inputJson, List<String> stationNames) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(inputJson.toString());
        JSONObject result = new JSONObject();
        for (int i = 0; i < stationNames.size(); i++) {
            String stationName = stationNames.get(i);
            JSONObject innerResponse = new JSONObject();

            for (JsonNode value : jsonNode) {
                JsonNode stationInfo = value.path(stationName);
                if (!stationInfo.isEmpty()) {
                    String arrival = stationInfo.path("arrival").asText();
                    String departure = stationInfo.path("departure").asText();
                    innerResponse.put("arrival", arrival);
                    innerResponse.put("departure", departure);
                    result.put(stationName, innerResponse);
                }
            }

        }
        return result;
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
    public int calculateTotalDistance(List<String> coordinateStations){
        double distance=0;
        for( int i=0;i<coordinateStations.size()-1;i++){
            String station1=coordinateStations.get(i);
            String station2=coordinateStations.get(i+1);
            Double sourceLatitude=stationRepo.getLatitude(station1);
            Double sourceLongitude=stationRepo.getLongitude(station1);
            Double destinationLatitude=stationRepo.getLatitude(station2);
            Double destinationLongitude=stationRepo.getLongitude(station2);
            distance+=calculateDistance(sourceLatitude,sourceLongitude,destinationLatitude,destinationLongitude);
        }
        return (int) Math.round(distance);
    }

    public List<Long> getTrainResponse(String type, String station1, String station2) {
        List<Long> response = new ArrayList<>();
        switch (type) {
            case "getBetweenToStationTrainsWithExpress":
                response = trainInfoRepo.getBetweenToStationTrainsWithExpress(station1, station2);
                break;
            case "getBetweenToStationTrainsWithDEMU":
                response = trainInfoRepo.getBetweenToStationTrainsWithDEMU(station1, station2);
                break;
            case "getBetweenToStationTrainsWithSF":
                response = trainInfoRepo.getBetweenToStationTrainsWithSF(station1, station2);
                break;
            case "getBetweenToStationTrainsWithPass":
                response = trainInfoRepo.getBetweenToStationTrainsWithPass(station1, station2);
                break;
            case "getBetweenToStationTrainsWithMail":
                response = trainInfoRepo.getBetweenToStationTrainsWithMail(station1, station2);
                break;
            case "getBetweenToStationTrainsWithJShtb":
                response = trainInfoRepo.getBetweenToStationTrainsWithJShtb(station1, station2);
                break;
            case "getBetweenToStationTrainsWithGR":
                response = trainInfoRepo.getBetweenToStationTrainsWithGR(station1, station2);
                break;
            case "getBetweenToStationTrainsWithMEMU":
                response = trainInfoRepo.getBetweenToStationTrainsWithMEMU(station1, station2);
                break;
            case "getBetweenToStationTrainsWithRaj":
                response = trainInfoRepo.getBetweenToStationTrainsWithRaj(station1, station2);
                break;
            case "getBetweenToStationTrainsWithShtb":
                response = trainInfoRepo.getBetweenToStationTrainsWithShtb(station1, station2);
                break;
            case "getBetweenToStationTrainsWithSKr":
                response = trainInfoRepo.getBetweenToStationTrainsWithSKr(station1, station2);
                break;
            case "getBetweenToStationTrainsWithDrnt":
                response = trainInfoRepo.getBetweenToStationTrainsWithDrnt(station1, station2);
                break;
            case "getBetweenToStationTrainsWithKlkt":
                response = trainInfoRepo.getBetweenToStationTrainsWithKlkt(station1, station2);
                break;
            case "getBetweenToStationTrainsWithHyd":
                response = trainInfoRepo.getBetweenToStationTrainsWithHyd(station1, station2);
                break;
            case "getBetweenToStationTrainsWithToy":
                response = trainInfoRepo.getBetweenToStationTrainsWithToy(station1, station2);
                break;
            case "getBetweenToStationTrainsWithDel":
                response = trainInfoRepo.getBetweenToStationTrainsWithDel(station1, station2);
                break;
            default:
                break;
        }
        return response;
    }

    public JSONObject buildTrainDetails(List<Long> response, String station1, String station2) throws IOException {
        JSONObject trainName = new JSONObject();
        for (Long trainNo : response) {
            JSONObject trainDetail = new JSONObject();
            TrainDetails trainDetails = trainInfoRepo.getTrainDetails(trainNo);

            List<String> coordinateStopTime = trainDetails.getCoordinatesStopTime();
//            System.out.println(coordinateStopTime);
            List<String> betweenStation = getElementsBetween(trainDetails.getCoordinates_stop(), station1, station2);
            JSONObject betweenStationWithTime = filterStationsTime(coordinateStopTime, betweenStation);
            int distance = calculateTotalDistance(betweenStation);
            trainDetail.put("TrainName", trainDetails.getName());
            trainDetail.put("Stations", betweenStation);
            trainDetail.put("Time", betweenStationWithTime);
            trainDetail.put("distance", distance);
            trainName.put(trainDetails.getNumber(), trainDetail);
        }
        return trainName;
    }
    public JSONObject buildTrainDetailsTodayAvailable(List<Long> response, String station1, String station2) throws IOException {
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalTime currentTime=LocalTime.now(zoneId);
        JSONObject trainName = new JSONObject();
        for (Long trainNo : response) {
            JSONObject trainDetail = new JSONObject();
            TrainDetails trainDetails = trainInfoRepo.getTrainDetails(trainNo);
            LocalTime departure =LocalTime.parse(trainDetails.getDeparture());
            if(currentTime.isBefore(departure)) {
                List<String> coordinateStopTime = trainDetails.getCoordinatesStopTime();
                List<String> betweenStation = getElementsBetween(trainDetails.getCoordinates_stop(), station1, station2);
                JSONObject betweenStationWithTime = filterStationsTime(coordinateStopTime, betweenStation);
                int distance = calculateTotalDistance(betweenStation);
                trainDetail.put("TrainName", trainDetails.getName());
                trainDetail.put("Stations", betweenStation);
                trainDetail.put("Time", betweenStationWithTime);
                trainDetail.put("distance", distance);
                trainName.put(trainDetails.getNumber(), trainDetail);

            }
        }
        return trainName;
    }
}

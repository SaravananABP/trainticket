package com.booking.trainticket.UserDetails.Service;

import com.booking.trainticket.UserDetails.PojoClass.History;
import com.booking.trainticket.UserDetails.Repository.HistoryRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {
    @Autowired
    HistoryRepo historyRepo;
    @Autowired
    WalletService walletService;

    public void addLocalHistory(String arrival,String departure,String emailId) throws JsonProcessingException {

        String getAllLocalHistory = historyRepo.getAllLocalHistory(emailId);
        List<JSONObject> localHis = new ArrayList<>();
        if (getAllLocalHistory != (null)) {
            JsonNode localHistoryJsonNode = new ObjectMapper().readTree(getAllLocalHistory.toString());

            if (!localHistoryJsonNode.isEmpty()) {
                for (JsonNode i : localHistoryJsonNode) {
                    String source = i.path("source").asText();
                    String destination = i.path("destination").asText();
                    JSONObject response = new JSONObject();
                    response.put("source", source);
                    response.put("destination", destination);
                    if(source!="Not Available"){
                        localHis.add(response);
                    }
                }
                JSONObject response = new JSONObject();
                response.put("source", arrival);
                response.put("destination", departure);
                localHis.add(response);
            } else {
                JSONObject response = new JSONObject();
                response.put("source", arrival);
                response.put("destination", departure);
                localHis.add(response);
            }
        }
        historyRepo.addNewHistory(emailId,getLastNElements(localHis,5).toString());
        }
    public void addBookingHistory(String arrival,String departure,String emailId,String type, int distance,double paid) throws JsonProcessingException {

        String getAllLocalHistory = historyRepo.getAllBookingHistory(emailId);
        List<JSONObject> bookingHis = new ArrayList<>();
        if (getAllLocalHistory != (null)) {
            JsonNode localHistoryJsonNode = new ObjectMapper().readTree(getAllLocalHistory.toString());

            if (!localHistoryJsonNode.isEmpty()) {
                for (JsonNode i : localHistoryJsonNode) {
                    String source = i.path("source").asText();
                    String destination = i.path("destination").asText();
                    String trainType = i.path("type").asText();
                    String totalDistance = i.path("totalDistance(km)").asText();
                    String paid1 = i.path("paid amount").asText();
                    JSONObject response = new JSONObject();
                    response.put("source", source);
                    response.put("destination", destination);
                    response.put("trainType",trainType);
                    response.put("totalDistance",totalDistance);
                    response.put("paid",paid1);
                    if(source!="Not Available"){
                        bookingHis.add(response);
                    }
                }

            }
                JSONObject response = new JSONObject();
                response.put("source", arrival);
                response.put("destination", departure);
                response.put("trainType",type);
                response.put("totalDistance",distance);
                response.put("paid",paid);
            bookingHis.add(response);
        }
        historyRepo.addNewBookedHistory(emailId,bookingHis.toString());
    }
    public static List<JSONObject> getLastNElements(List<JSONObject> list, int n) {
        int size = list.size();
        return list.subList(Math.max(size - n, 0), size);
    }
//    public List<JSONObject> getAllLocalHistory(String emailId) throws JsonProcessingException {
//
//        String getAllLocalHistory = historyRepo.getAllLocalHistory(emailId);
//        List<JSONObject> localHis = new ArrayList<>();
//        if (getAllLocalHistory != (null)) {
//            JsonNode localHistoryJsonNode = new ObjectMapper().readTree(getAllLocalHistory.toString());
//
//            if (!localHistoryJsonNode.isEmpty()) {
//                for (JsonNode i : localHistoryJsonNode) {
//                    String source = i.path("source").asText();
//                    String destination = i.path("destination").asText();
//                    JSONObject response = new JSONObject();
//                    response.put("source", source);
//                    response.put("destination", destination);
//                    localHis.add(response);
//                }
//            }
//        }else{
//            JSONObject response = new JSONObject();
//            response.put("source", "Not Available");
//            response.put("destination", "Not Available");
//            localHis.add(response);
//        }
//        return localHis;
//    }
    public String getAllBookingHistory(String emailId){
        String bookingHis=historyRepo.getAllBookingHistory(emailId);
        return bookingHis;
    }
    public String getAllLocalHistory(String emailId){
        return historyRepo.getAllLocalHistory(emailId);
    }
    public void defaultLocalHistory(String emailId){
        List<JSONObject> localHis = new ArrayList<>();
        JSONObject response = new JSONObject();
        response.put("source", "Not Available");
        response.put("destination", "Not Available");
        localHis.add(response);
        historyRepo.addNewHistory(emailId,localHis.toString());
    }


}

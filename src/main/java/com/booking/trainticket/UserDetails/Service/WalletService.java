package com.booking.trainticket.UserDetails.Service;

import com.booking.trainticket.Authentication.Service.UserInfoDetailsService;
import com.booking.trainticket.UserDetails.PojoClass.Wallet;
import com.booking.trainticket.UserDetails.Repository.WalletRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class WalletService {
    @Autowired
    WalletRepo walletRepo;
    @Autowired
    UserInfoDetailsService userInfoDetailsService;
    public void  makeBookingHistory(String email,String history){
        walletRepo.addNewPaymentHistory(email,history);
    }

    public JSONObject makeRecharge(float rechargeAmt,String emailId) throws JsonProcessingException {
        JSONObject finalResponse=new JSONObject();
        Float availableAmt=0.0f;
        if(walletRepo.availableBalance(emailId).getAvailableAmt()==null){
            availableAmt=  0.0f;
        }else {

            availableAmt=walletRepo.availableBalance(emailId).getAvailableAmt();

        }

        Float afterRecharge=availableAmt+(Float) rechargeAmt;

        JSONObject historyTiming=  makeHistoryWithTimings(rechargeAmt,afterRecharge,"Recharge");
        String walletHistory=walletRepo.getWalletHistory(emailId);
        List<JSONObject> walletHis = new ArrayList<>();
        if(walletHistory!=(null)) {
            JsonNode walletHistoryJsonNode = new ObjectMapper().readTree(walletHistory.toString());

            if (!walletHistoryJsonNode.isEmpty()) {
                for (JsonNode i : walletHistoryJsonNode) {
                    String date = i.path("date").asText();
                    String time = i.path("time").asText();
                    String rechargeAmount = i.path("rechargeAmt").asText();
                    String availableAmount = i.path("availableAmt").asText();
                    JSONObject response = new JSONObject();
                    response.put("date", date);
                    response.put("time", time);
                    response.put("rechargeAmt", rechargeAmount);
                    response.put("availableAmt", availableAmount);
                    walletHis.add(response);
                }
                walletHis.add(historyTiming);
            }
        }else {
            walletHis.add(historyTiming);
        }
        walletRepo.rechargeAmt(emailId, afterRecharge);
        walletRepo.addWalletHistory(emailId,walletHis.toString());

        finalResponse.put("Status","success");
        Float afterRechargeAvailableAmount=walletRepo.availableBalance(emailId).getAvailableAmt();
        finalResponse.put("Available balance",afterRechargeAvailableAmount);
        return finalResponse;
    }
    public void transectionHistory(float transectionAmt,String type,String emailId) throws JsonProcessingException {

        Float availableAmt=walletRepo.availableBalance(emailId).getAvailableAmt();
        if(availableAmt==null){
            availableAmt=0.0f;
        }
        JSONObject historyTiming=  makeHistoryWithTimings(transectionAmt,availableAmt,type);
        String transectionHistory=walletRepo.getAllTransectionHistory(emailId);
        List<JSONObject> transectionHis = new ArrayList<>();
        if(transectionHistory!=(null)) {
            JsonNode walletHistoryJsonNode = new ObjectMapper().readTree(transectionHistory.toString());

            if (!walletHistoryJsonNode.isEmpty()) {
                for (JsonNode i : walletHistoryJsonNode) {
                    String date = i.path("date").asText();
                    String time = i.path("time").asText();
                    String type1 = i.path("type").asText();
                    String paidAmount = i.path("paid amount").asText();
                    String availableAmount = i.path("availableAmt").asText();
                    JSONObject response = new JSONObject();
                    response.put("date", date);
                    response.put("time", time);
                    response.put("type",type1);
                    response.put("paid amount", paidAmount);
                    response.put("available amount", availableAmount);
                    transectionHis.add(response);
                }

                transectionHis.add(historyTiming);
            }
        }else {
            transectionHis.add(historyTiming);
        }
        walletRepo.addTransectionHistory(emailId,transectionHis.toString());

    }
    public JSONObject makeHistoryWithTimings(Float payedAmt, Float availAmt,String type){
        JSONObject response=new JSONObject();
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalTime currentTime = LocalTime.now(zoneId);
        response.put("date", LocalDate.now(zoneId).toString());
        response.put("time", currentTime.toString());
        response.put("type",type.toString());
        response.put("paid amount", payedAmt.toString());
        response.put("availableAmt", availAmt.toString());
        return response;
    }
//    public JSONObject payedHistoryTimings(Float payedAmt, Float availAmt){
//        JSONObject finalResponse=new JSONObject();
////        Map<String, String> response = new HashMap<>();
//        JSONObject response=new JSONObject();
//        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
//        LocalTime currentTime = LocalTime.now(zoneId);
//        response.put("date", LocalDate.now(zoneId));
//        response.put("time", currentTime);
//        response.put("type","booking");
//        response.put("paid amount", payedAmt);
//        response.put("availableAmt", availAmt);
//        finalResponse.put("result",response);
//        return response;
//    }

    public String getWalletHistory(String emailId){
        return walletRepo.getWalletHistory(emailId);
    }
    public String getAllTransectionHistory(String emailId){

        return walletRepo.getAllTransectionHistory(emailId);
    }
    public Float getAvailableBalance(String emailId){
        return walletRepo.availableBalance(emailId).getAvailableAmt();
    }
    public void defaultWalletHistory(String emailId){
        List<JSONObject> walletHis = new ArrayList<>();

        JSONObject response=new JSONObject();
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalTime currentTime = LocalTime.now(zoneId);
        response.put("date", LocalDate.now(zoneId).toString());
        response.put("time", currentTime.toString());
        response.put("rechargeAmt", 0);
        response.put("availableAmt", 0);
        walletHis.add(response);
        walletRepo.addWalletHistory(emailId,walletHis.toString());
        walletRepo.rechargeAmt(emailId,0.0f);
    }
    public void reducePaidAmount(String emailId,Float amt){
        walletRepo.rechargeAmt(emailId,(Float) amt);
    }
}

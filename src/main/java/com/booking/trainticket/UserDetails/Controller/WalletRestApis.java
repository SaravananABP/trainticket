package com.booking.trainticket.UserDetails.Controller;

import com.booking.trainticket.UserDetails.Service.HistoryService;
import com.booking.trainticket.UserDetails.Service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.server.PathParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/wallet")
public class WalletRestApis {
    @Autowired
    WalletService walletService;
    @Autowired
    HistoryService historyService;

    @GetMapping("/wallet/get/balance")
    public JSONObject getBalance(@PathParam("emailId")String emailId){
        JSONObject response=new JSONObject();
        Float bal=walletService.getAvailableBalance(emailId);
        response.put("status","Success");
        response.put("available balance",bal);
        return response;
    }
    @PostMapping("/wallet/make/recharge")
    public JSONObject makeRecharge(@RequestParam("rechargeAmount")Float amount,@PathParam("emailId")String emailId) throws JsonProcessingException {
        walletService.transectionHistory(amount,"Recharge",emailId);
        return walletService.makeRecharge(amount,emailId);
    }

    @GetMapping("/history/all/wallet/histories")
    public String allWalletHistory(@PathParam("emailId")String emailId){
        String histories=walletService.getWalletHistory(emailId);
        return histories;
    }
    @GetMapping("/history/all/transection/histories")
    public String allTransectionHistory(@PathParam("emailId")String emailId){
        String histories=walletService.getAllTransectionHistory(emailId);
        return histories;
    }
    @GetMapping("/history/all/local/histories")
    public String allTLocalHistory(@PathParam("emailId")String emailId) throws JsonProcessingException {
        String histories=historyService.getAllLocalHistory(emailId);
        return histories;
    }
    @GetMapping("/history/all/booking/histories")
    public String allBookingHistory(@PathParam("emailId")String emailId){
        String histories=historyService.getAllBookingHistory(emailId);
        return histories;
    }
    @GetMapping("/history/getAllHistoryType")
    public JSONObject getAllHistoryType(@PathParam("emailId")String emailId) throws JsonProcessingException {
        JSONObject response=new JSONObject();
        ObjectMapper objectMapper=new ObjectMapper();
        response.put("Booking History",objectMapper.readTree(historyService.getAllBookingHistory(emailId)));
//        response.put("Local History",objectMapper.readTree(historyService.getAllLocalHistory(emailId)));
        response.put("transection History",objectMapper.readTree(walletService.getAllTransectionHistory(emailId)));
        response.put("wallet History",objectMapper.readTree(walletService.getWalletHistory(emailId)));
        return response;
    }


}

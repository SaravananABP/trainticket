package com.booking.trainticket.Booking.Controller;

import com.booking.trainticket.Booking.PojoClass.CreateTicket;
import com.booking.trainticket.Booking.Service.CreateUnreserveTicketService;
import com.booking.trainticket.UserDetails.Repository.WalletRepo;
import com.booking.trainticket.UserDetails.Service.HistoryService;
import com.booking.trainticket.UserDetails.Service.WalletService;
import com.booking.trainticket.WhereIsMyTrain.Service.TrainDetailsFetchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.server.PathParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class BookingApis {
    @Autowired
    CreateUnreserveTicketService createUnreserveTicketService;
    @Autowired
    WalletService walletService;
    @Autowired
    HistoryService historyService;
    @PostMapping("/unreserve/create/newticket")
    private JSONObject getBetweenTwoStationsAllTrainToday(@RequestBody CreateTicket createTicket, @PathParam("emailId") String emailId) throws JsonProcessingException {
        JSONObject response =new JSONObject();
        String arrival=createTicket.getArrival();
        String  departure=createTicket.getDeparture();
        String type=createTicket.getType();
        String classType=createTicket.getClassType();
        int totalDistance=createUnreserveTicketService.calculateDistance(arrival,departure,type);
        Float availableBalance=(Float) walletService.getAvailableBalance(emailId);
        double toPay=createUnreserveTicketService.calculateTicketPrice(type,(double) totalDistance);
        boolean checkTrainType=createUnreserveTicketService.checkAvailableType(arrival,departure,type);
        boolean checkAvailableBalance=(double)walletService.getAvailableBalance(emailId)>=toPay;
        if(!checkAvailableBalance){
            response.put("status","failed");
            response.put("reason","insufficient balance");
        }else if(!checkTrainType) {
            response.put("status","failed");
            response.put("reason","type in not found");
        }
        else if(checkTrainType && checkAvailableBalance){
            response.put("status","Payment success");
            response.put("source",arrival);
            response.put("destination",departure);
            response.put("type",type);
            response.put("totalDistance(km)",totalDistance);
            response.put("paid amount",toPay);
            walletService.reducePaidAmount(emailId, (float) ( availableBalance - toPay));
            walletService.makeBookingHistory(emailId,response.toString());
            response.put("available balance",walletService.getAvailableBalance(emailId));
            walletService.transectionHistory((float) toPay,"Booking",emailId);
            historyService.addBookingHistory(arrival,departure,emailId,type,totalDistance,availableBalance-toPay);
        }

        return response;
    }
    @GetMapping("/getAllAvailableType")
    private List<String> getAllAvailableType(@RequestBody CreateTicket createTicket, @PathParam("emailId") String emailId){
        String arrival=createTicket.getArrival();
        String  departure=createTicket.getDeparture();
        String type=createTicket.getType();
        return createUnreserveTicketService.getAllAvailableType(arrival,departure,type);
    }

}

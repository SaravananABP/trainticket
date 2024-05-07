package com.booking.trainticket.WhereIsMyTrain.Controller;

import com.booking.trainticket.AdminAccess.Stations.Pojo.Relationship_properties;
import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.booking.trainticket.AdminAccess.Stations.Repository.StationRepo;
import com.booking.trainticket.AdminAccess.Stations.Repository.TrainInfoRepo;

import com.booking.trainticket.Authentication.Service.UserInfoDetailsService;
import com.booking.trainticket.UserDetails.Service.HistoryService;
import com.booking.trainticket.WhereIsMyTrain.Service.TrainDetailsFetchService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.websocket.server.PathParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@RestController
public class TrainDetailsController {
    @Autowired
    public TrainInfoRepo trainInfoRepo;
    @Autowired
    TrainDetailsFetchService trainDetailsFetchService;
    @Autowired
    StationRepo stationRepo;
    @Autowired
    UserInfoDetailsService userInfoDetailsService;
    @Autowired
    HistoryService historyService;

    @GetMapping(path = "/get/allTrain/name")
    public List<String> getAllTrainNames() {
        return trainInfoRepo.getAllTrainNames();
    }

    @GetMapping(path = "/get/allTrain/station/name/{code}")
    public List<String> getAllTrainStationNames(@PathParam("code") String code) {
        return trainInfoRepo.getAllTrainStopInStation(code);
    }

    @GetMapping(path = "/get/allTrain/station/name/time")
    public List<HashMap<String, Object>> getAllTrainStationNamesWithTime(@PathVariable(name = "code") String code) {
        return trainInfoRepo.getAllTrainStopInStationwithTime(code);
    }


    @GetMapping(path = "/get/all/train/names/time")
    public JSONObject getBetweenTwoStationsAllTrainStationNames(@RequestParam(name = "source") String station1,
                                                                @RequestParam(name = "destination") String station2) throws IOException {
        List<String> typesTrain = Arrays.asList("getBetweenToStationTrainsWithExpress", "getBetweenToStationTrainsWithDEMU", "getBetweenToStationTrainsWithSF",
                "getBetweenToStationTrainsWithPass", "getBetweenToStationTrainsWithMail", "getBetweenToStationTrainsWithJShtb",
                "getBetweenToStationTrainsWithGR", "getBetweenToStationTrainsWithMEMU", "getBetweenToStationTrainsWithRaj",
                "getBetweenToStationTrainsWithShtb", "getBetweenToStationTrainsWithSKr", "getBetweenToStationTrainsWithDrnt",
                "getBetweenToStationTrainsWithKlkt", "getBetweenToStationTrainsWithHyd", "getBetweenToStationTrainsWithToy",
                "getBetweenToStationTrainsWithDel");
        List<String> typesTrainNames = Arrays.asList("Exp", "DEMU", "SF", "Pass", "Mail", "JShtb", "GR", "MEMU", "Raj", "Shtb", "SKr", "Drnt", "Klkt", "Hyd", "Toy", "Del");
        JSONObject result = new JSONObject();
        for (int i = 0; i < typesTrainNames.size(); i++) {
            List<Long> response = trainDetailsFetchService.getTrainResponse(typesTrain.get(i), station1, station2);
            if (!response.isEmpty()) {
                JSONObject trainName = trainDetailsFetchService.buildTrainDetails(response, station1, station2);
                result.put(typesTrainNames.get(i), trainName);
                System.out.println();
            }
        }
        String emailId= userInfoDetailsService.getUserEmail();
        historyService.addLocalHistory(station1,station2,emailId);
        return result;
    }

    @GetMapping(path = "/get/all/train/today/available")
    public JSONObject getBetweenTwoStationsAllTrainToday(@RequestParam(name = "source") String station1,
                                                         @RequestParam(name = "destination") String station2) throws IOException {
        List<String> typesTrain = Arrays.asList("getBetweenToStationTrainsWithExpress", "getBetweenToStationTrainsWithDEMU", "getBetweenToStationTrainsWithSF",
                "getBetweenToStationTrainsWithPass", "getBetweenToStationTrainsWithMail", "getBetweenToStationTrainsWithJShtb",
                "getBetweenToStationTrainsWithGR", "getBetweenToStationTrainsWithMEMU", "getBetweenToStationTrainsWithRaj",
                "getBetweenToStationTrainsWithShtb", "getBetweenToStationTrainsWithSKr", "getBetweenToStationTrainsWithDrnt",
                "getBetweenToStationTrainsWithKlkt", "getBetweenToStationTrainsWithHyd", "getBetweenToStationTrainsWithToy",
                "getBetweenToStationTrainsWithDel");
        List<String> typesTrainNames = Arrays.asList("Exp", "DEMU", "SF", "Pass", "Mail", "JShtb", "GR", "MEMU", "Raj", "Shtb", "SKr", "Drnt", "Klkt", "Hyd", "Toy", "Del");
        JSONObject result = new JSONObject();
        for (int i = 0; i < typesTrainNames.size(); i++) {
            List<Long> response = trainDetailsFetchService.getTrainResponse(typesTrain.get(i), station1, station2);
            if (!response.isEmpty()) {
                JSONObject trainName = trainDetailsFetchService.buildTrainDetailsTodayAvailable(response, station1, station2);
                if (trainName!=null) {
                    result.put(typesTrainNames.get(i), trainName);
                }
            }
        }
        if (result.isEmpty()) {
            result.put("status", "Today no train available!!!");
        }
        String emailId= userInfoDetailsService.getUserEmail();
        historyService.addLocalHistory(station1,station2,emailId);
        return result;
    }
}

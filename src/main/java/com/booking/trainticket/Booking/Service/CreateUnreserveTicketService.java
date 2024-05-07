package com.booking.trainticket.Booking.Service;

import com.booking.trainticket.AdminAccess.Stations.Pojo.TrainDetails;
import com.booking.trainticket.AdminAccess.Stations.Repository.TrainInfoRepo;
import com.booking.trainticket.WhereIsMyTrain.Service.TrainDetailsFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CreateUnreserveTicketService {
    @Autowired
    TrainInfoRepo trainInfoRepo;
    @Autowired
    TrainDetailsFetchService trainDetailsFetchService;
    public int calculateDistance(String start,String end,String trainType){
        TrainDetails trainDetails = trainInfoRepo.getTrainByType(start,end,trainType);
        List<String> betweenStation = trainDetailsFetchService.getElementsBetween(trainDetails.getCoordinates_AllStop(), start, end);
        return trainDetailsFetchService.calculateTotalDistance(betweenStation);
    }
    public boolean checkAvailableType(String start,String end,String TrainType){
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalTime currentTime = LocalTime.now(zoneId);
        List<String> availableType=trainInfoRepo.getAllTypeBetweenStationWithTime(start,end,currentTime.toString());
        return availableType.contains(TrainType);
    }
    public List<String> getAllAvailableType(String start, String end, String TrainType){
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        LocalTime currentTime = LocalTime.now(zoneId);
        List<String> availableType=trainInfoRepo.getAllTypeBetweenStationWithTime(start,end,currentTime.toString());
        return availableType;
    }
    public static Float calculateTicketPrice(String trainType, double distanceInKm) {
        // Convert trainType to lowercase
        trainType = trainType.toLowerCase();

        // Calculate the number of 25 km increments
        int increments = (int) Math.ceil(distanceInKm / 25);

        // Define ticket prices based on train type
        switch (trainType.toLowerCase()) {
            case "exp":
                return (float) (10.0 * increments); // Rs. 10 for each 25 km increment
            case "demu":
                return (float) (7.5 * increments); // Rs. 7.5 for each 25 km increment
            case "sf":
                return (float) (15.0 * increments); // Rs. 15 for each 25 km increment
            case "pass":
                return (float) (5.0 * increments); // Rs. 5 for each 25 km increment
            case "mail":
                return (float) (13.0 * increments); // Rs. 13 for each 25 km increment
            case "jshtb":
                return (float) (5.0 * increments); // Rs. 5 for each 25 km increment
            case "gr":
                return (float) (10.0 * increments); // Rs. 10 for each 25 km increment
            case "memu":
                return (float) (6.0 * increments); // Rs. 6 for each 25 km increment
            case "raj":
                return (float) (20.0 * increments); // Rs. 20 for each 25 km increment
            case "shtb":
                return (float) (5.0 * increments); // Rs. 5 for each 25 km increment
            case "skr":
                return (float) (13.0 * increments); // Rs. 13 for each 25 km increment
            case "drnt":
                return (float) (18.0 * increments); // Rs. 18 for each 25 km increment
            case "klkt":
                return (float) (10.0 * increments); // Rs. 10 for each 25 km increment
            case "hyd":
                return (float) (15.0 * increments); // Rs. 15 for each 25 km increment
            case "toy":
                return (float) (22.0 * increments); // Rs. 22 for each 25 km increment
            case "del":
                return (float) (25.0 * increments); // Rs. 25 for each 25 km increment
            default:
                return (float) (10.0 * increments); // For unknown train types, Rs. 10 for each 25 km increment
        }
    }


}

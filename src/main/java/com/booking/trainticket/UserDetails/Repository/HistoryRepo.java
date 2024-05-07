package com.booking.trainticket.UserDetails.Repository;

import com.booking.trainticket.UserDetails.PojoClass.History;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo extends Neo4jRepository<History,Long>{
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(h:History)  return h.BookingHistory")
    String getAllBookingHistory(String emailId);
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(h:History)  return h.localHistory")
    String getAllLocalHistory(String emailId);
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(w:History) set w.localHistory=$newHistory return w.localHistory")
    String defaultLocalHistory(String emailId,String newHistory);
    @Query("MATCH (:User{emailId:$emailId})-[:toHistory]->(w:History) RETURN w.walletHistory")
    String getWalletHistory(String emailId);

    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(w:History) set w.localHistory=$newHistory return w.localHistory")
    String addNewHistory(String emailId, String newHistory);
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(w:History) set w.BookingHistory=$newHistory return w.localHistory")
    String addNewBookedHistory(String emailId,String newHistory);


}

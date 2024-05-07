package com.booking.trainticket.UserDetails.Repository;

import com.booking.trainticket.UserDetails.PojoClass.Wallet;
import org.json.simple.JSONObject;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WalletRepo extends Neo4jRepository<Wallet,Long> {
    @Query("MERGE (u:User {emailId: $emailId})-[:walletAmount]->(w:Wallet) SET w.availableAmt = $amt RETURN w")
    Wallet rechargeAmt(String emailId, Float amt);

    @Query("merge (u:User{emailId: $emailId})-[:walletAmount]->(w:Wallet) return w")
    Wallet availableBalance(String emailId);
    @Query("MATCH (:User{emailId:$emailId})-[:toHistory]->(w:History) RETURN w.walletHistory")
    String getWalletHistory(String emailId);
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(w:History) set w.walletHistory=$newHistory return w.availableAmt")
    Float addWalletHistory(String emailId, String newHistory);
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(w:History) set w.bookingHistory=$newHistory return w.availableAmt")
    Float addNewPaymentHistory(String emailId, String newHistory);
    @Query("merge (u:User{emailId: $emailId})-[:toHistory]->(w:History) set w.transectionHistory=$newHistory return w.availableAmt")
    Float addTransectionHistory(String emailId, String newHistory);
    @Query("MATCH (:User{emailId:$emailId})-[:toHistory]->(w:History) RETURN w.transectionHistory")
    String getAllTransectionHistory(String emailId);
    @Query("MERGE (u:User {emailId: $emailId}) " +
            "MERGE (u)-[:walletAmount]->(w:Wallet) " +
            "SET w.availableAmt = 0.0f " +
            "RETURN w.availableAmt")
    void setAmtValue(@Param("emailId") String emailId);

}

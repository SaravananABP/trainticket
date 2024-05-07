package com.booking.trainticket.Authentication.Repository;

import com.booking.trainticket.Authentication.Pojo.UserDetailsInfo;
import com.booking.trainticket.Authentication.Service.UserInfoUserDetails;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.query.Query;


@EnableNeo4jRepositories
public interface UserDetailsRepo extends Neo4jRepository<UserDetailsInfo, Long> {
    @Query("match(u:User{emailId:$emailId}) return u")
    UserDetailsInfo findByEmailId(String emailId);
    @Query("match(u:User{emailId:$emailId}) set u.role=$role return u")
    UserDetailsInfo updateRole(String emailId,String role);
    @Query("match(u:User{userName:$userName}) return u")
    UserDetailsInfo findByUserName(String userName);
    @Query("match(u:User{emailId:$emailId}) return u")
    UserDetailsInfo findByMobileNo(Long mobile);
    @Query("match(u:User{emailId:$emailId}) set u.name=$name return u.name")
    String UpdateUserName(String emailId,String name);
    @Query("match(u:User{emailId:$emailId}) set u.mobileNo=$name return u.name")
    String UpdateUserMobile(String emailId,Long name);
    @Query("match(u:User{emailId:$emailId}) set u.address=$name return u.name")
    String UpdateUserAddress(String emailId,String name);
    @Query("match(u:User{emailId:$emailId}) set u.password=$name return u.name")
    String UpdateUserPassword(String emailId,String name);
    @Query("match(u:User{emailId:$emailId}) set u.DateOfbirth=$name return u.name")
    String UpdateUserDateOfBirth(String emailId,String name);


}

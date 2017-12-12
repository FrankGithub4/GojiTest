package goji;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByUserID(String userID);

//    SELECT * FROM Message
//    INNER JOIN
//            (SELECT COUNT (*) AS recommenderCount, m.messageID from
//            (SELECT Recommendation.messageID from Recommendation
//                    INNER JOIN
//                    Message
//                    WHERE Message.messageID = Recommendation.messageID
//                    AND Message.userID = :userID) as m
//    GROUP BY m.messageID
//    ORDER BY recommenderCount DESC LIMIT
    @Query(value = "SELECT * FROM Message " +
            "INNER JOIN " +
            "(SELECT COUNT (*) AS recommenderCount, m.messageID from " +
            "(SELECT Recommendation.messageID from Recommendation " +
            "INNER JOIN " +
            "Message " +
            "WHERE Message.messageID = Recommendation.messageID " +
            "AND Message.userID = :userID) as m " +
            "GROUP BY m.messageID " +
            "ORDER BY recommenderCount DESC LIMIT 1) AS m1 " +
            "WHERE Message.messageID = m1.messageID" , nativeQuery = true)
    List<Message> findMostRecommendedMessageFromUser(@Param("userID") String userID);
}

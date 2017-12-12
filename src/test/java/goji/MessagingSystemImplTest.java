package goji;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MessagingSystemImplTest {

    @Autowired
    private MessagingSystem messagingSystem;

    @Test
    public void saveUserMessage() {
        String userID = "1";
        String messageContents = "hi";
        Message message = messagingSystem.saveUserMessage(userID, messageContents);
        //Simple test to see if saveUserMessage saves a message to storage.
        Assert.assertEquals(userID, message.getUserID());
        Assert.assertEquals(messageContents, message.getContents());
    }

    @Test
    public void getUserMessages() {

        String user1ID = "1";
        String user2ID = "2";

        Message message1 = messagingSystem.saveUserMessage(user1ID, "Message 1");

        List<Message> user1Messages = messagingSystem.getUserMessages(user1ID);
        List<Message> user2MessagesEmpty = messagingSystem.getUserMessages(user2ID);
        //Tests that if there is a single message sent that the getUserMessages method returns a list of size 1 for the userID of the user who sent it
        //And an empty list for another userID
        Assert.assertEquals(1, user1Messages.size());
        Assert.assertTrue(user2MessagesEmpty.isEmpty());
        Assert.assertEquals(message1, user1Messages.get(0));

        Message message2 = messagingSystem.saveUserMessage(user2ID, "Message 2");
        Message message3 = messagingSystem.saveUserMessage(user1ID, "Message 3");
        Message message4 = messagingSystem.saveUserMessage(user2ID, "Message 4");
        Message message5 = messagingSystem.saveUserMessage(user1ID, "Message 5");

        List<Message> user1Messages1 = messagingSystem.getUserMessages(user1ID);
        List<Message> user2Messages = messagingSystem.getUserMessages(user2ID);

        //Tests that when two users have sent amultiple messages, the getUserMessages returns the correct number of messages and the correct messages are returned for each user
        Assert.assertEquals(3, user1Messages1.size());
        Assert.assertEquals(2, user2Messages.size());
        Assert.assertEquals(message1, user1Messages1.get(0));
        Assert.assertEquals(message3, user1Messages1.get(1));
        Assert.assertEquals(message5, user1Messages1.get(2));
        Assert.assertEquals(message2, user2Messages.get(0));
        Assert.assertEquals(message4, user2Messages.get(1));

    }

    @Test
    public void saveUserRecommendation() {
        String recommenderID = "2";
        Message message = messagingSystem.saveUserMessage("1", "Hi");
        Recommendation recommendation = messagingSystem.saveUserRecommendation(recommenderID, message);
        //Simple test to see if saveUserRecommendation saves a recommendation to storage.
        Assert.assertEquals(recommenderID, recommendation.getRecommenderID());
        Assert.assertEquals(message, recommendation.getMessageID());
    }

    @Test
    public void getUsersWhoRecommended() {
        String user1ID = "1";
        String user2ID = "2";
        String user3ID = "3";
        String message1Contents = "hi Alice";
        String message2Contents = "hi Chris";
        String message3Contents = "hi Bob";
        Message message1 = messagingSystem.saveUserMessage(user1ID, message1Contents);
        Message message2 = messagingSystem.saveUserMessage(user2ID, message2Contents);
        Message message3 = messagingSystem.saveUserMessage(user3ID, message3Contents);

        List<String> usersWhoReocommend = messagingSystem.getUsersWhoRecommended();

        //Test List returned is empty when there are no users who have made recommendations
        Assert.assertTrue(usersWhoReocommend.isEmpty());


        Recommendation recommendation1 = messagingSystem.saveUserRecommendation(user1ID, message2);
        List<String> usersWhoReocommend1 = messagingSystem.getUsersWhoRecommended();
        //Tests that when one user recommends, that one user is returned, and that the user returned is the one who made the recommendation
        Assert.assertEquals(1, usersWhoReocommend1.size());
        Assert.assertEquals(user1ID, usersWhoReocommend1.get(0));

        Recommendation recommendation2 = messagingSystem.saveUserRecommendation(user1ID, message3);
        List<String> usersWhoReocommend2 = messagingSystem.getUsersWhoRecommended();
        //Tests that when one user has made multiple recommendations, the saveUserRecommendation method returns the user once and only once
        Assert.assertEquals(1, usersWhoReocommend2.size());
        Assert.assertEquals(user1ID, usersWhoReocommend2.get(0));

        Recommendation recommendation3 = messagingSystem.saveUserRecommendation(user2ID, message1);
        List<String> usersWhoReocommend3 = messagingSystem.getUsersWhoRecommended();
        //Tests that when 2 users make recommendations, that two users are returned,
        // and that the usera returned are the ones who made the recommendations (in the order the recommendations werer made)
        Assert.assertEquals(2, usersWhoReocommend3.size());
        Assert.assertEquals(user1ID, usersWhoReocommend3.get(0));
        Assert.assertEquals(user2ID, usersWhoReocommend3.get(1));

    }

    @Test
    public void getMostRecommendedMessage() {

        String user1ID = "1";
        String user2ID = "2";
        String user3ID = "3";

        Message message1 = messagingSystem.saveUserMessage(user1ID, "Message 1");
        Message message2 = messagingSystem.saveUserMessage(user1ID, "Message 2");
        Message message3 = messagingSystem.saveUserMessage(user1ID, "Message 3");

        Message message4 = messagingSystem.saveUserMessage(user2ID, "Message 4");
        Message message5 = messagingSystem.saveUserMessage(user3ID, "Message 5");
        Message message6 = messagingSystem.saveUserMessage(user3ID, "Message 6");

        //Tests that if there are no recommendations for any of a users messages, no message is returned
        //Design decision as spec does not state how to handle this situation, and simply returning an arbitrary message seems pointless
        //In real life would ask writer of spec for how they would like case handled
        Assert.assertTrue(messagingSystem.getMostRecommendedMessageByUser(user1ID).isEmpty());


        Recommendation recommendation1ID = messagingSystem.saveUserRecommendation(user2ID, message2);
        Recommendation recommendation2ID = messagingSystem.saveUserRecommendation(user2ID, message3);
        Recommendation recommendation3ID = messagingSystem.saveUserRecommendation(user3ID, message3);
        Recommendation recommendation4ID = messagingSystem.saveUserRecommendation(user3ID, message2);

        Recommendation recommendation5ID = messagingSystem.saveUserRecommendation(user1ID, message4);
        Recommendation recommendation6ID = messagingSystem.saveUserRecommendation(user2ID, message4);
        Recommendation recommendation7ID = messagingSystem.saveUserRecommendation(user1ID, message5);
        Recommendation recommendation8ID = messagingSystem.saveUserRecommendation(user3ID, message6);
        Recommendation recommendation9ID = messagingSystem.saveUserRecommendation(user3ID, message6);

        List<Message> mostRecommenedMessageUser1 = messagingSystem.getMostRecommendedMessageByUser(user1ID);
        List<Message> mostRecommenedMessageUser2 = messagingSystem.getMostRecommendedMessageByUser(user2ID);
        List<Message> mostRecommenedMessageUser3 = messagingSystem.getMostRecommendedMessageByUser(user3ID);

        //Tests that each user returns their most recommended message
        //user1ID, has two max messages equally recommended, in this case it checks that the message that the oldest message is returned.
        Assert.assertEquals(message2,mostRecommenedMessageUser1.get(0));
        Assert.assertEquals(message4,mostRecommenedMessageUser2.get(0));
        Assert.assertEquals(message6,mostRecommenedMessageUser3.get(0));
    }

}
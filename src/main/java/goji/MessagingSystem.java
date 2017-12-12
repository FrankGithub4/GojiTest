package goji;

import java.util.List;

//interface exposed to users as specified in spec
public interface MessagingSystem {
    Message saveUserMessage(String userID, String message);

    Recommendation saveUserRecommendation(String userID, Message messageID);

    List<Message> getUserMessages(String userID);

    List<String> getUsersWhoRecommended();

    List<Message> getMostRecommendedMessageByUser(String userID);
}

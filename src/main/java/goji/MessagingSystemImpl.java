package goji;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//concrete implementation exposed to users as specified in spec
@Service
public class MessagingSystemImpl implements MessagingSystem {

    private static final Logger logger = LoggerFactory.getLogger(MessagingSystemImpl.class);

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RecommendationRepository recommendationRepository;

    //Records a user sending a message using MessageRepository which extends CrudRepository
    @Override
    public Message saveUserMessage(String userID, String message) {
        return messageRepository.save(new Message(userID,message));
    }

    //Records a user recommending a message using RecommendationRepository which extends CrudRepository
    @Override
    public Recommendation saveUserRecommendation(String userID, Message messageID) {
        return recommendationRepository.save(new Recommendation(userID, messageID));
    }

    //Returns all messages sent by a user
    @Override
    public List<Message> getUserMessages(String userID) {
        return messageRepository.findByUserID(userID);
    }


    //returns all users (represented as Strings) who have made recommendations
    @Override
    public List<String> getUsersWhoRecommended() {
        return recommendationRepository.findDistinctRecommenders();
    }

    //returns a users mos recommended message
    //This is an arbitrary design decision on my part the spec does not give an indication as to how such a situation should be handled, so I went with a simple solution for the
    //purpose of this test. I
    //returns empty list and logs a message flagging this case if the user has made no recommendations.
    //Design decision as spec does not state how to handle this situation, and simply returning an arbitrary message seems pointless
    //In a real life situation I would ask the provider of this spec for clarification on this issue if possible
    @Override
    public List<Message> getMostRecommendedMessageByUser(String userID) {
        List<Message> mostRecommendedMessage = messageRepository.findMostRecommendedMessageFromUser(userID);
        if (mostRecommendedMessage.isEmpty())
            logger.info("No message returned as no recommendations have been made");
        return mostRecommendedMessage;
    }
}

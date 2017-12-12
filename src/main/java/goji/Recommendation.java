package goji;

import javax.persistence.*;
import java.util.Objects;

//Class used to specify entity to represent a user recommending a message
//recommendationID there for sake of convenience and simplicity. Would consider removing and changing to composite key consisting of recommenderID and messageID,
// assuming a user can't recommend the same message twice
//recommenderID user recommending message
//messageID message being recommended. Is foreign key for Message joining two entities
@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long recommendationID;
    private String recommenderID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "messageID")
    private Message messageID;

    public Recommendation() {
        super();
    }

    Recommendation(String recommenderID, Message messageID) {
        this.recommenderID = recommenderID;
        this.messageID = messageID;
    }

    public Long getRecommendationID() {
        return recommendationID;
    }

    public String getRecommenderID() {
        return recommenderID;
    }

    public void setRecommenderID(String recommenderID) {
        this.recommenderID = recommenderID;
    }

    public Message getMessageID() {
        return messageID;
    }

    public void setMessageID(Message messageID) {
        this.messageID = messageID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(recommenderID, that.recommenderID) &&
                Objects.equals(messageID, that.messageID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(recommenderID, messageID);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "recommendationID=" + recommendationID +
                ", recommenderID='" + recommenderID + '\'' +
                ", messageID=" + messageID +
                '}';
    }
}

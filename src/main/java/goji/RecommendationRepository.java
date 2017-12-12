package goji;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface RecommendationRepository extends CrudRepository<Recommendation, Long> {

    @Query(value = "SELECT DISTINCT Recommendation.recommenderID FROM Recommendation", nativeQuery = true)
    List<String> findDistinctRecommenders();
}

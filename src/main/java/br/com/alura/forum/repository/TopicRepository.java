package br.com.alura.forum.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.model.OpenTopicByCategory;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;


public interface TopicRepository extends Repository<Topic, Long>, JpaSpecificationExecutor<Topic> {
	
	@Query("select t from Topic t")
	List<Topic> list();
    List<Topic> findAll();
    
    @Query("SELECT count(topic) FROM Topic topic "
            + "JOIN topic.course course "
            + "JOIN course.subcategory subcategory "
            + "JOIN subcategory.category category "
            + "WHERE category.id = :categoryId")
    Integer countTopicsByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT count(topic) FROM Topic topic "
            + "JOIN topic.course course "
            + "JOIN course.subcategory subcategory "
            + "JOIN subcategory.category category "
            + "WHERE category.id = :categoryId AND topic.status = :status")
    Integer countTopicsByCategoryIdAndStatus(@Param("categoryId") Long categoryId,
                                             @Param("status")TopicStatus status);
    
    @Query("SELECT count(topic) FROM Topic topic "
            + "JOIN topic.course course "
            + "JOIN course.subcategory subcategory "
            + "JOIN subcategory.category category "
            + "WHERE category.id = :categoryId AND topic.creationInstant > :lastWeek")
    Integer countLastWeekTopicsByCategoryId(@Param("categoryId") Long categoryId,
                                            @Param("lastWeek")Instant lastWeek);
    
    void save(Topic topic);
    
    List<Topic> findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(User owner, Instant oneHourAgo);


    Optional<Topic> findById(Long topicId);
    
    @Query("select new br.com.alura.forum.model.OpenTopicByCategory(" +
    		"t.course.subcategory.category.name as categoryName, " +
    		"count(t) as topicCount, " +
    		"now() as instant) from Topic t " +
    		"where t.status = 'NOT_ANSWERED' " +
    		"group by t.course.subcategory.category")
    		List<OpenTopicByCategory> findOpenTopicsByCategory();
	
	
	
	

}

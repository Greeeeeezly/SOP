package com.example.defaultconsumer.repositiries;


import com.example.defaultconsumer.models.Subscription;
import com.example.defaultconsumer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query(value = "SELECT s.user FROM Subscription s WHERE s.tour.id = :tourId AND s.user.priority = false ")
    List<User> findDefaultSubscribersByTourId(@Param("tourId") Long tourId);

    @Modifying
    @Query("DELETE FROM Subscription s WHERE s.user.id = :userId AND s.tour.id = :tourId")
    void deleteByUserIdAndTourId(Long userId, Long tourId);

    @Modifying
    @Query("DELETE FROM Subscription s WHERE s.tour.id = :tourId")
    void deleteAllByTourId(Long tourId);
}

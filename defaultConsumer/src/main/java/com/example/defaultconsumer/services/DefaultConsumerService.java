package com.example.defaultconsumer.services;

import com.example.defaultconsumer.models.Tour;
import com.example.defaultconsumer.models.User;
import com.example.defaultconsumer.repositiries.SubscriptionRepository;
import com.example.defaultconsumer.repositiries.TourRepository;
import com.example.defaultconsumer.repositiries.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
@Service
public class DefaultConsumerService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final RabbitTemplate rabbitTemplate;
    private final BookingService bookingService;
    private final Random random;

    public DefaultConsumerService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, TourRepository tourRepository, RabbitTemplate rabbitTemplate, BookingService bookingService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.bookingService = bookingService;
        this.random = new Random();
    }

    @Bean
    public Queue myQueue() {
        System.out.println("Creating queue: defaultQueue");
        return new Queue("defaultQueue", true);
    }

    @Transactional
    @RabbitListener(queues = "defaultQueue")
    public void receiveNotification(Long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NoSuchElementException("Tour with ID " + tourId + " not found"));
        List<User> defaultUsers = subscriptionRepository.findDefaultSubscribersByTourId(tourId);

        if (!defaultUsers.isEmpty()) {
            for (User user : defaultUsers) {
                if (random.nextInt(100) < 25) {
                    bookingService.createBooking(tourId, user.getId());
                    subscriptionRepository.deleteByUserIdAndTourId(user.getId(), tourId);
                    System.out.println("Обычный пользователь " + user.getName() + " забронировал тур номер " + tourId);
                    rabbitTemplate.convertAndSend("notificationExchange", "notification.wsdefault", "default user :" +user.getId() + " booked " + tourId);
                    return;
                }
            }
        }
        tour.setAvailableSeats(tour.getAvailableSeats() + 1);
        tourRepository.save(tour);
        subscriptionRepository.deleteAllByTourId(tourId);
        System.out.println("Удаление завершено.");
        rabbitTemplate.convertAndSend("notificationExchange", "notification.wsdefault", "nobody booked, subscriptions deleted for tour "+ tourId);

    }
}

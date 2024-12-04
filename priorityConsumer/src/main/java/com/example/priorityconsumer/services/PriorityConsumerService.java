package com.example.priorityconsumer.services;

import com.example.priorityconsumer.models.User;
import com.example.priorityconsumer.repositories.SubscriptionRepository;
import com.example.priorityconsumer.repositories.TourRepository;
import com.example.priorityconsumer.repositories.UserRepository;
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
public class PriorityConsumerService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final RabbitTemplate rabbitTemplate;
    private final BookingService bookingService;
    private final Random random;


    public PriorityConsumerService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, TourRepository tourRepository, RabbitTemplate rabbitTemplate, BookingService bookingService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.bookingService = bookingService;
        this.random = new Random();
    }
    @Bean
    public Queue myQueue() {
        return new Queue("priorityQueue", true);
    }
    @Transactional
    @RabbitListener(queues = "priorityQueue")
    public void receiveNotification(Long tourId) {
        List<User> priorityUsers = subscriptionRepository.findPrioritySubscribersByTourId(tourId);

        if (!priorityUsers.isEmpty()) {
            for (User user : priorityUsers) {
                if (random.nextInt(100) < 25) {  // 25% шанс
                    bookingService.createBooking(tourId, user.getId());
                    subscriptionRepository.deleteByUserIdAndTourId(user.getId(), tourId);
                    System.out.println("Приоритетный пользователь " + user.getName() + " забронировал тур номер " + tourId);
                    return;
                }
            }
        }
        rabbitTemplate.convertAndSend("notificationExchange", "notification.default", tourId);
        rabbitTemplate.convertAndSend("notificationExchange", "notification.wsdefault", "priority users ignored tour :" + tourId);
    }
}

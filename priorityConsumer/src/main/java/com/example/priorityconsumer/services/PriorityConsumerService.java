package com.example.priorityconsumer.services;

import com.example.priorityconsumer.UserRequest;
import com.example.priorityconsumer.UserResponse;
import com.example.priorityconsumer.grpcClient.GrpcClient;
import com.example.priorityconsumer.models.User;
import com.example.priorityconsumer.repositories.SubscriptionRepository;
import com.example.priorityconsumer.repositories.TourRepository;
import com.example.priorityconsumer.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class PriorityConsumerService {

    private final GrpcClient grpcClient;

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final RabbitTemplate rabbitTemplate;
    private final BookingService bookingService;
    private final Random random;


    public PriorityConsumerService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, TourRepository tourRepository, RabbitTemplate rabbitTemplate, BookingService bookingService, GrpcClient grpcClient) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.bookingService = bookingService;
        this.random = new Random();
        this.grpcClient = grpcClient;
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
                // Проверяем, активен ли приоритетный пользователь через gRPC
                //UserRequest request = UserRequest.newBuilder().setUserId(user.getId()).build();
                UserResponse response = grpcClient.checkUserActive(user.getId());
                System.out.println(response.getMessage());
                if (response.getIsActive()) {  // Если активен
                    if (random.nextInt(100) < 25) {  // 25% шанс
                        bookingService.createBooking(tourId, user.getId());
                        subscriptionRepository.deleteByUserIdAndTourId(user.getId(), tourId);
                        System.out.println("Приоритетный пользователь " + user.getName() + " забронировал тур номер " + tourId);
                        return;
                    }
                } else {
                    System.out.println("Приоритетный пользователь " + user.getName() + " не активен или не оплатил.");
                }
            }
        }
        rabbitTemplate.convertAndSend("notificationExchange", "notification.default", tourId);
        rabbitTemplate.convertAndSend("notificationExchange", "notification.wsdefault", "priority users ignored tour :" + tourId);
    }
}

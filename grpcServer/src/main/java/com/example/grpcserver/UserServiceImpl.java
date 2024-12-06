package com.example.grpcserver;

import com.example.grpcserver.models.User;
import com.example.grpcserver.repositories.UserRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void checkUserActive(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        Long userId = request.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверка активности только для приоритетных пользователей
        if (user.isPriority() && user.isActive()) {
            UserResponse response = UserResponse.newBuilder()
                    .setIsActive(true)
                    .setMessage("User is active and eligible for booking"+ userId)
                    .build();
            responseObserver.onNext(response);
        } else if (!user.isPriority()) {
            UserResponse response = UserResponse.newBuilder()
                    .setIsActive(false)
                    .setMessage("User is not a priority user" + userId)
                    .build();
            responseObserver.onNext(response);
        } else {
            UserResponse response = UserResponse.newBuilder()
                    .setIsActive(false)
                    .setMessage("User is not active" + userId)
                    .build();
            responseObserver.onNext(response);
        }
        System.out.println("server works");
        responseObserver.onCompleted();
    }
}

package com.example.priorityconsumer.grpcClient;

import com.example.priorityconsumer.UserRequest;
import com.example.priorityconsumer.UserResponse;
import com.example.priorityconsumer.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GrpcClient {

    private final ManagedChannel channel;
    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    public GrpcClient(@Value("${grpc.server.host}") String grpcServerHost,
                      @Value("${grpc.server.port}") int grpcServerPort) {
        // Создаем gRPC канал с адресом сервера
        this.channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()  // Отключение SSL
                .build();

        // Создаем gRPC клиент
        this.userServiceBlockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserResponse checkUserActive(Long userId) {
        UserRequest request = UserRequest.newBuilder()
                .setUserId(userId)
                .build();

        System.out.println("Отправлен запрос в gRPC сервер");
        return userServiceBlockingStub.checkUserActive(request);
    }

    public void shutdown() {
        channel.shutdown();
    }
}

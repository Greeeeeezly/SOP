package com.example.grpcserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class GrpcServer {

    private final int port = 50051; 
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public GrpcServer(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostConstruct
    public void start() throws Exception {
        // Создание и запуск сервера gRPC
        Server server = ServerBuilder.forPort(port)
                .addService(userServiceImpl)  // Используем зависимость, внедрённую через Spring
                .build()
                .start();

        System.out.println("gRPC Server started on port " + port);
        server.awaitTermination();
    }
}

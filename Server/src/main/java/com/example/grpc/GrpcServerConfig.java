package com.example.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port:9090}")
    private int grpcPort;

    @Autowired
    private PrizeServiceImpl prizeService;

    private Server grpcServer;

    @Bean
    public Server grpcServer() throws IOException {
        grpcServer = NettyServerBuilder.forAddress(new InetSocketAddress("0.0.0.0", 9090))
                .addService(prizeService)
                .build()
                .start();

        System.out.println("gRPC Server started on 0.0.0.0:" + grpcPort + " (accessible from all interfaces)");
        return grpcServer;
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (grpcServer != null) {
            grpcServer.shutdown();
            System.out.println("gRPC Server stopped.");
        }
    }
}

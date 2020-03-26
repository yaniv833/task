package com.yaniv.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static String FILE_PATH = "data.json";

    public static void main(String[] args) throws IOException, InterruptedException {
        DataServiceImpl dataService = new DataServiceImpl();
        dataService.loadData(FILE_PATH);
        Server server = ServerBuilder
                .forPort(8000)
                .addService(dataService).build();

        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started!");
        server.awaitTermination();
    }
}

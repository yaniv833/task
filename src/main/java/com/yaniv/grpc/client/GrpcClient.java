package com.yaniv.grpc.client;

import com.yaniv.grpc.DataFetchRequest;
import com.yaniv.grpc.DataServiceGrpc;
import com.yaniv.grpc.Entity;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        DataServiceGrpc.DataServiceBlockingStub stub
                = DataServiceGrpc.newBlockingStub(channel);

        Iterator<Entity> entities = stub.fetch(DataFetchRequest.newBuilder().build());

        System.out.println("Response received from server:\n");
        while (entities.hasNext()) {
            System.out.println(entities.next().toString());
        }

        channel.shutdown();
    }
}

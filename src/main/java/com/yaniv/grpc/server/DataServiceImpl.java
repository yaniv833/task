package com.yaniv.grpc.server;

import com.yaniv.grpc.DataFetchRequest;
import com.yaniv.grpc.DataResponse;
import com.yaniv.grpc.DataServiceGrpc;
import com.yaniv.grpc.Version;
import io.grpc.stub.StreamObserver;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.FileReader;


public class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {
    @Override
    public void fetch(
            DataFetchRequest request, StreamObserver<DataResponse> responseObserver){
        JSONParser parser = new JSONParser();
        try{
            JSONObject data = (JSONObject) parser.parse(new FileReader("data.json"));
            JSONArray entities = (JSONArray) data.get("entities");
            for(Object obj : entities){
                JSONObject jsonObject = (JSONObject) obj;
                String entity = new StringBuilder()
                        .append(jsonObject.get("name"))
                        .append(jsonObject.get("type"))
                        .append(jsonObject.get("versions"))
                        .toString();
                JSONObject versions = (JSONObject) jsonObject.get("versions");
                Version version = Version.newBuilder()
                        .setName(versions.get("name").toString())
                        .setSizeBytes(Integer.valueOf(versions.get("size_bytes").toString()))
                        .build();
                DataResponse response = DataResponse.newBuilder()
                        .setName(jsonObject.get("name").toString())
                        .setType(jsonObject.get("type").toString())
                        .setVersions(version)
                        .build();
                responseObserver.onNext(response);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
        responseObserver.onCompleted();
    }
}

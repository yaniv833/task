package com.yaniv.grpc.server;

import com.yaniv.grpc.DataFetchRequest;
import com.yaniv.grpc.Entity;
import com.yaniv.grpc.DataServiceGrpc;
import com.yaniv.grpc.Version;
import io.grpc.stub.StreamObserver;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {
    List<Entity> entities;

    @Override
    public void fetch(
            DataFetchRequest request, StreamObserver<Entity> responseObserver) {
        for (Entity entity : entities) {
            responseObserver.onNext(entity);
        }
        responseObserver.onCompleted();
    }

    /**
     * load only once the data to the memory when the server rises.
     */
    public void loadData(String filename) {
        entities = new ArrayList<Entity>();
        JSONParser parser = new JSONParser();
        try {
            JSONObject data = (JSONObject) parser.parse(new FileReader(filename));
            JSONArray entitiesJson = (JSONArray) data.get("entities");
            for (Object obj : entitiesJson) {
                JSONObject jsonObject = (JSONObject) obj;
                JSONObject versions = (JSONObject) jsonObject.get("versions");
                Version version = Version.newBuilder()
                        .setName(versions.get("name").toString())
                        .setSizeBytes(Integer.valueOf(versions.get("size_bytes").toString()))
                        .build();
                Entity entity = Entity.newBuilder()
                        .setName(jsonObject.get("name").toString())
                        .setType(jsonObject.get("type").toString())
                        .setVersions(version)
                        .build();
                entities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

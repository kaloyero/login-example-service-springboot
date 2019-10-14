package com.example.springsocial.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class JsonCodec {
    private static ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();


    public static <T> String toJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {

        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json != null) {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (IOException e) {

            }
        }
        return null;
    }

    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        try {
            //return objectMapper.readValue(json, new TypeReference<List<T>>(){});
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {

        }
        return null;
    }


    public static PGobject convertToJsonB(Object object) {
        try {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(JsonCodec.toJson(object));
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static PGobject convertToJsonbIfIsNotNull(Object object) {
        if (object == null) {
            return null;
        } else {
            return convertToJsonB(object);
        }
    }


}
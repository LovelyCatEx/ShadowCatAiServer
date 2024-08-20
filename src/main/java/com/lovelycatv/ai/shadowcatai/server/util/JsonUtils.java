package com.lovelycatv.ai.shadowcatai.server.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * JSON utilities
 *
 * @author vains
 * @since 2020-11-10
 */
@Slf4j
public final class JsonUtils {

    private JsonUtils() {
        throw new UnsupportedOperationException("Utility classes cannot be instantiated.");
    }

    public final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MAPPER.registerModule(new Jdk8Module());
        MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * JSON string to object
     *
     * @param json json
     * @param clazz T
     * @param <T> T
     * @return Instance of T
     */
    public static <T> T jsonCovertToObject(String json, Class<T> clazz) {
        if (json == null || clazz == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Could not parse object: ", e);
        }
        return null;
    }

    /**
     * JSON string to object
     * @param json json
     * @param type Type
     * @param <T> T
     * @return Instance of T
     */
    public static <T> T jsonCovertToObject(String json, TypeReference<T> type) {
        if (json == null || type == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("Could not parse object: ", e);
        }
        return null;
    }

    /**
     * Convert stream to object
     * @param inputStream InputStream
     * @param clazz Class
     * @param <T> T
     * @return Instance
     */
    public static <T> T covertStreamToObject(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null || clazz == null) {
            return null;
        }
        try {
            return MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("Could not parse object: ", e);
        }
        return null;
    }

    /**
     * JSON string to List
     *
     * @param json json
     * @param collectionClazz Collection class
     * @param elementsClazz Collection generic class
     * @param <T> T
     * @return Instance of T
     */
    public static <T> T jsonCovertToObject(String json, Class<?> collectionClazz, Class<?> ... elementsClazz) {
        if (json == null || collectionClazz == null || elementsClazz == null) {
            return null;
        }
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(collectionClazz, elementsClazz);
            return MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            log.error("Could not parse object: ", e);
        }
        return null;
    }

    /**
     * Object to JSON string
     *
     * @param o Object to be converted
     * @return JSON string
     */
    public static String objectCovertToJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return o instanceof String ? (String) o : MAPPER.writeValueAsString(o);
        } catch (IOException e) {
            log.error("Could not parse object: ", e);
        }
        return null;
    }

    /**
     * Convert an object to another
     *
     * @param o Object to be converted
     * @param collectionClazz Collection class
     * @param elementsClazz Collection generic class
     * @param <T> T
     * @return Instance of T
     */
    public static  <T> T objectCovertToObject(Object o, Class<?> collectionClazz, Class<?>... elementsClazz) {
        String json = objectCovertToJson(o);
        return jsonCovertToObject(json, collectionClazz, elementsClazz);
    }

}

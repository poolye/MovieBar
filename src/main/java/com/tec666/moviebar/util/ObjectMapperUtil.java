package com.tec666.moviebar.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author longge93
 */
public class ObjectMapperUtil {

    private static ObjectMapper objectMapper;

    private synchronized static ObjectMapper init() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public static ObjectMapper get() {
        if (objectMapper == null) {
            return init();
        }
        return objectMapper;
    }
}

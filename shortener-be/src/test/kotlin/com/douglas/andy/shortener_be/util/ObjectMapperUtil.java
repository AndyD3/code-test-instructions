package com.douglas.andy.shortener_be.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperUtil {

    private final ObjectWriter ow;

    public ObjectMapperUtil() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.findAndRegisterModules();
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    public String getJson(Object object) throws JsonProcessingException {
        return ow.writeValueAsString(object);
    }
}

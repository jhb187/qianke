/**
 * 
 */
package com.qianke.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: json工具类
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Component
public class JsonUtil {
	 private static ObjectMapper mapper = new ObjectMapper();

	    static {
	        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
	        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
	        mapper.configure(JsonParser.Feature.ALLOW_YAML_COMMENTS, true);
	        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
	        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
	        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
	        mapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
	        mapper.configure(JsonParser.Feature.ALLOW_TRAILING_COMMA, true);
	    }

	    public static <T> T parse(String source, Class<T> clz) {
	        try {
	            return mapper.readValue(source, clz);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public static <T> T parse(String source, TypeReference typeReference) {
	        try {
	            return mapper.readValue(source, typeReference);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public static <T> T parse(InputStream source, Class<T> clz) {
	        try {
	            return mapper.readValue(source, clz);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public static <T> T parse(InputStream source, TypeReference typeReference) {
	        try {
	            return mapper.readValue(source, typeReference);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public static String toString(Object object) {
	        try {
	            return mapper.writeValueAsString(object);
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    public static Map toMap(Object object) {
	        return mapper.convertValue(object, Map.class);
	    }

	    public static <T> T toBean(Object object, Class<T> clz) {
	        return mapper.convertValue(object, clz);
	    }
}

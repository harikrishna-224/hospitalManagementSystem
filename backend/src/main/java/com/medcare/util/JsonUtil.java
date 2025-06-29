package com.medcare.util;

import com.medcare.model.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonUtil {
    
    public static String toJson(Object obj) {
        if (obj == null) return "null";
        
        if (obj instanceof String) {
            return "\"" + escapeString((String) obj) + "\"";
        }
        
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        
        if (obj instanceof LocalDate) {
            return "\"" + ((LocalDate) obj).format(DateTimeFormatter.ISO_LOCAL_DATE) + "\"";
        }
        
        if (obj instanceof LocalTime) {
            return "\"" + ((LocalTime) obj).format(DateTimeFormatter.ISO_LOCAL_TIME) + "\"";
        }
        
        if (obj instanceof LocalDateTime) {
            return "\"" + ((LocalDateTime) obj).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"";
        }
        
        if (obj instanceof Enum) {
            return "\"" + ((Enum<?>) obj).name().toLowerCase() + "\"";
        }
        
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(list.get(i)));
            }
            sb.append("]");
            return sb.toString();
        }
        
        // Handle objects using reflection
        return objectToJson(obj);
    }
    
    private static String objectToJson(Object obj) {
        StringBuilder sb = new StringBuilder("{");
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        boolean first = true;
        
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null) {
                    if (!first) sb.append(",");
                    sb.append("\"").append(field.getName()).append("\":");
                    sb.append(toJson(value));
                    first = false;
                }
            } catch (IllegalAccessException e) {
                // Skip this field
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
    
    private static String escapeString(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> clazz) {
        // Simple JSON parsing - in production, use a proper JSON library
        if (clazz == Map.class) {
            return (T) parseJsonObject(json);
        }
        
        // For now, return a basic map for all objects
        return (T) parseJsonObject(json);
    }
    
    private static Map<String, Object> parseJsonObject(String json) {
        Map<String, Object> result = new HashMap<>();
        
        // Remove outer braces
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
        }
        
        // Simple parsing - split by commas (this is very basic)
        String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim();
                
                // Remove quotes from string values
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                
                result.put(key, value);
            }
        }
        
        return result;
    }
}
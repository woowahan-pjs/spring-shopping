package helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ConverterHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toDto(String request, Class<T> type) {
        try {
            return objectMapper.registerModule(new JavaTimeModule()).readValue(request, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

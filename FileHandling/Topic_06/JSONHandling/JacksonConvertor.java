package FileHandling.Topic_06.JSONHandling;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonConvertor {

    /**
     * Serialize: List of Java Objects -> JSON array -> writes to file
     * @param objects - list of objects to serialize
     * @param path    - file path to write the JSON into
     * @return the JSON array string representation
     */
    public static <T> String serialize(List<T> objects, Path path) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonStr = mapper.writeValueAsString(objects); // List -> JSON string
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), objects); // List -> pretty JSON -> file
            return jsonStr;

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        return null;
    }

    /**
     * Deserialize: read JSON array file -> List of Java objects
     * @param path  - file path containing a JSON array
     * @param clazz - element type (e.g. Student.class)
     *              NOTE: TypeReference<List<T>> is anonymous so Jackson can capture
     *              the generic type at runtime, working around type erasure.
     * @return list of deserialized objects, or null if an error occurs
     */
    public static <T> List<T> deserialize(Path path, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // constructCollectionType builds the full List<T> type Jackson needs at runtime
            return mapper.readValue(
                path.toFile(),
                mapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        return null;
    }
}
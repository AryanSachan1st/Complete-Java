package FileHandling.Topic_06.JSONHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Utility class for converting Java objects to/from JSON using Gson.
 * Supports lists of any class (Student, Employee, etc.)
 */
public class GsonConvertor {

    /**
     * Serialize: List of Java Objects -> JSON array -> writes to file
     * @param objects - list of objects to serialize
     * @param path    - file path to write the JSON into
     * @return the JSON array string representation
     */
    public static <T> String serialize(List<T> objects, Path path) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonStr = gson.toJson(objects);

        // Overwrite the file (no APPEND) so the result is always a valid JSON array
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(jsonStr);
            System.out.println("JSON file saved!");
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
     *              NOTE: TypeToken.getParameterized() builds the full List<T> type at
     *              runtime, avoiding the type-erasure pitfall of TypeToken<List<T>>{}.
     * @return list of deserialized objects, or null if an error occurs
     */
    public static <T> List<T> deserialize(Path path, Class<T> clazz) {
        Gson gson = new Gson();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(reader, listType);

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        return null;
    }
}
package FileHandling.Topic_06.YAMLHandling;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Path;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.inspector.TagInspector;

public class YAMLConvertor {
    // Reading YAML -> Java object
    public static <T> T readYAML(Path path, Class<T> clazz) {
        // Allow global tags for our own package (SnakeYAML 2.x security requires explicit whitelist)
        LoaderOptions loaderOptions = new LoaderOptions();
        TagInspector tagInspector = (Tag tag) -> tag.getClassName().startsWith("FileHandling.Topic_06.YAMLHandling");
        loaderOptions.setTagInspector(tagInspector);
        Yaml yaml = new Yaml(new Constructor(clazz, loaderOptions));

        try (FileInputStream fis = new FileInputStream(path.toFile());) {
            T rootObject = yaml.load(fis); // load file data to rootObject
            return rootObject;

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        return null;
    }

    // Writing Java object -> YAML file
    public static <T> void writeToYAML(T rootObject, Path path) {
        // Setting up the writing style which we want
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        // Storing those writing configurations
        Yaml yaml = new Yaml(options);

        // Write to the file
        try (FileWriter writer = new FileWriter(path.toFile())) {
            yaml.dump(rootObject, writer); // dump rootObject into file
            System.out.println("YAML written successfully");
            return;

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        System.out.println("Writing YAML failed!");
    }
}

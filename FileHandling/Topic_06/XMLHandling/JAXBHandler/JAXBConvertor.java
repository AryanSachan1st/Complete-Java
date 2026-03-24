package FileHandling.Topic_06.XMLHandling.JAXBHandler;

import java.nio.file.Path;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class JAXBConvertor {
    // Marshal (Java Object -> XML) [write]
    public static <T> void marshal(Class<T> clazz, T rootObj, Path outputPath) {
        JAXBContext classContext;
        try {
            classContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = classContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(rootObj, outputPath.toFile());

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        
    }

    // Unmarshal (XML -> Java Object) [read]
    public static <T> T unmarshal(Class<T> clazz, Path inputPath) {
        try {
            JAXBContext classContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = classContext.createUnmarshaller();
            @SuppressWarnings("unchecked")
            T rootObj = (T) unmarshaller.unmarshal(inputPath.toFile());
            
            return rootObj;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return null;
    }
}

package FileHandling.Topic_06.XMLHandling.DOMHandler;

import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class DOMConvertor {
    // Read the document from a file
    public static Document readXML(Path path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(path.toFile());
            doc.getDocumentElement().normalize();

            return doc;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return null;

    }

    // Build document -> Write that document in a file [have to create it separate everytime]
    public static void writeStudentToXML(Path path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // global scope (doc)
            Document doc = builder.newDocument();

            // root tag
            Element root = doc.createElement("School");
            doc.appendChild(root);

            // attribute tag
            Element student = doc.createElement("Student");
            student.setAttribute("id", "5023");
            doc.appendChild(student);

            // attribute tag's 1st child's 
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode("Aryan"));
            student.appendChild(name);

            // attribute tag's 1st child's 
            Element age = doc.createElement("age");
            age.appendChild(doc.createTextNode("21"));
            student.appendChild(age);

            Element hobby = doc.createElement("character");
            hobby.appendChild(doc.createTextNode("ninja archer"));
            doc.appendChild(hobby);

            // Write to file
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // pull source, pull destination -> transform source to destination
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(path.toFile());
            transformer.transform(source, result);

            System.out.println("XML Written Successful");

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}

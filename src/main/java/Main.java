import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String... args) {

        String fileName = "src/main/resources/data.xml";
        String outputFileName = "src/main/resources/output.txt";
        writeString(Objects.requireNonNull(listToJson(parseXML(fileName))), outputFileName);

    }

    public static List<Employee> parseXML(String fileName) {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = builderFactory.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("employee");
            List<Employee> result = new ArrayList<>();
            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                    String firstname = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastname = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                    result.add(new Employee(id, firstname, lastname, country, age));
                }
            }
            return result;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void writeString(T resource, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(resource.toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static <T> String listToJson(List<T> list) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(list, listType);
    }
}

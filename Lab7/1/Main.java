//var 3
// Denys Gordiichuk
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the file: ");
        String fileName = in.nextLine();
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found");
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        Element root = document.getDocumentElement();
        NodeList units = root.getElementsByTagName("unit");
        NodeList employees = root.getElementsByTagName("employee");
        System.out.println("Units: ");
        for (int i = 0; i < units.getLength(); i++) {
            Element unit = (Element) units.item(i);
            System.out.println("Name: " + unit.getAttribute("name"));
            System.out.println("Head: " + unit.getAttribute("head"));
            System.out.println("Employees: ");
            for (int j = 0; j < employees.getLength(); j++) {
                Element employee = (Element) employees.item(j);
                if (employee.getAttribute("unit").equals(unit.getAttribute("name"))) {
                    System.out.println("Name: " + employee.getAttribute("name"));
                    System.out.println("Surname: " + employee.getAttribute("surname"));
                    System.out.println("Position: " + employee.getAttribute("position"));
                    System.out.println("Salary: " + employee.getAttribute("salary"));
                }
            }
            System.out.println();
        }
    }
}
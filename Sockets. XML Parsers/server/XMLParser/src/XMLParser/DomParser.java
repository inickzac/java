package XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import SerialIzation.Student;
import SerialIzation.Students;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DomParser {

	Document document;

	public DomParser() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(new File("Students.xml"));
	}

	/*
	 * парсим  в начале UpdateDate затем студентов
	 */
	public Students parseStudents() {
		Students students = new Students();
		students.setUpdateDate(
				document.getDocumentElement().getElementsByTagName("UpdateDate").item(0).getTextContent());
		students.setStudents(studentsBypass(document.getDocumentElement().getElementsByTagName("Student")));
		return students;
	}

	private ArrayList<Student> studentsBypass(NodeList studentElements) {
		ArrayList<Student> students = new ArrayList<Student>();
		for (int i = 0; i < studentElements.getLength(); i++) {
			students.add(studentBypass(studentElements.item(i).getChildNodes()));
		}
		return students;
	}

	private Student studentBypass(NodeList StudentListNode) {
		Student student = new Student();
		for (int i = 0; i < StudentListNode.getLength(); i++) {
			if (StudentListNode.item(i).getNodeName().equals("Faculty")) {
				student.setFaculty(StudentListNode.item(i).getTextContent());
			}
			if (StudentListNode.item(i).getNodeName().equals("Name")) {
				student.setName(StudentListNode.item(i).getTextContent());
			}
			if (StudentListNode.item(i).getNodeName().equals("Surname")) {
				student.setSurname(StudentListNode.item(i).getTextContent());
			}
			if (StudentListNode.item(i).getNodeName().equals("Course")) {
				student.setCourse(Integer.parseInt(StudentListNode.item(i).getTextContent()));
			}
			if (StudentListNode.item(i).getNodeName().equals("ID")) {
				student.setId(Integer.parseInt(StudentListNode.item(i).getTextContent()));
			}
		}

		return student;
	}

}

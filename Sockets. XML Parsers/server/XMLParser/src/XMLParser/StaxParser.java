package XMLParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import SerialIzation.Student;
import SerialIzation.Students;

public class StaxParser {

	String lastElementName;
	private XMLStreamReader xmlReader;
	Students students = new Students();

	public StaxParser() throws FileNotFoundException, XMLStreamException {
		XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
		xmlReader = xmlFactory.createXMLStreamReader(new FileInputStream("Students.xml"));
		students.setStudents(new ArrayList<Student>());
		byPass();
	}

	public Students parseStudent() throws XMLStreamException {
		byPass();
		return students;
	}

	private void byPass() throws XMLStreamException {

		Student student = new Student();
		/*
		 * парсим файл
		 */
		while (xmlReader.hasNext()) {
			int event = xmlReader.next();
			if (event == XMLStreamConstants.START_ELEMENT) {
				lastElementName = xmlReader.getLocalName();
			}
			if (event == XMLStreamConstants.CHARACTERS) {
				if (lastElementName.equals("Name"))
					student.setName(GetInformation());
				if (lastElementName.equals("Surname"))
					student.setSurname(GetInformation());
				if (lastElementName.equals("Faculty"))
					student.setFaculty(GetInformation());
				if (lastElementName.equals("Course"))
					student.setCourse(Integer.parseInt(GetInformation()));
				if (lastElementName.equals("ID"))
					student.setId(Integer.parseInt(GetInformation()));
				if (lastElementName.equals("UpdateDate"))
					students.setUpdateDate(GetInformation());
			}
			if (event == XMLStreamConstants.END_ELEMENT) {
				lastElementName = "";
				if (xmlReader.getLocalName().equals("Student")) {
					students.getStudents().add(student);
					student = new Student();
				}
			}

		}

	}

	private String GetInformation() {
		return new String(xmlReader.getText().trim());

	}
}

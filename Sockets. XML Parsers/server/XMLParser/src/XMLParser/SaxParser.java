package XMLParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import SerialIzation.Student;
import SerialIzation.Students;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxParser {
	
	private  static Students students;	
	
	public static Students parseStudents() throws ParserConfigurationException, SAXException, IOException
	{
		/*
		 *статический конструктор создает экземпл€р класса который парсит файл 
		 */
		students = new Students();
		@SuppressWarnings("unused")
		SaxParser parser = new SaxParser();		
		return students;
	}
	
	private SaxParser() throws ParserConfigurationException, SAXException, IOException {
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser parser = factory.newSAXParser();
	StudentsXMLHandler handler = new StudentsXMLHandler();
	students.setStudents(new ArrayList<Student>());
	parser.parse(new File("Students.xml"), handler);	
    
	}

	private static class StudentsXMLHandler extends DefaultHandler {
		private String  lastElementName;	
		private Student student = new Student();

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			lastElementName = qName;
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String information = new String(ch, start, length);
			information = information.replace("\n", "").trim();

			if (!information.isEmpty()) {
				if (lastElementName.equals("Name"))
					student.setName(information);
				if (lastElementName.equals("Surname"))
					student.setSurname(information);
				if (lastElementName.equals("Faculty"))
					student.setFaculty(information);
				if (lastElementName.equals("Course"))
					student.setCourse(Integer.parseInt(information));
				if (lastElementName.equals("ID"))
					student.setId(Integer.parseInt(information));
				if (lastElementName.equals("UpdateDate"))
					students.setUpdateDate(information);
			}
		}
						
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName == "Student") {
				students.getStudents().add(student);
				student = new Student();
			}
		}
	}
}

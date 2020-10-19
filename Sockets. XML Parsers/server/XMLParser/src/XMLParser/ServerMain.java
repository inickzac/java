package XMLParser;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

import SerialIzation.Student;
import SerialIzation.Students;
import SerialIzation.TypeOfRequestParseXML;

public class ServerMain {

	private static Socket clientSocket; // сокет дл€ общени€
	private static ServerSocket server; // сервер сокет
	private static ObjectInputStream in; // поток чтени€ из сокета
	private static ObjectOutputStream out; // поток записи в сокет

	public static void main(String[] args) {
		try {
			if (validateXMLByXSD(new File("Students.xml"), new File("Students.xsd")))
				System.out.print("”спешна€ валидации\n");						
			startConnect();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * создаем сокет и ожидаем соединений клиента
	 */
	private static void startConnect() throws Exception {
		try {
			try {
				server = new ServerSocket(3000);
				System.out.println("—ервер запущен ќжидание соединени€");
				clientSocket = server.accept();
				try {
					out = new ObjectOutputStream(clientSocket.getOutputStream());
					in = new ObjectInputStream(clientSocket.getInputStream());
					/*
					 * обмениваемс€ информацией с клиентом 
					 */
					communication();
				} finally {
					System.out.println("«акрываем сокет");
					clientSocket.close();
					in.close();
					out.close();
				}
			} finally {
				System.out.println("—ервер закрыт!");
				server.close();
			}
		} catch (EOFException e) {
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * производим валидацию XML по XSD
	 */
	public static boolean validateXMLByXSD(File xml, File xsd) throws SAXException, IOException {
		try {
			SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd).newValidator()
					.validate(new StreamSource(xml));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
	 * принимает от клиента запрашиваемый тип парсера, парсим и передаем распарсеный файл
	 */
	private static void communication() throws Exception {
		while (true) {
			SerialIzation.TypeOfRequestParseXML typeOfRequestParseXML = (TypeOfRequestParseXML) in.readObject();
			if (typeOfRequestParseXML.getTypeParserXML() == TypeOfRequestParseXML.typesParserXML.DOM) {
				DomParser domParser = new DomParser();
				out.writeObject(domParser.parseStudents());
			}
			if (typeOfRequestParseXML.getTypeParserXML() == TypeOfRequestParseXML.typesParserXML.SAX) {
				out.writeObject(SaxParser.parseStudents());
			}
			if (typeOfRequestParseXML.getTypeParserXML() == TypeOfRequestParseXML.typesParserXML.STAX) {
				StaxParser staxParser = new StaxParser();
				out.writeObject(staxParser.parseStudent());
			}
		}
	}

}

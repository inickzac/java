package xmlParserClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import SerialIzation.Student;
import SerialIzation.Students;
import SerialIzation.TypeOfRequestParseXML;

public class ClientMain {
	private static Socket clientSocket; // сокет для общения
	private static ObjectInputStream in; // поток чтения из сокета
	private static ObjectOutputStream out; // поток записи в сокет

	public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
		startClient();
	}

	private static void startClient() throws  InterruptedException, ClassNotFoundException  {
		try {
			try {				
				/*
				 * открываем сокет и обмениваемся информацией с сервером
				 */
				clientSocket = new Socket("localhost", 3000); //
				new BufferedReader(new InputStreamReader(System.in));
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				communication();

			} finally {
				System.out.println("Клиент был закрыт...");
				clientSocket.close();
				in.close();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void communication() throws ClassNotFoundException, IOException, InterruptedException {
		chooseMethodParse();
		showStudents(getStudents());
	}

	/*
	 * выбираем метод парсинга и отправляем тип парсера на сервер
	 */
	private static void chooseMethodParse() throws IOException {
		java.util.Scanner inConsole = new java.util.Scanner(System.in);
		System.out.print("Выберите тип парсера\n1.dom\n2.sax\n3.stax");
		int choice = inConsole.nextInt();
		if (choice == 1) {
			out.writeObject(new TypeOfRequestParseXML(TypeOfRequestParseXML.typesParserXML.DOM));
		}
		if (choice == 2) {
			out.writeObject(new TypeOfRequestParseXML(TypeOfRequestParseXML.typesParserXML.SAX));
		}
		if (choice == 3) {
			out.writeObject(new TypeOfRequestParseXML(TypeOfRequestParseXML.typesParserXML.STAX));
		}
		inConsole.close();
	}
	/*
	 * получаем от сервера распарсеный список студентов
	 */
	private static Students getStudents() throws ClassNotFoundException, IOException {
		SerialIzation.Students students = (SerialIzation.Students) in.readObject();
		return students;
	}

	/*
	 * выводим в консоль студентов
	 */
	private static void showStudents(Students students) {
		System.out.print("Дата обновления списка " + students.getUpdateDate()+ "\n");
		for(Student s:students.getStudents() ) {
			System.out.print("Имя студента " + s.getName()+ "\n");
			System.out.print("Фамилия студента " + s.getSurname()+ "\n");
			System.out.print("Курс " + s.getCourse()+ "\n");
			System.out.print("Факультет " + s.getFaculty()+ "\n");
			System.out.print("ID студента " + s.getId()+ "\n");
		}
	}

}

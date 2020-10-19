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
	private static Socket clientSocket; // ����� ��� �������
	private static ObjectInputStream in; // ����� ������ �� ������
	private static ObjectOutputStream out; // ����� ������ � �����

	public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
		startClient();
	}

	private static void startClient() throws  InterruptedException, ClassNotFoundException  {
		try {
			try {				
				/*
				 * ��������� ����� � ������������ ����������� � ��������
				 */
				clientSocket = new Socket("localhost", 3000); //
				new BufferedReader(new InputStreamReader(System.in));
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				communication();

			} finally {
				System.out.println("������ ��� ������...");
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
	 * �������� ����� �������� � ���������� ��� ������� �� ������
	 */
	private static void chooseMethodParse() throws IOException {
		java.util.Scanner inConsole = new java.util.Scanner(System.in);
		System.out.print("�������� ��� �������\n1.dom\n2.sax\n3.stax");
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
	 * �������� �� ������� ����������� ������ ���������
	 */
	private static Students getStudents() throws ClassNotFoundException, IOException {
		SerialIzation.Students students = (SerialIzation.Students) in.readObject();
		return students;
	}

	/*
	 * ������� � ������� ���������
	 */
	private static void showStudents(Students students) {
		System.out.print("���� ���������� ������ " + students.getUpdateDate()+ "\n");
		for(Student s:students.getStudents() ) {
			System.out.print("��� �������� " + s.getName()+ "\n");
			System.out.print("������� �������� " + s.getSurname()+ "\n");
			System.out.print("���� " + s.getCourse()+ "\n");
			System.out.print("��������� " + s.getFaculty()+ "\n");
			System.out.print("ID �������� " + s.getId()+ "\n");
		}
	}

}

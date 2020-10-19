
package library;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import library.ModelLib.LoginFailedExaption;
import library.ModelLib.accessException;

public class libraryMain {
	static ModelLib libmodel;
	static Scanner in = new Scanner(System.in,"cp866");

	public static void main(String[] args) throws IOException, accessException {
		// TODO Auto-generated method stub
		while (true) {
			if (libmodel == null) {
				try {
					libmodel = new ModelLib(GetDataFromConsole("������� �����"), GetDataFromConsole("������� ������")," ");

				} catch (LoginFailedExaption e) {
					System.out.print(e.getMessage() + "\n");
					continue;
				}

				catch (Exception e) {
					System.out.print(e.getMessage());
				}
			}
			userMenu();
		}
	}

	static String GetDataFromConsole(String requestedData) {
		System.out.print(requestedData + "\n");
		return in.nextLine();
	}

	/*
	 * ������� ������ � ������� � ��������� �����
	 */
	static String GetDataFromConsoleInValidated(String requestedData, int startRange, int endRange) {
		System.out.print(requestedData + "\n");
		int select = in.nextInt();
		in.nextLine();
		while (!(select >= startRange && select <= endRange)) {
			System.out.print("������������ �������� ������� ��� ���\n");
			select = in.nextInt();
		}
		return String.valueOf(select);
	}

	/*
	 * �������� ������ ������������
	 */
	static void createUser() throws accessException, IOException {
		String val = (GetDataFromConsoleInValidated(
				"������� ��� ������������ ��������\n1.������������\n2.�������������", 1, 2));
		if (val.equals("1")) {
			libmodel.createUser(ModelLib.AcType.User, GetDataFromConsole("������� �����"),
					GetDataFromConsole("������� ������"));
		}
		if (val.equals("2")) {
			libmodel.createUser(ModelLib.AcType.Administrator, GetDataFromConsole("������� �����"),
					GetDataFromConsole("������� ������"));
		}
	}

	/*
	 * ������� ���� ���������
	 */
	static void userMenu() throws IOException, accessException {
		String MainMenu = "1.��� ��������� ��������\n2.��� ������ � ��������";
		int endRange = 2;
		if (libmodel.getAcType().equals(ModelLib.AcType.Administrator)) {
			MainMenu += "\n3.��� �������� ����� �� ��������\n4.��� ���������� ����� � �������\n5.��� ���������� ������������";
			endRange = 5;
		}
		String ChooseMenu = GetDataFromConsoleInValidated(MainMenu, 1, endRange);

		if (ChooseMenu.equals("1")) {
			showListInConsole(libmodel.showBooks(0, 30));
		}
		if (ChooseMenu.equals("2")) {
			ArrayList<String> books = libmodel.findBooks(GetDataFromConsole("������� �������� ����� ��� ������"));
			showListInConsole(books);
			System.out.println("���������� ��������� ����:" + books.size());
		}
		if (ChooseMenu.equals("3")) {
			int quantityDelBook = libmodel.deleteBook(GetDataFromConsole("������� �������� ����� ��� ��������"));
			System.out.println("���������� ��������� ����:" + quantityDelBook);
		}
		if (ChooseMenu.equals("4")) {
			libmodel.createBook(GetDataFromConsole("������� �������� ����� ��� ����������"));
			System.out.println("����� ������� ����������");
		}
		if (ChooseMenu.equals("5")) {
			createUser();
			System.out.println("������������ ������� ��������");
		}
	}

	static void showListInConsole(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

}

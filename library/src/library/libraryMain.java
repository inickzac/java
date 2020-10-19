
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
					libmodel = new ModelLib(GetDataFromConsole("Введите логин"), GetDataFromConsole("Введите пароль")," ");

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
	 * Считать данные с консоли с проверкой ввода
	 */
	static String GetDataFromConsoleInValidated(String requestedData, int startRange, int endRange) {
		System.out.print(requestedData + "\n");
		int select = in.nextInt();
		in.nextLine();
		while (!(select >= startRange && select <= endRange)) {
			System.out.print("Неправильное значение введите еще раз\n");
			select = in.nextInt();
		}
		return String.valueOf(select);
	}

	/*
	 * Создание нового пользователя
	 */
	static void createUser() throws accessException, IOException {
		String val = (GetDataFromConsoleInValidated(
				"Введите тип создаваемого аккаунта\n1.Пользователь\n2.Администратор", 1, 2));
		if (val.equals("1")) {
			libmodel.createUser(ModelLib.AcType.User, GetDataFromConsole("Введите логин"),
					GetDataFromConsole("Введите пароль"));
		}
		if (val.equals("2")) {
			libmodel.createUser(ModelLib.AcType.Administrator, GetDataFromConsole("Введите логин"),
					GetDataFromConsole("Введите пароль"));
		}
	}

	/*
	 * Главное меню программы
	 */
	static void userMenu() throws IOException, accessException {
		String MainMenu = "1.Для просмотра каталога\n2.Для поиска в каталоге";
		int endRange = 2;
		if (libmodel.getAcType().equals(ModelLib.AcType.Administrator)) {
			MainMenu += "\n3.Для удаления книги из каталога\n4.Для добавления книги в каталог\n5.Для добавления пользователя";
			endRange = 5;
		}
		String ChooseMenu = GetDataFromConsoleInValidated(MainMenu, 1, endRange);

		if (ChooseMenu.equals("1")) {
			showListInConsole(libmodel.showBooks(0, 30));
		}
		if (ChooseMenu.equals("2")) {
			ArrayList<String> books = libmodel.findBooks(GetDataFromConsole("Введите название книги для поиска"));
			showListInConsole(books);
			System.out.println("Количество найденных книг:" + books.size());
		}
		if (ChooseMenu.equals("3")) {
			int quantityDelBook = libmodel.deleteBook(GetDataFromConsole("Введите название книги для удаления"));
			System.out.println("Количество удаленных книг:" + quantityDelBook);
		}
		if (ChooseMenu.equals("4")) {
			libmodel.createBook(GetDataFromConsole("Введите название книги для добавления"));
			System.out.println("Книга успешно добавленна");
		}
		if (ChooseMenu.equals("5")) {
			createUser();
			System.out.println("Пользователь успешно добавлен");
		}
	}

	static void showListInConsole(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

}

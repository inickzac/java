package library;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/*
 * ��� �������� ������ ����������� ����� � ������ ��� �������� ������������ ������ �� ������ � ������
 * ���� ������� ������ �� �������� ���������� ���������� LoginFailedExaption
 */


public class ModelLib {

	public static enum AcType {
		Administrator, User
	}

	ModelLib.AcType actype;
	String Name = "";
	private String bookFileName = "books";

	public ModelLib(String Login, String Password, String Name) throws LoginFailedExaption {
		try {
			if (!Enter(Login, Password)) {
				throw new LoginFailedExaption();
			}
			if (!new File("books").exists()) {
				createBooksFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ModelLib.AcType getAcType() {
		ModelLib.AcType actype = this.actype;
		return actype;
	}

	/*
	 * ����������� ������ � md5 ���
	 */
	static String GetHash(String NormalString) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			BigInteger bi = new BigInteger(1, messageDigest.digest(NormalString.getBytes()));
			return bi.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	/*
	 * �������� ������ � ������ ��� �����
	 */
	boolean Enter(String Login, String Pass) throws IOException {
		List<String> PassList = Files.readAllLines(Paths.get("pass"));
		for (String Pas : PassList) {
			String[] StrPassMass = Pas.split(" ");
			if (StrPassMass[0].equals(GetHash(Login)) && StrPassMass[1].equals(GetHash(Pass))) {
				if (StrPassMass[2].equals(GetHash(ModelLib.AcType.Administrator.toString()))) {
					actype = ModelLib.AcType.Administrator;
					return true;
				}
				if (StrPassMass[2].equals(GetHash(ModelLib.AcType.User.toString()))) {
					actype = ModelLib.AcType.User;
					return true;
				}
			}
		}
		return false;
	}

	private void CreatePasFile(String LoginAdm, String PassAdm) throws IOException {
		FileOutputStream fos = new FileOutputStream("pass");
		byte[] buf = (GetHash(LoginAdm) + " " + GetHash(PassAdm) + " "
				+ GetHash(ModelLib.AcType.Administrator.toString()) + "\n").getBytes();
		fos.write(buf);
		fos.close();
	}

	/*
	 * ������� ������ ������������
	 */
	public void createUser(ModelLib.AcType AccauntType, String Login, String Password)
			throws accessException, IOException {
		if (!actype.equals(ModelLib.AcType.Administrator)) {
			throw new accessException();
		} else {
			FileOutputStream fos = new FileOutputStream("pass", true);
			byte[] buf = (GetHash(Login) + " " + GetHash(Password) + " " + GetHash(AccauntType.toString()) + "\n")
					.getBytes();
			fos.write(buf);
			fos.close();
		}
	}

	/*
	 * ������� ����� �����
	 */
	public void createBook(String bookName) throws IOException, accessException {
		if (actype.equals(ModelLib.AcType.Administrator)) {
			FileWriter writer = new FileWriter(bookFileName, true);
			writer.write(bookName + "\n");
			writer.close();
		} else {
			throw new accessException();
		}
	}

	/*
	 * ������� ����� 
	 */
	public int deleteBook(String bookName) throws IOException, accessException {
		if (actype.equals(ModelLib.AcType.Administrator)) {
			int quantityOfDelBook = createNewFileWithoutString(bookName);
			File fileOld = new File(bookFileName);
			fileOld.delete();
			File fileNew = new File("copyBook");
			fileNew.renameTo(fileOld);
			return quantityOfDelBook;
		} else {
			throw new accessException();
		}
	}

	/*
	 * ������� ����� ���� ��� ��������� ������
	 */
	private int createNewFileWithoutString(String delString) throws IOException {
		FileWriter writer = new FileWriter("copyBook", true);
		File file = new File(bookFileName);
		Scanner in = new Scanner(file);
		String line = "";
		int quantityOfDelBook = 0;
		while (in.hasNextLine()) {
			if (!(line = in.nextLine()).equals(delString)) {
				writer.write(line+"\n");
			} else {
				quantityOfDelBook++;
			}
		}
		writer.close();
		in.close();
		return quantityOfDelBook;
	}

	/*
	 * ���������� ������ ���� ����
	 */
	public ArrayList<String> showBooks(int startIndex, int endIndex) throws FileNotFoundException {
		int pointer = 0;
		ArrayList<String> booksList = new ArrayList<String>();
		File file = new File(bookFileName);
		Scanner in = new Scanner(file);
		while (in.hasNextLine() && pointer <= endIndex) {
			if (pointer <= startIndex) {
				booksList.add(in.nextLine());
			}
		}
		in.close();
		return booksList;
	}

	/*
	 * ���������� ������ ��������� ����
	 */
	public ArrayList<String> findBooks(String bookName) throws FileNotFoundException {
		ArrayList<String> SearchResultList = new ArrayList<String>();
		File file = new File(bookFileName);
		Scanner in = new Scanner(file);
		while (in.hasNextLine()) {
			String res = in.nextLine();
			if (res.contains(bookName)) {
				SearchResultList.add(bookName);
			}
		}
		in.close();
		return SearchResultList;
	}

	private void createBooksFile() throws IOException {
		FileOutputStream fos = new FileOutputStream(bookFileName);
		fos.write("".getBytes());
		fos.close();
	}

	@SuppressWarnings("serial")
	class LoginFailedExaption extends Exception {

		LoginFailedExaption() {
			super("������������ ����� ��� ������");
		}

	}

	@SuppressWarnings("serial")
	class accessException extends Exception {

		accessException() {
			super("� ��� ��� ������� ��� ������ ��������");
		}
	}

}

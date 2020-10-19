package Port;

import java.util.ArrayList;

/**
 * ������ ������� ��������� � ����� ������ 
 * �������� �������� ���� �������� � ���������� ������ 
 */


public class Ship extends Thread {

	public String name;
	private int quantityOfProduct;
	/**
	 * �������� ���� ��������� ��������
	 */
	private static ArrayList<Ship> shipsList;
	private SeaPort portOfArrival;

	public static enum statusShip {
		waitInLine, toSail, readyForNextTask
	}

	private statusShip status;

	static {
		shipsList = new ArrayList<Ship>();
	}

	public Ship(String name) {
		this.name = name;
		shipsList.add(this);
		status = Ship.statusShip.readyForNextTask;
		quantityOfProduct = getRandom(1000, 100);
	}

	public String getShipName() {
		return name;
	}

	public void reportCompletionOfDownload() {
		this.status = Ship.statusShip.readyForNextTask;
	}

	public int getQuantityOfProductInCargo() {
		return quantityOfProduct;
	}

	public int getRandom(int max, int min) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	/**
	 * ������� ���� �������:
	 * �������� ����
	 * ���� ����� ����������� ��� ��������
	 * ������ �� ������� � ����
	 * ���� ��������� ������ ����� 
	 */
	public void run() {
		try {
			while (true) {
				portOfArrival = choosePortOfArrival();
				status = Ship.statusShip.toSail;				
				waitForTravelTime(getRandom(5000, 100));
				portOfArrival.addInQueue(this);
				status = Ship.statusShip.waitInLine;
				waitPortWork();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void waitPortWork() throws Exception {

		System.out.print("������� " + this.name + " ������� � �������\n");
		while (!status.equals(Ship.statusShip.readyForNextTask)) {
			Thread.sleep(100);
		}

	}

	/**
	 * ����� �������� ���� ����� ��� �������� �������
	 */
	public void Download(int expectedQuantityDownload) {
		quantityOfProduct += expectedQuantityDownload;
		System.out.print("������� " + this.name + " �������� " + expectedQuantityDownload + " ������\n");
	}

	/**
	 * ����� �������� ���� ����� ��� ��������� �������
	 */
	public void Unload(int expectedQuantityUnloading) throws NotEnoughGoodsException {
		if (expectedQuantityUnloading <= quantityOfProduct) {
			quantityOfProduct -= expectedQuantityUnloading;
			System.out.print("������� " + this.name + " ��������� " + expectedQuantityUnloading + " ������\n");
		} else {
			throw new NotEnoughGoodsException();
		}
	}

	private SeaPort choosePortOfArrival() throws Exception {
		SeaPort port;
		if (SeaPort.GetArrayPorts().size() > 1) {
			do {
				port = SeaPort.GetArrayPorts().get(getRandom(SeaPort.GetArrayPorts().size() - 1, 0));
			} while (port == portOfArrival);
			return port;
		} else
			throw new Exception("���������� ����� ��� 2 �����");
	}

	private void waitForTravelTime(int miliseconds) throws InterruptedException {
		System.out.print("������� " + this.name + " ������ � ����� " + portOfArrival.getNane() + "\n");
		Thread.sleep(miliseconds);
	}

	/**
	 * ����� �������� ����� ������������� ���-�� ������ ��� ������� ������� (1���� =1 �� ��� )
	 * ������������� ����� ���������� ��������� �������
	 * [0]-��������� ����� ��� ���������
	 * [1]-��������� ����� ��� ��������
	 */
	public int[] requestedQuantityOfGoods() {
		return new int[] { getRandom(this.getQuantityOfProductInCargo(), 0),
				getRandom(portOfArrival.getQuantityOfGoodsInStock(), 0) };
	}

	@SuppressWarnings("serial")
	public class NotEnoughGoodsException extends Exception {
		public NotEnoughGoodsException() {
			super("������������ ������ � �����");
		}
	}
}

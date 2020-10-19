package PortConcur;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ���� ������� ��������� ���������� �������� ������� ������� ������� ��
 * ��������� � ����������� � �����
 */

public class SeaPort {
	private int quantityOfGoodsInStock;
	/**
	 * �������� ���� ��������� ������
	 */
	private static ArrayList<SeaPort> portsList;
	private ArrayList<Berth> berthsList;
	private String name;
	private ConcurrentLinkedQueue<Ship> shipsQueue;
	private Object synStock;

	static {
		portsList = new ArrayList<SeaPort>();
	}

	public String getNane() {
		return name;
	}

	public SeaPort(String name) {

		this.name = name;
		quantityOfGoodsInStock = getRandom(1000, 1);
		Init();
		startBerths(getRandom(10, 1));
		SeaPort.portsList.add(this);
	}

	public SeaPort(int maxNumberOfBerths, int maxQuantityOfGoodsInStock, String name) {
		this.name = name;
		quantityOfGoodsInStock = getRandom(maxQuantityOfGoodsInStock, 1);
		Init();
		startBerths(getRandom(maxNumberOfBerths, 1));
		SeaPort.portsList.add(this);
	}

	public int getQuantityOfGoodsInStock() {
		return quantityOfGoodsInStock;
	}

	public void setToQuantityOfGoodsInStock(int quantity) throws NotEnoughGoodsException {
		if (quantity < 0) {
			throw new NotEnoughGoodsException();
		}
		quantityOfGoodsInStock = quantity;
	}

	private void Init() {

		shipsQueue = new ConcurrentLinkedQueue<Ship>();
		berthsList = new ArrayList<Berth>();
		synStock = new Object();
	}

	public static ArrayList<SeaPort> GetArrayPorts() {
		return portsList;
	}

	/**
	 * ��������������� ������� ������� �� �������
	 */
	public Ship getShipInQueue() throws InterruptedException {
		Ship ship;
		ship = shipsQueue.poll();
		while (ship == null) {
			Thread.sleep(100);
			ship = shipsQueue.poll();
		}
		return ship;
	}

	/**
	 * ������� �������� ���������� �������� � �������� ������ ������������� ���
	 * ������
	 */
	private void startBerths(int quantity) {
		for (int i = 0; i < quantity; i++) {
			berthsList.add(new Berth(this, String.valueOf(i), synStock));
			berthsList.get(i).start();
		}
	}

	public void addInQueue(Ship ship) {
		shipsQueue.add(ship);
	}

	public int getRandom(int max, int min) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	/**
	 * ���������� ������ ������� �������� (�������� + ���������� ������� �� �����)
	 * ��� �����������
	 */
	public ArrayList<Ship> getShipsInLineFull() {

		return new ArrayList<Ship>(shipsQueue);
	}

	/**
	 * ���������� ��������� �������� ����������� �� �������� � ������ ������
	 */
	public ArrayList<Ship> GetShipsInBerth() {
		ArrayList<Ship> shipsInBerth = new ArrayList<Ship>();
		for (int i = 0; i < berthsList.size(); i++) {
			if (berthsList.get(i).getShipInBerth() != null) {
				shipsInBerth.add(berthsList.get(i).getShipInBerth());
			}
		}
		return shipsInBerth;
	}

	@SuppressWarnings("serial")
	public class NotEnoughGoodsException extends Exception {
		public NotEnoughGoodsException() {
			super("������������ ������ �� ������ �����");
		}
	}

}

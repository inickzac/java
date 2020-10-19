package Port;

import java.util.ArrayList;

/**
 * Каждый корабль создается в новом потоке 
 * Случайно выбирает порт прибытия и количество товара 
 */


public class Ship extends Thread {

	public String name;
	private int quantityOfProduct;
	/**
	 * Колекция всех созданных кораблей
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
	 * Главный цикл корабля:
	 * Выбираем порт
	 * Ждем время необходимое для плавания
	 * Встаем на очередь в порт
	 * Ждем окончания работы порта 
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

		System.out.print("Корабль " + this.name + " Ожидает в очереди\n");
		while (!status.equals(Ship.statusShip.readyForNextTask)) {
			Thread.sleep(100);
		}

	}

	/**
	 * Порты вызывают этот метод для загрузки корабля
	 */
	public void Download(int expectedQuantityDownload) {
		quantityOfProduct += expectedQuantityDownload;
		System.out.print("Корабль " + this.name + " загрузил " + expectedQuantityDownload + " Товара\n");
	}

	/**
	 * Порты вызывают этот метод для разгрузки корабля
	 */
	public void Unload(int expectedQuantityUnloading) throws NotEnoughGoodsException {
		if (expectedQuantityUnloading <= quantityOfProduct) {
			quantityOfProduct -= expectedQuantityUnloading;
			System.out.print("Корабль " + this.name + " разгрузил " + expectedQuantityUnloading + " Товара\n");
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
			throw new Exception("Существует менее чем 2 порта");
	}

	private void waitForTravelTime(int miliseconds) throws InterruptedException {
		System.out.print("Корабль " + this.name + " Плывет к порту " + portOfArrival.getNane() + "\n");
		Thread.sleep(miliseconds);
	}

	/**
	 * Метод сообщает порту запрашиваемое кол-во товара для расчета стоянки (1мсек =1 ед тов )
	 * Запрашиваемый товар выбирается случайным образом
	 * [0]-Ожидаемый товар для разгрузки
	 * [1]-Ожидаемый товар для загрузки
	 */
	public int[] requestedQuantityOfGoods() {
		return new int[] { getRandom(this.getQuantityOfProductInCargo(), 0),
				getRandom(portOfArrival.getQuantityOfGoodsInStock(), 0) };
	}

	@SuppressWarnings("serial")
	public class NotEnoughGoodsException extends Exception {
		public NotEnoughGoodsException() {
			super("Недостаточно товара в трюме");
		}
	}
}

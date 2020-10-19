package Port;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * ѕорт создает случайное количество причалов
 * причалы достают корабли из коллекции в наход€щейс€ в порту
 * дл€ потокобезопасности перед обходом колекции она копируетс€
 */

public class SeaPort {
	private Object synBerthsInPorts;
	private int quantityOfGoodsInStock;
	/**
	 *  олекци€ всех созданных портов
	 */
	private static ArrayList<SeaPort> portsList;
	private ArrayList<Ship> copyQueueThePier;
	private ArrayList<Ship> queueAtThePier;
	private ArrayList<Berth> berthsList;
	private String name;
	private Object syn;
	private Object synStock;
	private Iterator<Ship> shipIter;
	private int shipsCompleateInLineInCopyQueue;

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

		berthsList = new ArrayList<Berth>();
		synBerthsInPorts = new Object();
		syn = new Object();
		queueAtThePier = new ArrayList<Ship>();
		copyQueueThePier = new ArrayList<Ship>();
		shipIter = copyQueueThePier.iterator();
		synStock = new Object();
	}

	public static ArrayList<SeaPort> GetArrayPorts() {
		return  portsList;
	}

	
	/**
	 * ѕровер€ет остались ли корабли в копии очреди,
	 * если не осталось, создает новую копию из очереди и обнул€ет очередь
	 */
	public Ship GetSafeShipInBypassQueueShips() throws InterruptedException {
		synchronized (synBerthsInPorts) {
			if (shipIter.hasNext()) {
				shipsCompleateInLineInCopyQueue++;
				return shipIter.next();
			} else {
				while (queueAtThePier.isEmpty()) {
					Thread.sleep(100);
				}
				return getShipAfterCopy();
			}
		}

	}
	/**
	 * ¬спомогательный метод дл€ GetSafeShipInBypassQueueShips()
	 * выполн€ет копирование ссылки,  
	 * инициализирует итератор
	 * и возвращает первый корабль из очереди.
	 */
	private Ship getShipAfterCopy() {
		synchronized (syn) {
			shipsCompleateInLineInCopyQueue = 0;
			copyQueueThePier = queueAtThePier;
			shipIter = copyQueueThePier.iterator();
			queueAtThePier = new ArrayList<Ship>();
			shipsCompleateInLineInCopyQueue++;
			return shipIter.next();
		}
	}

	/**
	 * —оздает заданное количество причалов и передает обьект синхронизации дл€ склада 
	 */
	private void startBerths(int quantity) {
		for (int i = 0; i < quantity; i++) {
			berthsList.add(new Berth(this, String.valueOf(i), synStock));
			berthsList.get(i).start();
		}
	}

	public void addInQueue(Ship ship) {
		synchronized (syn) {
			queueAtThePier.add(ship);
		}
	}

	public int getRandom(int max, int min) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	
	/**
	 * ¬озвращает полную очередь кораблей (ќсновную + оставшиес€ корабли из копии) дл€ логировани€
	 */
	public ArrayList<Ship> getShipsInLineFull() {
		ArrayList<Ship> ships = new ArrayList<Ship>();
		ships.addAll(queueAtThePier);
		if (shipsCompleateInLineInCopyQueue > copyQueueThePier.size()) {
			ships.addAll(shipsCompleateInLineInCopyQueue, copyQueueThePier);
		}
		return ships;
	}

	/**
	 * ¬озвращает коллекцию кораблей наход€щихс€ на причалах в данный момент 
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
			super("Ќедостаточно товара на складе порта");
		}
	}

}

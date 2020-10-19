package Port;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import Port.SeaPort.NotEnoughGoodsException;

/**
 * Каждый причал создается в новом потоке
 */
public class Berth extends Thread {
	private SeaPort port;
	private String name;
	private Object synObject;
	private int[] requestedQuantityOfGoods;
	private Ship ship;	
	private Timer time; 
	
	public Berth(SeaPort port, String name, Object syn)  {
		this.port = port;
		this.name = name;
		synObject = syn;
		PrintWriter writer;		
		time = new Timer();
		try {
			writer = new PrintWriter("WrongParkingTime");
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public String getNane() {
		return name;
	}

	/**
	 * Берем корабль из очереди, достаем нужное кол-во отовара из склада
	 * Запускаем контроль стоянки корабля(товары на погрузку+ на разгрузку + 500 мсек)  
	 * Разгружаем и загружаем корабль
	 * Сообщаем кораблю о готовности
	 */
	public void run() {
		try {
			while (true) {
				ship = port.GetSafeShipInBypassQueueShips();
				System.out
						.print("Корабль " + ship.name + " Зашел на причал " + name + " Порта " + port.getNane() + "\n");
				synchronized (synObject) {
					requestedQuantityOfGoods = ship.requestedQuantityOfGoods();
					port.setToQuantityOfGoodsInStock(port.getQuantityOfGoodsInStock() - requestedQuantityOfGoods[1]);
				}
				startTimeManager(requestedQuantityOfGoods[1]+requestedQuantityOfGoods[0]+500);
				unloadShip(ship);
				downShip(ship);
				ship.reportCompletionOfDownload();				
				System.out.print(
						"Корабль " + ship.name + " Вышел из причала " + name + " Порта " + port.getNane() + "\n");
				ship = null;
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void unloadShip(Ship ship)
			throws InterruptedException, NotEnoughGoodsException, Port.Ship.NotEnoughGoodsException {
		Thread.sleep(requestedQuantityOfGoods[0]);
		synchronized (synObject) {
			port.setToQuantityOfGoodsInStock(port.getQuantityOfGoodsInStock() + requestedQuantityOfGoods[0]);
			ship.Unload(requestedQuantityOfGoods[0]);
		}
	}

	private void downShip(Ship ship) throws InterruptedException {
		synchronized (synObject) {
			ship.Download(requestedQuantityOfGoods[1]);
		}
		Thread.sleep(requestedQuantityOfGoods[1]);
	}

	public Ship getShipInBerth() {
		return ship;
	}

	private void startTimeManager(long maximumParkingTime) {
		time.schedule(new TimerTask() {
			@Override
			public void run() {
				if(ship!=null) {
					 try {
						recordTheWrongParking(ship);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, maximumParkingTime);
	}

	private void recordTheWrongParking(Ship ship) throws IOException {
		FileWriter fw = new FileWriter("WrongParkingTime", true);
		fw.write(ship.name +" Превысил время нахождения на порту\n");
		fw.close();
	}

}

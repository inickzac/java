package PortConcur;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Logger extends Thread {

	/**
	 * Запускается в отдельном потоке
	 */
	public Logger() {
		try {
			FileWriter fw = new FileWriter("Log", false);
			fw.write("");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		final Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					FileWriter fw = new FileWriter("Log", true);
					fw.write(getLog());
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}, 5000, 5000);
	}

	/**
	 * Записывает количество товара на складе, количество и имена кораблей в очереди,
	 * количество и имена кораблей находящихся на причалах в данный момент 
	 */
	public String getLog() {
		String log = "";
		for (int i = 0; i < SeaPort.GetArrayPorts().size(); i++) {
			log += "Порт " + SeaPort.GetArrayPorts().get(i).getNane() + "\n";
			log += "Товара на складе " + SeaPort.GetArrayPorts().get(i).getQuantityOfGoodsInStock() + "\n";
			ArrayList<Ship> ships = SeaPort.GetArrayPorts().get(i).getShipsInLineFull();
			log += "В чередь на погрузку " + ships.size() + " кораблей:\n";
			for (int j = 0; j < ships.size(); j++) {
				log += "Корабль " + ships.get(j).getShipName() + "\n";
			}
			ArrayList<Ship> ShipsInBerth = SeaPort.GetArrayPorts().get(i).GetShipsInBerth();
			log += "Сейчас на  причалах " + ShipsInBerth.size() + " кораблей:\n";
			for (int j = 0; j < ShipsInBerth.size(); j++) {
				log += "Корабль " + ShipsInBerth.get(j).getName() + "\n";
			}
		}
		return log;
	}

}

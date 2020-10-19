package PortConcur;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Logger extends Thread {

	/**
	 * ����������� � ��������� ������
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
	 * ���������� ���������� ������ �� ������, ���������� � ����� �������� � �������,
	 * ���������� � ����� �������� ����������� �� �������� � ������ ������ 
	 */
	public String getLog() {
		String log = "";
		for (int i = 0; i < SeaPort.GetArrayPorts().size(); i++) {
			log += "���� " + SeaPort.GetArrayPorts().get(i).getNane() + "\n";
			log += "������ �� ������ " + SeaPort.GetArrayPorts().get(i).getQuantityOfGoodsInStock() + "\n";
			ArrayList<Ship> ships = SeaPort.GetArrayPorts().get(i).getShipsInLineFull();
			log += "� ������ �� �������� " + ships.size() + " ��������:\n";
			for (int j = 0; j < ships.size(); j++) {
				log += "������� " + ships.get(j).getShipName() + "\n";
			}
			ArrayList<Ship> ShipsInBerth = SeaPort.GetArrayPorts().get(i).GetShipsInBerth();
			log += "������ ��  �������� " + ShipsInBerth.size() + " ��������:\n";
			for (int j = 0; j < ShipsInBerth.size(); j++) {
				log += "������� " + ShipsInBerth.get(j).getName() + "\n";
			}
		}
		return log;
	}

}

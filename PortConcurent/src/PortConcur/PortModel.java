package PortConcur;

public class PortModel {

	public static void main(String[] args) {
		
		/**
		 * Создаем нужное количество портов
		 */
		for (int i = 0; i < 2; i++) {
			new SeaPort(String.valueOf(i));
		}		
		
		/**
		 * Создаем нужное количество кораблей
		 */
		for (int i = 0; i < 10; i++) {
			new Ship(String.valueOf(i)).start();
		}		
	
		/**
		 * Создаем поток для лога
		 */
		Logger loger = new Logger();
	loger.start();
		
	
	}
}

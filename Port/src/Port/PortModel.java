package Port;

public class PortModel {

	public static void main(String[] args) {
		
		/**
		 * ������� ������ ���������� ������
		 */
		for (int i = 0; i < 3; i++) {
			new SeaPort(String.valueOf(i));
		}		
		
		/**
		 * ������� ������ ���������� ��������
		 */
		for (int i = 0; i < 60; i++) {
			new Ship(String.valueOf(i)).start();
		}		
	
		/**
		 * ������� ����� ��� ����
		 */
		Logger loger = new Logger();
	loger.start();
		
	
	}
}

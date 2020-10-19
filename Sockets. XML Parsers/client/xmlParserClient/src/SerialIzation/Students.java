package SerialIzation;

import java.io.Serializable;
import java.util.ArrayList;

public class Students implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String UpdateDate;
	private ArrayList<Student> students;

	public String getUpdateDate() {
		return UpdateDate;
	}

	public void setUpdateDate(String updateDate) {
		UpdateDate = updateDate;
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}
	
}


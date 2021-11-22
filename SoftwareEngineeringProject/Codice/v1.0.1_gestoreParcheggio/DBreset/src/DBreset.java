
import java.sql.SQLException;

public class DBreset {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			BackupDB db = new BackupDB("jdbc:mysql://localhost:3300","root","admin");
			
			db.drop_setupDB();
			
			db.closeDB();
			
			System.out.println("Done.");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

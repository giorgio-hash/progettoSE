
import java.sql.SQLException;

import prog.io.FileInputManager;

public class DBreset {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String user = getUser();
		String pswd = getPswd();
		try {
			
			BackupDB db = new BackupDB("jdbc:mysql://"+getAddressName(),user,pswd);
			
			db.drop_setupDB();
			
			db.closeDB();
			
			System.out.println("Done.");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static String getAddressName() {
		
		FileInputManager file = new FileInputManager("dbCoords.txt");
		
		String str = file.readLine();
		
		return str;
	}
	
	private static String getUser() {
		
		FileInputManager file = new FileInputManager("dbCoords.txt");
		
		String str = file.readLine();
		while( str != null ){ 
					if( str.contains("user:") )
						return str.split(":")[1].trim();
			str = file.readLine();
		}
					
		return null;				
	}
	
	private static String getPswd() {
		
		FileInputManager file = new FileInputManager("dbCoords.txt");
		
		String str = file.readLine();
		while( str != null ) { 
					if( str.contains("password:") )
						return str.split(":")[1].trim();
			str = file.readLine();
		}
					
		return null;				
	}

}

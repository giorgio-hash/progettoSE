package launcher;
import java.io.IOException;

import java.sql.SQLException;

import concorrenziali.AutoEntrante;
import concorrenziali.AutoUscente;
import gestori.GestoreParcheggio;
import prog.io.FileInputManager;
import utilities.BackupDB;

public class Launcher
{
	public static void main(String args[]) {
		GestoreParcheggio server;
		BackupDB db;
		String user = getUser();
		String pswd = getPswd();
		try {
			db = new BackupDB("jdbc:mysql://"+getAddressName(),user,pswd);
			db.setupDB();
			db.describeTableDB();
			
			server = new GestoreParcheggio(db);
			
			Thread entranteThread = new Thread(new AutoEntrante(server));
			Thread uscenteThread = new Thread(new AutoUscente(server));
			
	      	uscenteThread.start();
	      	entranteThread.start();
	      	
	      	
	      	
	      	db.closeDB();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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


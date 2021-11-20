package testingTerminaleUscita;

import java.io.IOException;
import java.sql.SQLException;

public class Launcher
{
	public static void main(String args[]) {
		Parcheggio server;
		BackupDB db;
		try {
			db = new BackupDB("jdbc:mysql://localhost:3300","root","admin");
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
}


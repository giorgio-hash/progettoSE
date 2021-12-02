
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BackupDB {

	private Connection conn;
	private Statement s;
	private static ResultSet rs;
	
	private String url;
	private String user;
	private String pswd;
	
	public BackupDB(String url, String user, String pswd) throws SQLException {
		// TODO Auto-generated constructor stub
		
		this.url = url;
		this.user = user;
		this.pswd = pswd;
		
	}
	
	
	private void setDB() throws SQLException {
		s.execute("USE databasename;");
	}
	
	private void getConnection() throws SQLException {
		
		conn = DriverManager.getConnection(url+"/?user="+user+"&password="+pswd);
		s= conn.createStatement();
		setDB();
		
	}
	
	public void closeDB() throws SQLException {
		conn.close();
	}
	
	
	
	
	public void insert(String barcode,int a, int m, int g, int h, int min) throws SQLException {
		
		getConnection();
		s.execute("INSERT INTO ticket VALUES ("+barcode+","+a+","+m+","+g+","+h+","+min+");");
	
	}
	
	public void extract(String barcode) throws SQLException {
		
		getConnection();
		s.execute("DELETE FROM ticket WHERE barcode="+barcode+";");
	}
	
	
	
	public ArrayList<Ticket> getBackup() throws SQLException {
		
		getConnection();
		
		ArrayList<Ticket> parcheggio = new ArrayList<Ticket>();
		
		rs = s.executeQuery("SELECT * FROM ticket;");
		
		while(rs.next()) {
			parcheggio.add(new Ticket(rs.getString(1),LocalDateTime.of(rs.getInt(2), rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6))));
		}
		
		return parcheggio;
		
	}
	
	
	
	
	public void setupDB() throws SQLException {
		
		conn = DriverManager.getConnection(url+"/?user="+user+"&password="+pswd);
		s= conn.createStatement();
		
		s.execute("CREATE DATABASE IF NOT EXISTS databasename;");
		setDB();
		s.execute("CREATE TABLE IF NOT EXISTS ticket("
				+ "barcode varchar(20) PRIMARY KEY," 
				+ "anno int NOT NULL," 
				+"mese int NOT NULL," 
				+"giorno int NOT NULL,"
				+"ora int NOT NULL,"
				+"minuti int NOT NULL);");
		
	}
	
	
	public void drop_setupDB() throws SQLException {
		getConnection();
		s.execute("DROP DATABASE databasename;");
	}
	
	
	public void describeTableDB() throws SQLException {
		
		getConnection();
		rs = s.executeQuery("DESC ticket");
		
		System.out.println("Struttura tabella di backup: ");
		while(rs.next())
			System.out.print(rs.getString(1) + "\t");
		
	}
	
	
	public static void display() throws SQLException {
		
		System.out.println("barcode \t LocalDateTime");
		while(rs.next()) {
			System.out.println(rs.getString(1) + " \t\t " + LocalDateTime.of(rs.getInt(1), rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5)).toString());
		}
	}
}

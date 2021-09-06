package cw2;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.*;  
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Winners {


	Winners(){
		DBConnect conn = new DBConnect();
		
		JFrame frame = new JFrame();
		
		JLabel title;
		JTable table;
		
		title = new JLabel("Winners");
		title.setFont(new Font("Times New Roman", Font.BOLD, 25));
		title.setBounds(300, 20, 150, 30);
		
		Object[][] data;


		String query = "SELECT tickets.id, tickets.name AS ticketNumber, customers.name, customers.phone, customers.email FROM tickets INNER JOIN Customers ON tickets.user_id=customers.id;";
		ArrayList<CutomerTicket> sft = new ArrayList<CutomerTicket>();
		List<CutomerTicket> finalListForDb = new ArrayList<CutomerTicket>();
		try {
			ResultSet rs = conn.connection().createStatement().executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("id");
				String ticketnumber = rs.getString("ticketNumber");
				String customerName = rs.getString("name");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				
				CutomerTicket st = new CutomerTicket(id,ticketnumber ,  customerName,email,phone);
				sft.add(st);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (sft.size() < 3){
			finalListForDb = sft;
		}else{
			Collections.shuffle(sft);
			finalListForDb = sft.subList(0, 3);
		}

		for (CutomerTicket listItem : finalListForDb) {
			System.out.println(listItem.id);
			addWinner(listItem.id);
		}
		
		String column[] = {"ID", "Ticket Number", "Customer Name"};		
		data = new Object[finalListForDb.size()][column.length]; 

		for(int i=0; i<finalListForDb.size(); i++) {
			data[i][0] = finalListForDb.get(i).id;
			data[i][1] = finalListForDb.get(i).ticketnumber;
			data[i][2] = finalListForDb.get(i).name;
		}
			
		table = new JTable(data, column);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(100, 60, 600, 400);
		
		frame.add(title);
		frame.add(sp);
		
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		
	}

	public boolean addWinner(int id) {
		DBConnect conn = new DBConnect();
		PreparedStatement stmt;
		try {
			stmt = (PreparedStatement) conn.connection().prepareStatement("insert into winners values (null, ?)");
			stmt.setInt(1, id);
			stmt.executeUpdate();
			return true;	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}			
		return false;
	}
	
}

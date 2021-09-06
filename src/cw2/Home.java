package cw2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.*;  
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;


public class Home {

	Home(Object[][] filteredData, boolean ischanged){
		
		DBConnect conn = new DBConnect();
		Object[][] data;
		JFrame frame = new JFrame("Home");
		
		JLabel title,searchlbl, sortlbl;
		JTextField searchInput;
		JButton searchBtn, sortBtn, winnerBtn, logoutBtn, loadBtn;
		JTable table;
		JComboBox search_dropdown, sort_dropdown;
				
		title = new JLabel("Lottery System", JLabel.CENTER);
		title.setFont(new Font("Times New Roman", Font.BOLD, 30));
		title.setBounds(0, 0, 1200, 40);
		
		logoutBtn = new JButton("Logout");
		logoutBtn.setBounds(900, 50, 100, 30);
		logoutBtn.addActionListener(e -> {
			frame.dispose();
			new Login();
		});
		
		winnerBtn = new JButton("Winner Ticket Draw");
		winnerBtn.setBounds(700, 650, 200, 30);

		loadBtn = new JButton("Load CSV");
		loadBtn.setBounds(500, 650, 200, 30);
		
		sortlbl = new JLabel("Sort by");
        sortlbl.setBounds(100, 80, 100, 30);
        
        String sort_type[] = {"Ascending", "Decending"};
        sort_dropdown = new JComboBox(sort_type);
        sort_dropdown.setBounds(200, 80, 150, 30);
		
		sortBtn = new JButton("Sort");
		sortBtn.setBounds(370, 80, 100, 30);
		
		searchlbl = new JLabel("Search by");
        searchlbl.setBounds(740, 80, 100, 30);
        
        String search_type[] = {"Ticket Number", "Customer Name"};
        search_dropdown = new JComboBox(search_type);
        search_dropdown.setBounds(840, 80, 150, 30);
		
		searchInput = new JTextField();
		searchInput.setBounds(1000, 80, 170, 30);
		
		searchBtn = new JButton("Search");
		searchBtn.setBounds(1170, 80, 100, 30);
		String column[] = {"ID", "Ticket Number", "Customer Name", "Email", "Phone"};		

		
		if(ischanged==true) {
			data = filteredData;
		} else {
		// table
		String query = "SELECT tickets.id, tickets.name AS ticketNumber, customers.name, customers.phone, customers.email FROM tickets INNER JOIN Customers ON tickets.user_id=customers.id";
		ArrayList<CutomerTicket> sft = new ArrayList<CutomerTicket>();
		try {
			ResultSet rs = conn.connection().createStatement().executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("id");
				String ticketnumber = rs.getString("ticketNumber");
				String customerName = rs.getString("name");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				CutomerTicket st = new CutomerTicket(id, ticketnumber,  customerName,email,phone);
				sft.add(st);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		data = new Object[sft.size()][column.length]; 

		for(int i=0; i<sft.size(); i++) {
			data[i][0] = sft.get(i).id;
			data[i][1] = sft.get(i).ticketnumber;
			data[i][2] = sft.get(i).name;
			data[i][3] = sft.get(i).email;
			data[i][4] = sft.get(i).phone;
		}
		}
		
		table = new JTable(data, column);
		JScrollPane sp = new JScrollPane(table);
		sp.setBounds(320, 180, 800, 450);
		
		winnerBtn.addActionListener(e -> {
			new Winners();
			
		});

		loadBtn.addActionListener(e -> {
			
			if (loadCSV()){
				JOptionPane.showMessageDialog(frame, "CSV File Loaded Successfully.");
				frame.dispose();
				new Home(null, false);
			}else{
				JOptionPane.showMessageDialog(frame, "CSV File Load Failed!");
			}


		});

		sortBtn.addActionListener(e -> {
			
			String type = search_dropdown.getSelectedItem().toString();
			String order = sort_dropdown.getSelectedItem().toString();
			int index = 1;
			MergeSort ms = new MergeSort();
			int num = data.length;
			ms.sort(data, 0, num-1, index, order);
			frame.dispose();
			new Home(data, true);
			
		});
		
		searchBtn.addActionListener(e -> {
			
			String type = search_dropdown.getSelectedItem().toString();
			int index = 1;
			
			if(type.equals("Ticket Number")) {
				index = 1;
			}
			else if(type.equals("Customer Name")) {
				index = 2;
			}
			
			String search_text = searchInput.getText();
						
			if(search_text.length() == 0) {
				JOptionPane.showMessageDialog(frame, "Enter something to search..");;
			} else {
			String query = "SELECT tickets.id, tickets.name AS ticketNumber, customers.name, customers.phone, customers.email FROM tickets INNER JOIN Customers ON tickets.user_id=customers.id;";
			ArrayList<CutomerTicket> sft = new ArrayList<CutomerTicket>();
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
			
			Object[][] result_data = new Object[sft.size()][column.length]; 

			for(int i=0; i<result_data.length; i++) {
				result_data[i][0] = sft.get(i).id;
				result_data[i][1] = sft.get(i).ticketnumber;
				result_data[i][2] = sft.get(i).name;
				result_data[i][3] = sft.get(i).email;
				result_data[i][4] = sft.get(i).phone;
			}
			
			ArrayList<Object[]> result = new ArrayList<Object[]>();
			Search search = new Search();
			search.searchString(result_data, result, index, search_text);
			
			Object[][] result_list = new Object[result.size()][column.length];
            for(int i=0; i<result.size(); i++) {
            	result_list[i] = result.get(i);
            }
            if (result_list.length<=0) {
                JOptionPane.showMessageDialog(sp, "No tickets found of consisting data "+search_text);
            } else {
            	frame.dispose();
                new Home(result_list, true); 
            }
            
			}
			
		});
		
		frame.add(sp);
		frame.add(title);
		frame.add(searchInput);
		frame.add(searchBtn);
		frame.add(searchlbl);
		frame.add(search_dropdown);
		frame.add(sort_dropdown);
		frame.add(sortBtn);
		frame.add(sortlbl);
		frame.add(winnerBtn);
		frame.add(loadBtn);
		frame.add(logoutBtn);
		frame.setSize(1400, 800);
		frame.setLayout(null);
		frame.setVisible(true);
		
	}

	public boolean loadCSV() {
		System.out.println(Paths.get("sample.csv").toAbsolutePath().toString());
		try (BufferedReader br = Files.newBufferedReader(Paths.get("sample.csv").toAbsolutePath())) {

			// CSV file delimiter
			String DELIMITER = ",";
		
			// read the file line by line
			String line;
			ArrayList<String[]> multiList = new ArrayList<String[]>();
			while ((line = br.readLine()) != null) {
		
				// convert line into columns
				String[] columns = line.split(DELIMITER);
				multiList.add(columns);
				// print all columns
				System.out.println(columns);
				
			}
			multiList.remove(0);
			System.out.println(multiList);

			for (String[] list : multiList) {
				addTicket(list);
			}
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean addTicket(String[] list) {
		DBConnect conn = new DBConnect();
		PreparedStatement stmt;
		try {
			stmt = (PreparedStatement) conn.connection().prepareStatement("insert into customers values (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, list[1]);
			stmt.setString(2, list[3]);
			stmt.setString(3, list[2]);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
				int customerId = rs.getInt(1);
				System.out.println(customerId);
				stmt = (PreparedStatement) conn.connection().prepareStatement("insert into tickets values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, list[0]);
				stmt.setInt(2, customerId);
				stmt.executeUpdate();
			}
			return true;	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}			
		return false;
	}


	}

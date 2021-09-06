package cw2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register {

		Register(){
				JFrame frame = new JFrame();
				JLabel id, username, password;
				JTextField ident, uname;
				JPasswordField passwd;
				JButton regBtn;
				
				// user id 
				id = new JLabel("Your ID");
				id.setBounds(200, 200, 200, 30);
				
				ident = new JTextField();
				ident.setBounds(320, 200, 250, 30);
				
				// user name 
				username = new JLabel("Username");
				username.setBounds(200, 250, 200, 30);

				uname = new JTextField();
				uname.setBounds(320, 250, 250, 30);
				
				// password
				password = new JLabel("Password");
				password.setBounds(200, 300, 200, 30);
				
				passwd = new JPasswordField();
				passwd.setBounds(320, 300, 250, 30);
				
				// login button
				regBtn = new JButton("Register");
				regBtn.setBounds(320, 350, 250, 30);	
				regBtn.addActionListener(e -> {
					
					int user_id;
					String name, pswd;
					
					user_id = Integer.parseInt(ident.getText());
					name = uname.getText();
					pswd = passwd.getText();
					
					if(name.length() != 0 && pswd.length()!= 0) {
					
						boolean result = registerUser(user_id, name, pswd);
						
						if(result == true) {
							frame.dispose();
							new Login();
							JOptionPane.showMessageDialog(frame, "User registered successfully");
						}
						else {
							JOptionPane.showMessageDialog(frame, "Couldnot register user");
						}
					}
					else {
						JOptionPane.showMessageDialog(frame, "All fields are required");
					}
					
				});
				
				// adding to frame
				frame.add(id);
				frame.add(ident);
				frame.add(username);
				frame.add(password);
				frame.add(uname);
				frame.add(passwd);
				frame.add(regBtn);		
				
				// setting frame
				frame.setSize(800, 600);
				frame.setLayout(null);
				frame.setVisible(true);
		}
		
		public boolean registerUser(int user_id, String name, String password) {
			
			DBConnect conn = new DBConnect();
			PreparedStatement stmt;
			
			try {
				stmt = (PreparedStatement) conn.connection().prepareStatement("insert into admin_user values (?, ?, ?)");
				stmt.setInt(1, user_id);
				stmt.setString(2, name);
				stmt.setString(3, password);
				stmt.executeUpdate();
				
				return true;
			
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
			
			return false;
		}
		
}

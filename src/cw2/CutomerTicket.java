package cw2;

import java.sql.Date;

public class CutomerTicket {
	
	int id;
	// price, quantity;
	String ticketnumber;
	String name;
	String email, phone;
	// publisher;
	// String publish_date;
	
	CutomerTicket(int id, String  ticketnumber, String name, String email, String phone){
		this.id = id;
		this.name = name;
		this.ticketnumber = ticketnumber;
		this.email = email;
		this.phone= phone;
		// this.publisher = publisher;
		// this.publish_date = publish_date;
		// this.price = price;
		// this.quantity = quantity;
	}
	
}

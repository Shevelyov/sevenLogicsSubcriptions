package org.dbProject.restfulcrud.dao;

import org.dbProject.restfulcrud.model.CalendarEvent;
import org.dbProject.restfulcrud.model.LoginInfo;
import org.dbProject.restfulcrud.model.Subscription;
import org.dbProject.restfulcrud.model.ToDoList;
import org.dbProject.restfulcrud.model.User;

import java.io.FileInputStream;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class FamilyOrganizerDAO {
	
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private final String DB_URL = "jdbc:mysql://localhost:3306/family_organizer?autoReconnect=true&useSSL=false";
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String USER = "root";
	private final String PASS = "root";
	
	//get Subscription Type 
	public List<Subscription> getSubscriptionData()
	{

		List<Subscription> list = new ArrayList<Subscription>();
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Fetching records from the table...");
			stmt = con.createStatement();
			String sql = "Select Subscription_ID,Subscription_Type,Subscription_Amount from Subscription;";

			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){

				Subscription sub=new Subscription();
				sub.setSubscriptionID(Integer.toString(rs.getInt("Subscription_ID")));
				sub.setSubscriptionType(rs.getString("Subscription_Type"));
				sub.setAmount(rs.getString("Subscription_Amount"));

				list.add(sub);
			}
			rs.close();


		}catch(Exception e){
			System.out.println(e.getMessage());

		}finally{

			try{
				if(stmt!=null)
				{
					con.close();

				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
			try{
				if(con!=null)
				{
					con.close();

				}

			}catch(SQLException se){
				System.out.println(se.getMessage());

			}

		}
		return list;

	}

	public List<CalendarEvent> getAllEvents(User u)
	{
		List<CalendarEvent> list = new ArrayList<CalendarEvent>();
		try{
			/*Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);*/

			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Fetching records from the table...");
			stmt = con.createStatement();
			String sql = "Select Event_ID,Event_Title,Event_Desc,Notify_ByEmail,Event_User_Selected_Date,User_ID from Calendar_Events where User_ID="+u.getUserID()+" and Date(Event_User_Selected_Date)='"+ u.getCustName() +"';";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				//Retrieve by column name
				CalendarEvent cal=new CalendarEvent();
				cal.setEventID(Integer.toString(rs.getInt("Event_ID")));
				cal.setTitle(rs.getString("Event_Title"));
				cal.setDesc(rs.getString("Event_Desc"));
				cal.setNotifyByEmail(rs.getBoolean("Notify_ByEmail"));
				cal.setEventUserSelectedDate(rs.getString("Event_User_Selected_Date"));
				cal.setUserID(Integer.toString(rs.getInt("User_ID")));
				list.add(cal);
			}
			rs.close();


		}catch(Exception e){
			System.out.println(e.getMessage());
			
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
				{
					con.close();
					
				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
			try{
				if(con!=null)
				{
					con.close();
					
				}

			}catch(SQLException se){
				System.out.println(se.getMessage());
				
			}

		}
		return list;
	}

	//add user
	public List<User> addUser(User u)
	{
		List<User> list = new ArrayList<User>();
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Inserting records into the table...");
			stmt = con.createStatement();
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);
			// hash the pwd
			String hashedPwd= hashPwd(u.getPwd(), "", "hashPwd");
			//insert record
			PreparedStatement ps=con.prepareStatement("insert into Users(username,pwd,customer_name,family_name,emailID,subscription_ID_FK,"
					+ "Subscription_StartDate,loginStatus) values(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			ps.setString(1,u.getUserName());
			ps.setString(2,hashedPwd);
			ps.setString(3,u.getCustName());
			ps.setString(4,u.getFamilyName());
			ps.setString(5,u.getEmailID());
			ps.setString(6,u.getSubscriptionType());
			ps.setString(7,currentDate);
			ps.setBoolean(8, false);
			
			ps.executeUpdate();
			ResultSet rs=ps.getGeneratedKeys();
			int id=0;
			if(rs.next()){
				id=rs.getInt(1);
			}
			rs.close();
			String username=u.getUserName();
			String subscriptionID=u.getSubscriptionType();
			//String pwd=u.getPwd(); 
			User newObj=new User();
			newObj.setUserID(id);
			newObj.setUserName(username);
			newObj.setSubscriptionType(subscriptionID);
			//newObj.setPwd(pwd);
			System.out.println("Inserted records into the table...");
			System.out.println(id);
			list.add(newObj);
		
		}catch(Exception e){
			
			System.out.println(e.getMessage());
			
		}finally{
			
			try{
				if(stmt!=null)
					con.close();
				
			}catch(SQLException se){
				System.out.println(se.getMessage());
			
			}
			try{
				if(con!=null)
					con.close();
				

			}catch(SQLException se){
				System.out.println(se.getMessage());
				
			}
		}
		return list;

	}
	//update subscription plan
	public String updateSubscriptionPlan(User u)
	{
		String status="";
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = con.createStatement();
			
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);
			
			String sql = "UPDATE Users set Subscription_ID_FK="+ u.getSubscriptionType() + ",Subscription_StartDate='"+currentDate  + "' where user_ID="+ u.getUserID()+";";
			stmt.executeUpdate(sql);
			System.out.println("record updated into the table...");
			status="success";
		}catch(Exception e){
			System.out.println(e.getMessage());
			status="error";
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
		}
		return status;
	}
	
	//add events
	public String addEvent(CalendarEvent eventObj)
	{		
		String status="";
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Inserting records into the table...");
			stmt = con.createStatement();
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);
			System.out.println(eventObj.getEventUserSelectedDate());
			String sql = "INSERT INTO Calendar_Events(Event_Title,Event_Desc,Notify_ByEmail,Event_User_Selected_Date,Event_Date,User_ID)" +
					"VALUES ('" + eventObj.getTitle() +"','"+ eventObj.getDesc()+"',"+ eventObj.isNotifyByEmail()+",'"+ eventObj.getEventUserSelectedDate()
					+"','"+currentDate + "',"+ Integer.parseInt(eventObj.getUserID()) + ");";
			stmt.executeUpdate(sql);
			System.out.println("Inserted records into the table...");
			
			status="success";
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			status="error";
			
		}finally{
			
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
		}
		return status;
	
	}
	//update events
	
	public String updateEvent(CalendarEvent eventObj)
	{
		String status="";
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Inserting records into the table...");
			stmt = con.createStatement();
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);
			String sql = "UPDATE Calendar_Events set Event_Title='"+ eventObj.getTitle() +"', "
					+ "Event_Desc='" + eventObj.getDesc() +"',"
					+ "Notify_ByEmail=" + eventObj.isNotifyByEmail() +"," 
					+ "Event_User_Selected_Date='" + eventObj.getEventUserSelectedDate() +"'," 
					+ "Event_Date='" + currentDate  
					+"' where Event_ID="+eventObj.getEventID()+";";
			stmt.executeUpdate(sql);
			System.out.println("record updated into the table...");
			
			status="success";
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			status="error";
		}finally{
		
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
		}

		return status;
	}
	//delete events
	
	public String deleteEvent(CalendarEvent eventObj)
	{
		String status="";
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("deleting records into the table...");
			stmt = con.createStatement();
			
			String sql = "delete from Calendar_Events where Event_ID=" + eventObj.getEventID() + ";";
			stmt.executeUpdate(sql);
			System.out.println("record deleted into the table...");
			status="success";
		}catch(Exception e){
			System.out.println(e.getMessage());
			status="error";
			
		}finally{
			
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
		}
		
		return status;
	}


	/*//insert Login Info and status
	public String insertLoginInfo(LoginInfo info)
	{
		boolean isError=false;
		// JDBC driver name and database URL
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		String DB_URL = "jdbc:mysql://localhost:3306/familyOrganizerdb";

		//  Database credentials
		String USER = "root";
		String PASS = "admin";


		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			//STEP 4: Execute a query
			System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);
			String sql = "INSERT INTO LoginInfo(User_ID,loginStatus,deviceType,deviceIPAddress,loginDate)" +
					"VALUES ('" + info.getUserID() +"',"+ info.isLoginStatus()+",'"+ info.getDeviceType()+"','"+ 
					info.getDeviceIPAddress()+"','"+currentDate + "');";
			stmt.executeUpdate(sql);
			System.out.println("Inserted records into the table...");

		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			isError=true;
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					conn.close();
			}catch(SQLException se){
				isError=true;
			}
			try{
				if(conn!=null)
					conn.close();

			}catch(SQLException se){
				se.printStackTrace();
				isError=true;
			}//end finally try
		}//end try
		if (isError==false)
		{
			return "success";
		}
		else
		{
			return "failure";
		}
	}*/
	//check valid login
	/*public String checkValidUserLogin(User u)
	{
		String status="";
		try{
			con=getConnection();
			//get original pwd from DB for that user
			stmt = con.createStatement();
			String sql = "Select pwd from Users where username='" + u.getUserName() +"';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String originalPwd = rs.getString(1);
			rs.close();
			//hash pwd
			String hashedPwd= hashPwd(originalPwd,u.getPwd(), "matchPwd");
			System.out.println(hashedPwd);
			if (hashedPwd.toUpperCase().equals("INVALID"))
			{
				status="invalid";
			}
			else
			{
				status="valid";
				//System.out.println("Inserting records into the table...");
				stmt = con.createStatement();
				sql = "Select COUNT(*) from Users where username='" + u.getUserName() +"' and pwd='"+ originalPwd +"';";
				rs = stmt.executeQuery(sql);
				rs.next();
				int rowCount = rs.getInt(1);
				if (rowCount>0)
				{
					status="valid";
					
				}
				else
				{
					status="invalid";
					
				}
				rs.close();
				System.out.println("record updated into the table...");
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			status="error";
			
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				status="error";
				
			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
				
			}
		}
		
		return status;
	}*/
	
	//get all to-do list items for a User
	public List<ToDoList> getAllToDoListItems(User u)
	{
		List<ToDoList> list = new ArrayList<ToDoList>();
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Fetching records from the table...");
			stmt = con.createStatement();
			String sql = "Select ID,Title from toDoList where User_ID="+u.getUserID()+";";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				//Retrieve by column name
				ToDoList obj=new ToDoList();
				obj.setID(rs.getInt("ID"));
				obj.setTitle(rs.getString("Title"));
				list.add(obj);
			}
			rs.close();

		}catch(Exception e){
			System.out.println(e.getMessage());
			
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
				{
					con.close();
					
				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
			try{
				if(con!=null)
				{
					con.close();
				}

			}catch(SQLException se){
				System.out.println(se.getMessage());
			}

		}
		return list;
	}
	
	//add to-do List items
		public String addItemsForToDoList(ToDoList obj)
		{		
			String status="";
			try{
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Inserting records into the table...");
				stmt = con.createStatement();
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDate= sdf.format(dt);
				
				String sql = "INSERT INTO toDoList(Title,SystemDate,User_ID)" +
						"VALUES ('" + obj.getTitle() +"','"+currentDate + "',"+ obj.getUserID() + ");";
				stmt.executeUpdate(sql);
				System.out.println("Inserted records into the table successfully...");
				
				status="success";
				
			}catch(Exception e){
				System.out.println(e.getMessage());
				status="error";
				
			}finally{
				
				try{
					if(stmt!=null)
						con.close();
				}catch(SQLException se){
					System.out.println(se.getMessage());
					status="error";
				}
				try{
					if(con!=null)
						con.close();

				}catch(SQLException se){
					System.out.println(se.getMessage());
					status="error";
				}
			}
			return status;
		
		}
		//update to-do list items
		
		public String updateItemsForToDoList(ToDoList obj)
		{
			String status="";
			try{
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("Updating records into the table...");
				stmt = con.createStatement();
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDate= sdf.format(dt);
				String sql = "UPDATE toDoList set Title='"+ obj.getTitle() +"', "
						+ "SystemDate='" + currentDate   +"' where ID="+ obj.getID() +";";
				stmt.executeUpdate(sql);
				System.out.println("record updated into the table...");
				
				status="success";
				
			}catch(Exception e){
				System.out.println(e.getMessage());
				status="error";
			}finally{
			
				try{
					if(stmt!=null)
						con.close();
				}catch(SQLException se){
					System.out.println(se.getMessage());
					status="error";
				}
				try{
					if(con!=null)
						con.close();

				}catch(SQLException se){
					System.out.println(se.getMessage());
					status="error";
				}
			}

			return status;
		}
		//delete to-do list items
		
		public String deleteItemsForToDoList(ToDoList obj)
		{
			String status="";
			try{
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL, USER, PASS);
				System.out.println("deleting records into the table...");
				stmt = con.createStatement();
				
				String sql = "delete from toDoList where ID=" + obj.getID() + ";";
				stmt.executeUpdate(sql);
				System.out.println("record deleted into the table...");
				status="success";
			}catch(Exception e){
				System.out.println(e.getMessage());
				status="error";
				
			}finally{
				
				try{
					if(stmt!=null)
						con.close();
				}catch(SQLException se){
					System.out.println(se.getMessage());
					status="error";
				}
				try{
					if(con!=null)
						con.close();

				}catch(SQLException se){
					System.out.println(se.getMessage());
					status="error";
				}
			}
			
			return status;
		}
	
	
	public String checkDuplicateUsername(User u)
	{
		String status="";
		try{

			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Inserting records into the table...");
			stmt = con.createStatement();
			String sql = "Select COUNT(*) from Users where username='" + u.getUserName() +"';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int rowCount = rs.getInt(1);
			if (rowCount>=1)
			{
				status="duplicate";
				
			}
			else
			{
				status="success";
			
			}
			rs.close();
			System.out.println("record updated into the table...");

		}catch(Exception e){
			System.out.println(e.getMessage());
			status="error";
		
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			
			}
			try{
				if(con!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
				
			}
		}
		
		return status;
	}
	//update user pwd
	public String updateUserPwd(User u)
	{
		boolean isError=false;
		String status="";


		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");
			
			//get username and pwd
			stmt = conn.createStatement();
			String sql = "Select Username,pwd from Users where User_ID=" + u.getUserID() +";";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String username = rs.getString(1);
			String oldPwd = rs.getString(2);
			rs.close();
			//====================================================
			//match user entered correct old pwd
			String hashedPwd= hashPwd(oldPwd,u.getPwd(), "matchPwd");
			System.out.println(hashedPwd);
			if (hashedPwd.toUpperCase().equals("INVALID"))
			{
				status="invalid";
			}
			else
			{
				//====================================================
		
				//hash the new pwd and update the record
				 hashedPwd= hashPwd(u.getCustName(), "", "hashPwd");
				 System.out.println(hashedPwd);
					//STEP 4: Execute a query
					System.out.println("Inserting records into the table...");
					stmt = conn.createStatement();
					//here custName acts as new pwd
					sql = "UPDATE Users set pwd='"+ hashedPwd +"' where User_ID="+ u.getUserID()+";";
					stmt.executeUpdate(sql);
					System.out.println("record updated into the table...");
					status="valid";
			
			}
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			status=e.getMessage();
			isError=true;
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					conn.close();
			}catch(SQLException se){
				isError=true;
				status=se.getMessage();
			}
			try{
				if(conn!=null)
					conn.close();

			}catch(SQLException se){
				se.printStackTrace();
				status=se.getMessage();
				isError=true;
			}//end finally try
		}//end try
		/*if (isError==false)
		{
			return "success";
		}
		else
		{
			return "failure";
		}*/
		return status;
	}
  
	public String login_logout(LoginInfo info)
	{
		String status="";
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			//check if valid user n pwd 
			stmt = conn.createStatement();
			String sql = "update Users set loginStatus=" + info.isLoginStatus() + " where User_ID="+ info.getUserID() +" ;";
			int i=stmt.executeUpdate(sql);
			System.out.println("record updated into the table...");
			System.out.println("execute update:" + i);
			if (i >0)
			{
				//insert records in log
				stmt = conn.createStatement();
				Date dt = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentDate= sdf.format(dt);
				System.out.println(info.getOperation());
				System.out.println(info.getOperation().toUpperCase().trim());
				if (info.getOperation().toUpperCase().trim().equals("LOGIN"))
				{
				sql = "INSERT INTO loginInfo(User_ID,loginStatus,deviceType,deviceIPAddress,loginDate)" +
						"VALUES (" + info.getUserID() +","+ info.isLoginStatus()+",'"+ info.getDeviceType() +"','"+ info.getDeviceIPAddress() +"','"+currentDate + "');";
				int j=stmt.executeUpdate(sql);
				System.out.println(j);
				System.out.println("Inserted records into the login info table...");
				status="success";
				}
				else if (info.getOperation().toUpperCase().trim().equals("LOGOUT"))
				{
					sql = "INSERT INTO loginInfo(User_ID,loginStatus,deviceType,deviceIPAddress,logoutDate)" +
							"VALUES (" + info.getUserID() +","+ info.isLoginStatus()+",'"+ info.getDeviceType() +"','"+ info.getDeviceIPAddress()
							+"','"+currentDate + "');";
					stmt.executeUpdate(sql);
					System.out.println("Inserted records into the table...");
					status="success";
				}
				status="success";
			}
			else
				status="failure";
			
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			status=e.getMessage();

		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					conn.close();
				status="success";
			}catch(SQLException se){

				status=se.getMessage();
			}
			try{
				if(conn!=null)
					conn.close();
					status="success";
			}catch(SQLException se){
				se.printStackTrace();
				status=se.getMessage();
			}//end finally try
		}//end try
		return status;
	}

	public List<Subscription> checkSubscriptionPlan(User u)
	{
		List<Subscription> list = new ArrayList<Subscription>();
		try{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Fetching records from the table...");
			stmt = con.createStatement();
			String sql = "select u.subscription_id_fk,s.Subscription_Type from Users u, Subscription s where u.Subscription_ID_FK=s.Subscription_ID and u.user_ID=" + u.getUserID() + ";";

			ResultSet rs = stmt.executeQuery(sql);
			rs.next();

			//Retrieve by column name
			Subscription sub=new Subscription();
			sub.setSubscriptionID(Integer.toString(rs.getInt("subscription_id_fk")));
			sub.setSubscriptionType(rs.getString("Subscription_Type"));
			list.add(sub);

			rs.close();


		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{

			try{
				if(stmt!=null)
				{
					con.close();

				}
			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
			try{
				if(con!=null)
				{
					con.close();
					
				}

			}catch(SQLException se){
				System.out.println(se.getMessage());
			}

		}
		return list;

	}
	
	public String Logout(User u,String IPAddress)
	{
		String status="";
		try
		{
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			//get pwd from DB for that user
			System.out.println(u.getUserID());
			
			//update Login Info table
			//insert into login Info
			System.out.println("updating Login Info table..");
			stmt = con.createStatement();
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate= sdf.format(dt);
			String sql = "Update LoginInfo set logoutDate='" +currentDate+"', loginStatus=false where User_ID="+ u.getUserID()+" and deviceIPAddress='" + IPAddress + "';";
			stmt.executeUpdate(sql);

			System.out.println("updated Login Info table successfully");
			
			int size=0;
			//update main users table only if user is logged out of all the devices.
			stmt = con.createStatement();
			sql ="select count(*),loginStatus from logininfo where user_id=" + u.getUserID()+" and loginStatus=true group by(loginStatus);";	
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) 
			{
				rs.last();
				size = rs.getRow();
				rs.beforeFirst();
				System.out.println("resultset size: " + size);
				if (size==0)//if user logged out from all devices
				{
					stmt = con.createStatement();
					sql = "Update Users set loginStatus=false where user_ID=" + u.getUserID() +";";
					stmt.executeUpdate(sql);
					System.out.println("record updated into the table...");
				}
			}
			status="success";
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			status="error";
		}
		finally{
			
			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());
				status="error";
			}
		}
		return status;
	}
	
	public List<User> Login(User u,String IPAddress)
	{
		List<User> list = new ArrayList<User>();
		try
		{
			int size=0;
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL, USER, PASS);
			//get pwd from DB for that user
			stmt = con.createStatement();
			String sql = "Select pwd from Users where username='" + u.getUserName() +"';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) 
			{

				rs.last();
				size = rs.getRow();
			    rs.beforeFirst();
			    System.out.println("resultset size: " + size);
				if (size>0)
				{
					rs.next();
					String originalPwd = rs.getString(1);
					rs.close();
					//hash pwd
					String hashedPwd= hashPwd(originalPwd,u.getPwd(), "matchPwd");
					System.out.println(hashedPwd);
					if (hashedPwd.toUpperCase().equals("VALID"))
					{
						System.out.println("password matched");
						
						System.out.println("Fetching records about Users...");
						stmt = con.createStatement();
						sql = "Select User_ID,username,Subscription_ID_FK from Users where username='" + u.getUserName() +"' and pwd='"+  originalPwd +"';";
						rs = stmt.executeQuery(sql);
						rs.next();
						int id=rs.getInt("User_ID");
						User newObj=new User();
						newObj.setUserID(id);
						newObj.setUserName(rs.getString("username"));
						newObj.setSubscriptionType(rs.getString("Subscription_ID_FK"));
						newObj.setMessage("success");
						list.add(newObj);
						rs.close();
						
						System.out.println("Updating Login status about Users...");
						stmt = con.createStatement();
						sql = "UPDATE Users set loginStatus=true where User_ID="+ id +";";
						stmt.executeUpdate(sql);
						System.out.println("Updated Login status about Users successfully..");
						
						//insert into login Info
						System.out.println("inserting into Login Info table..");
						stmt = con.createStatement();
						Date dt = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentDate= sdf.format(dt);
						sql = "INSERT INTO LoginInfo(User_ID,deviceIPAddress,loginDate,loginStatus)" +
								"VALUES (" + id +",'"+ IPAddress+"','"+currentDate + "',true);";
						stmt.executeUpdate(sql);

						System.out.println("inserted into Login Info table successfully");
					}
					else
					{
						User newObj=new User();
						newObj.setMessage("Username and Password does not match");
						list.add(newObj);
					}
				}
				else
				{
				User newObj=new User();
				newObj.setMessage("No such username exists");
				list.add(newObj);
				}
			}
			else
			{
				User newObj=new User();
				newObj.setMessage("No such username exists");
				list.add(newObj);
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());

		}
		finally{

			try{
				if(stmt!=null)
					con.close();
			}catch(SQLException se){
				System.out.println(se.getMessage());

			}
			try{
				if(con!=null)
					con.close();

			}catch(SQLException se){
				System.out.println(se.getMessage());

			}
		}
		return list;
	}
	
	public Connection getConnection()
	{
		try {
			Properties props = new Properties();
			FileInputStream in = new FileInputStream("db.properties");
			props.load(in);
			in.close();

			// create a mysql database connection
			String JDBC_DRIVER = props.getProperty("jdbc.driver");
			Class.forName(JDBC_DRIVER);


			String DB_URL = props.getProperty("jdbc.url");
			String USER = props.getProperty("jdbc.username");
			String PASS = props.getProperty("jdbc.password");

			con = DriverManager.getConnection(DB_URL, USER, PASS);

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage()); 
		}catch (SQLException ex) {
			System.out.println(ex.getMessage()); 
		}catch (Exception ex) {
			System.out.println(ex.getMessage()); 
		}
		return con;

	}
	
	public String hashPwd(String oldPwd,String newPwd, String command)
	{
        String generatedSecuredPasswordHash=""; 
    	BCryptPasswordEncoder encoder; 
        if (command=="hashPwd")
        {
        	 encoder=new BCryptPasswordEncoder();
        	 generatedSecuredPasswordHash=encoder.encode(oldPwd);
        }
        else if (command=="matchPwd")
        {
        	encoder=new BCryptPasswordEncoder();
        	boolean matched=encoder.matches(newPwd, oldPwd);
        	if (matched== true)
        	{
        		generatedSecuredPasswordHash="valid";
        	}
        	else
        	{
        		generatedSecuredPasswordHash="invalid";
        	}

        }
                	
        return generatedSecuredPasswordHash;
	}

	
}

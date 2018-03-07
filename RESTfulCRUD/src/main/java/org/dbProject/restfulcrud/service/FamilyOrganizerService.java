package org.dbProject.restfulcrud.service;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.dbProject.restfulcrud.dao.FamilyOrganizerDAO;
import org.dbProject.restfulcrud.model.*;

@Path("/familyOrganizer")
public class FamilyOrganizerService {
	@GET
	@Path("/getSubscriptionData")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Subscription> getSubscriptionData() {
		FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        List<Subscription> subscriptionData = dao.getSubscriptionData();
        return subscriptionData;
    }
	
	@POST
    @Path("/addUser")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<User> addUser(User u){
        u.setUserName(u.getUserName());
        u.setPwd(u.getPwd());
        u.setCustName(u.getCustName());      
        u.setFamilyName(u.getFamilyName());
        u.setEmailID(u.getEmailID());
        u.setSubscriptionType(u.getSubscriptionType());
        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        List<User> list=dao.addUser(u);
        return list;

    }
	
	@POST
    @Path("/updateSubscriptionPlan")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String updateSubscriptionPlan(User u){
        u.setUserName(u.getUserName());
        u.setSubscriptionType(u.getSubscriptionType());
        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.updateSubscriptionPlan(u);
        
        return status;
    }
	
	@POST
    @Path("/addEvent")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String addEvent(CalendarEvent cal){
        cal.setTitle(cal.getTitle());
        cal.setDesc(cal.getDesc());
        cal.setEventUserSelectedDate(cal.getEventUserSelectedDate());
        cal.setUserID(cal.getUserID());
        cal.setNotifyByEmail(cal.isNotifyByEmail());
            
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.addEvent(cal);
        
        return status;
    }
	
	@POST
    @Path("/deleteEvent")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String deleteEvent(CalendarEvent cal){
		cal.setEventID(cal.getEventID());
                       
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.deleteEvent(cal);
        
        return status;
    }
	
	@POST
    @Path("/updateEvent")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String updateEvent(CalendarEvent cal){
		cal.setEventID(cal.getEventID());
        cal.setTitle(cal.getTitle());
        cal.setDesc(cal.getDesc());
        cal.setEventUserSelectedDate(cal.getEventUserSelectedDate());
        cal.setUserID(cal.getUserID());
        cal.setNotifyByEmail(cal.isNotifyByEmail());
                
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.updateEvent(cal);
        
        return status;
    }
	
	@POST
    @Path("/getAllEvents")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<CalendarEvent> getAllEvents(User u){
		u.setUserID(u.getUserID());
		u.setCustName(u.getCustName());
		System.out.println(u.getUserID());
		FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
		List<CalendarEvent> calendarEventData = dao.getAllEvents(u);
		return calendarEventData;
    }
	
	
	@POST
    @Path("/addToDoListItem")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String addToDoListItem(ToDoList item){
        item.setTitle(item.getTitle());
        item.setUserID(item.getUserID());
                   
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.addItemsForToDoList(item);
        
        return status;
    }
	
	@POST
    @Path("/deleteToDoListItem")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String deleteToDoListItem(ToDoList item){
		item.setID(item.getID());
                       
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.deleteItemsForToDoList(item);
        
        return status;
    }
	
	/*@POST
    @Path("/updateToDoListItem")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String updateToDoListItem(ToDoList item){
		item.setID(item.getID());
		item.setTitle(item.getTitle());
                        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.updateItemsForToDoList(item);
        
        return status;
    }*/
	
	@POST
    @Path("/getAllToDoListItems")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<ToDoList> getAllToDoListItems(User u){
		u.setUserID(u.getUserID());
		
		FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
		List<ToDoList> listOfItems = dao.getAllToDoListItems(u);
		return listOfItems;
    }
	
	
	/*@POST
    @Path("/checkValidUser")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String checkValidUser(User u){
		u.setUserName(u.getUserName());
        u.setPwd(u.getPwd());
                        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.checkValidUserLogin(u);
        
        return status;
    }
	*/
	@POST
    @Path("/checkDuplicateUsername")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String checkDuplicateUsername(User u){
		u.setUserName(u.getUserName());
                        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.checkDuplicateUsername(u);
        
        return status;
    }
	
	@POST
    @Path("/updateUserPwd")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String updateUserPwd(User u){
		u.setUserID(u.getUserID());
        u.setPwd(u.getPwd());
        //here as new pwd
        u.setCustName(u.getCustName());             
        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.updateUserPwd(u);
        
        return status;
    }
	
	/*@POST
    @Path("/loginLogoutUser")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String loginLogoutUser(LoginInfo info){
		
		info.setUserID(info.getUserID());
		info.setLoginStatus(info.isLoginStatus());
		info.setDeviceType(info.getDeviceType());
		info.setDeviceIPAddress(info.getDeviceIPAddress());
		info.setOperation(info.getOperation());
		
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.login_logout(info);
        
        return status;
    }*/
	
	
	@POST
	@Path("/checkSubscriptionPlan")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<Subscription> checkSubscriptionPlan(User u) {
		u.setUserID(u.getUserID());
		
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        List<Subscription> subscriptionData = dao.checkSubscriptionPlan(u);
        
        return subscriptionData;
    }
	
	@POST
    @Path("/login")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public List<User> Login(User u){
		String IPAddress=getClientIPAddress();
		
        u.setUserName(u.getUserName());
        u.setPwd(u.getPwd());
       
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        List<User> list=dao.Login(u,IPAddress);
        return list;

    }
	
	@POST
    @Path("/logout")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String Logout(User u){
		String IPAddress=getClientIPAddress();
        u.setUserID(u.getUserID());
        
        FamilyOrganizerDAO dao = new FamilyOrganizerDAO();
        String status=dao.Logout(u,IPAddress);
        return status;

    }
	
	//inject
	@Context
	private HttpServletRequest httpServletRequest;
	 
	public String getClientIPAddress() {
	 //get the ip
	 System.out.println("IP=" + httpServletRequest.getRemoteAddr());
	 return httpServletRequest.getRemoteAddr();
	}
}

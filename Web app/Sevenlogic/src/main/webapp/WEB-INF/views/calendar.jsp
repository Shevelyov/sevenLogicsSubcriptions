<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>
<body>
	<style type="text/css">
.modal-header h1, .modal-header h2, .modal-header h3, .modal-header h4 {
	color: black;
}

.eventModal .modal-content {
	top: 50% left:30%
}
</style>
	<link href='resources/css/bootstrap-datetimepicker.min.css' rel='stylesheet' />
	<%@ include file="navigation.jsp"%>
	<div class="container-fluid">
		<div class="row">
		  <div id="calContent" class="pull-right">
		    <button class="btn addEventShow">Add Event</button>
		  </div>
		</div>
		<div class="row" style="margin-top:10px;">
			<div id="calContent" class="calendarEvt"></div>
		</div>
	</div>
	
	<div id="eventModal" class="eventModal modal fade">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title"></h4>
	      </div>
	      <div class="modal-body">
	      	<div class="form-group sd">
	          <label for="description">Description</label> 
			  <textarea class="form-control" id="description" placeholder="Description"></textarea>
	        </div>
	        <div class="form-group sd">
	          <label for="startDt">Start Date and Time</label> 
			  <input type="text" class="form-control" id="startDt" placeholder="Start Date">
	        </div>
	        <div class="form-group sd">
	          <label for="sendNotification">Send Notification</label> 
			  <input type="checkbox" class="form-control" id="sendNotification">
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default closeBtn">Close</button>
	        <button type="button" class="btn btn-primary updateBtn">Save changes</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div id="addModal" class="addModal modal fade">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h4 class="modal-title">Add new event</h4>
	      </div>
	      <div class="modal-body">
	      	<div class="form-group sd">
	          	<label for="evtName">Event Name</label> 
			  	<input type="text" class="form-control" id="evtName" placeholder="Event Name">
	        </div>
	        <div class="form-group sd">
	          <label for="description">Description</label> 
			  <textarea class="form-control" id="description" placeholder="Description"></textarea>
	        </div>
	        <div class="form-group sd">
	          	<label for="startDt">Start Date and Time</label> 
				<input type="text" class="form-control" id="startDt" placeholder="Start Date">
	        </div>
	        	<div class="form-group sd">
	          <label for="sendNotification">Send Notification</label> 
			  <input type="checkbox" class="form-control" id="sendNotification">
	        </div>
	      </div>
	      <div class="modal-footer">
	       		<button type="button" class="btn btn-default closeBtn">Close</button>
	        		<button type="button" class="btn btn-primary addEvtBtn">Add Event</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<script src='resources/js/bootstrap-datetimepicker.min.js'></script>
	<script src='resources/js/calendar.js'></script>
</body>
</html>

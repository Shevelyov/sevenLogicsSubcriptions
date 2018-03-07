(function() {
  var selectedDt = {};
  var changed = false;

  $(document).ready(function() {
    $('.calendarEvt').fullCalendar({
      events: function(start, end, timezone, callback) {
        $.ajax({
          url: contextPath + 'rest/familyOrganizer/getAllEvents',
          dataType: 'json'
        }).done(function(response) {
          var events = [];
          if (response && response.CalendarEvent && response.CalendarEvent.length > 0) {
            for (var i = 0; i < response.CalendarEvent.length; i++) {
              var calEvent = response.CalendarEvent[i];
              events.push({
                "id": calEvent.eventID,
                "title": calEvent.title,
                "description": calEvent.desc,
                "notifyByEmail": calEvent.notifyByEmail,
                "eventUserSelectedDate": calEvent.eventUserSelectedDate,
                "start": calEvent.eventUserSelectedDate,
                "end": calEvent.eventUserSelectedDate
              });
            }
          }
          callback(events);
        });
      },
      eventRender: function(eventObj, $el) {
        $el.popover({title: eventObj.title, content: eventObj.description, trigger: 'hover', placement: 'top', container: 'body'})
      },
      eventClick: function(calEvent, jsEvent, view) {
        bootbox.dialog({
          message: calEvent.title,
          buttons: {
            confirm: {
              label: 'Edit',
              callback: function(){
                showEvent(calEvent);
              }
            },
            cancel: {
              label: 'Delete',
              className: 'btn-danger',
              callback: function(){
                deleteEvent(calEvent);
              }
            }
          }
        });
      }
    });
    
    var deleteEvent = function(calEvent){
      var postData = {
        "eventID": calEvent.id
      }
      console.log('Delete : ', postData);
      $.ajax({
        url: contextPath + 'rest/familyOrganizer/deleteEvent',
        data: JSON.stringify(postData),
        method: 'POST',
        contentType: 'application/json'
      }).done(function(response) {
        $('.calendarEvt').fullCalendar('refetchEvents');
      });
    }

    $('.updateBtn').on('click', function() {
      var valid = true;
      var clear = false;

      var newStartDt = moment($('.eventModal #startDt').val(), 'YYYY-MM-DD hh:mm').toDate();
      valid = moment(newStartDt).isAfter(new Date());
      if (valid) {
        //POST data bask to backend
        var postData = {
          "eventID": selectedDt.id,
          "title": selectedDt.title,
          "desc": $('.eventModal #description').val(),
          "notifyByEmail": $(".eventModal #sendNotification").is(':checked'),
          "eventUserSelectedDate": moment($('.eventModal #startDt').val(), 'YYYY-MM-DD hh:mm').format('YYYY-MM-DD hh:mm')
        }
        $.ajax({
          url: contextPath + 'rest/familyOrganizer/updateEvent',
          data: JSON.stringify(postData),
          method: 'POST',
          contentType: 'application/json'
        }).done(function(response) {
          $('.calendarEvt').fullCalendar('refetchEvents');
          clearUpdateModal();
        });
      } else {
        bootbox.alert('Please select the proper start time', null);
      }
    });

    var clearUpdateModal = function() {
      if (selectedDt['obj']) {
        selectedDt['obj'].css('border-color', '');
      }
      selectedDt = {};
      changed = false;
      $(".eventModal").modal('hide');
    };

    var showEvent = function(eveDetails) {
      selectedDt = eveDetails;
      $('.eventModal .modal-title').html('Event Detail for : ' + eveDetails.title);
      $('.eventModal #description').html(eveDetails.description);
      var startDt = {
        format: 'yyyy-mm-dd HH:ii P',
        autoclose: true
      };
      if (eveDetails.start) {
        startDt['startDate'] = moment(eveDetails.start).format('YYYY-MM-DD hh:mm A');
        selectedDt['startDate'] = startDt['startDate'];
        $('.eventModal #startDt').val(startDt['startDate']);
      }
      $('.eventModal #sendNotification').prop("checked", (eveDetails.notifyByEmail == 'true'));
      $('.eventModal #startDt').datetimepicker(startDt).on('changeDate', function(eve) {
        changed = true;
      });

      $(".eventModal").modal('show');
    };

    $('.addEvtBtn').on('click', function() {
      var valid = true;

      var newStartDt = moment($('.addModal #startDt').val(), 'YYYY-MM-DD hh:mm A').toDate();
      valid = moment(newStartDt).isAfter(new Date());
      if (!valid) {
        bootbox.alert('Please select the proper start time and end time', null);
      } else {
        if ($('.addModal #evtName').val().length == 0) {
          valid = false;
          bootbox.alert('Please enter the event name', null);
        }
      }

      if (valid) {
        var postData = {
          "title": $('.addModal #evtName').val(),
          "desc": $('.addModal #description').val(),
          "notifyByEmail": $(".addModal #sendNotification").is(':checked'),
          "eventUserSelectedDate": moment($('.addModal #startDt').val(), 'YYYY-MM-DD hh:mm').format('YYYY-MM-DD hh:mm')
        }
        $.ajax({
          url: contextPath + 'rest/familyOrganizer/addEvent',
          data: JSON.stringify(postData),
          method: 'POST',
          contentType: 'application/json'
        }).done(function(response) {
          $('.calendarEvt').fullCalendar('refetchEvents');
          clearAddModal();
        });
      }
    });

    var addEventShow = function() {
      $('.addModal .modal-title').html('Add new event');
      var startDt = {
        format: 'yyyy-mm-dd HH:ii P',
        autoclose: true
      };
      startDt['startDate'] = moment().format('YYYY-MM-DD hh:mm A');
      selectedDt['startDate'] = startDt['startDate'];
      $('.addModal #startDt').val(startDt['startDate']);

      $('.addModal #startDt').datetimepicker(startDt);
      $(".addModal").modal('show');
    };

    var clearAddModal = function() {
      selectedDt = {};
      changed = false;
      $(".addModal").modal('hide');
    };

    $('.eventModal .closeBtn').on('click', clearUpdateModal);
    $('.addEventShow').on('click', addEventShow);
    $('.addModal .closeBtn').on('click', clearAddModal);
  });
})();
(function(){
  $(document).ready(function() {
    var userInfo;
    if(sessionStorage.getItem('loggedInUser')) {
      $('.nav.navbar-nav #userInfo').show();
      $('.nav.navbar-nav #calendarPage').show();
      userInfo = JSON.parse(sessionStorage.getItem('loggedInUser'));
      $('#loggedInUser').html(userInfo.userName);
      $('.loginBtn').remove();
      
      $('#logoutBtn').on('click', function() {
        $.ajax({
          url: contextPath + 'rest/familyOrganizer/logout',
          method: 'POST'
        }).done(function(response) {
          sessionStorage.clear();
          $('.nav.navbar-nav #userInfo').hide();
          $('.nav.navbar-nav #calendarPage').hide();
          window.location.reload();
        });
      });
    } 
  });
})();
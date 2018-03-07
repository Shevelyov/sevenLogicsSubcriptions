(function(){
  $(document).ready(function() {
    if(sessionStorage.getItem('loggedInUser')) {
      $('.loginBtn').remove();
    } else {
      $('#loginBtn').on('click', function() {
        var postData = {
          "userName": $('#username').val(),
          "pwd": $('#password').val()
        }
        $.ajax({
          url: contextPath + 'rest/familyOrganizer/login',
          data: JSON.stringify(postData),
          method: 'POST',
          contentType: 'application/json',
          dataType: 'json'
        }).done(function(response) {
          sessionStorage.setItem('loggedInUser', JSON.stringify(response));
          $('#login-modal').modal('hide');
          window.location.reload();
        });
      });
    }
  });
})()
$('#sign-btn').on('click', function() {
    var email = $('#email').val();
    var password = $('#password').val();
    
    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/api/v1/signin',
        contentType: 'application/json',
        data: JSON.stringify({
            uemail: email,
            upassword: password
        }),
        success: function(response) {
            console.log(response);
            if (response.code === '200') {
                localStorage.setItem('uemail', email);
                window.location.href = 'dashboard.html';
            } else {
                alert('Error: ' + response.message);
            }
        },
    });
});
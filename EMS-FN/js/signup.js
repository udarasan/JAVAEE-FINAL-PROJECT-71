$('#sign-up-btn').on('click', function() {
    var name = $('#name').val();
    var email = $('#email').val();
    var password = $('#password').val();

    if (name && email && password) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/EMS_Web_exploded/api/v1/signup',
            data: JSON.stringify({ 
                uname: name,
                uemail: email, 
                upassword: password 
            }),
            contentType: 'application/json',
            success: function(response) {
                console.log(response.code);
                if (response.code==201) {
                    console.log('User registered successfully');
                    window.location.href = 'signin.html';
                } else {
                    $('#error-message').text(response.message).show();
                }
            },
            error: function() {
                $('#error-message').text('An error occurred. Please try again.').show();
            }
        });
    } else {
        $('#error-message').text('Please enter both email and password.').show();
    }
})
//window open
$(document).ready(function() {
    // Check if user is logged in
    var email = localStorage.getItem('uemail');
    if (!email) {
        window.location.href = 'signin.html';
    } else {
        $('#welcome-message').text('Welcome, ' + email);
        // Fetch and display employees
        fetchEmployees();
    }
});

$('#save-employee').on('click', function() {

    // Collect form data
    const formData=new FormData();
    formData.append('ename', $('#ename').val());
    formData.append('eemail', $('#eemail').val());
    formData.append('eaddress', $('#eaddress').val());

    const fileInput = $('#eimage')[0];
    if (fileInput.files.length > 0) {
        formData.append('eimage', fileInput.files[0]);
    }else {
        alert('Please select an image file.');
        return;
    }
    // Send AJAX request to save employee
    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/api/v1/employee',
        data: formData,
        processData: false, // Important for FormData
        contentType: false, // Important for FormData
        success: function(response) {
            console.log(response);
            if (response.code === '201') {
                alert('Employee saved successfully!');
                window.location.reload(); // Reload the page to see the updated list
            } else {
                alert('Error: ' + response.message);
            }
        },
        error: function() {
            alert('Failed to save employee.');
        }
    });






    // var ename = $('#ename').val();
    // var eemail= $('#eemail').val();
    // var eaddress = $('#eaddress').val();
    
    // $.ajax({
    //     method: 'POST',
    //     url: 'http://localhost:8080/EMS_Web_exploded/api/v1/employee',
    //     contentType: 'application/json',
    //     data: JSON.stringify({
    //         ename: ename,
    //         eaddress: eaddress,
    //         eemail: eemail
    //     }),
    //     success: function(response) {
    //         console.log(response);
    //         if (response.code === '200') {
    //             alert('Employee saved successfully!');
    //             window.location.reload(); // Reload the page to see the updated list
    //         } else {
    //             alert('Error: ' + response.message);
    //         }
    //     },
    // });
});

function fetchEmployees() {
    $.ajax({
        method: 'GET',
        url: 'http://localhost:8080/EMS_Web_exploded/api/v1/employee',
        success: function(response) {
            if (response.code === '200') {
                var employees = response.data;
                var employeeTable = $('#employee-table tbody');
                employeeTable.empty(); // Clear existing rows
                employees.forEach(function(employee) {
                    employeeTable.append(
                        `<tr>
                            <td>
                                <button class="btn btn-primary" onclick="editEmployee(${employee.eid})">Edit</button>
                                <button class="btn btn-danger" onclick="deleteEmployee(${employee.eid})">Delete</button>
                            </td>
                            <td>${employee.ename}</td>
                    
                             <td>${employee.eaddress}</td>
                            
                            <td>${employee.eemail}</td>
                            <td><img src="/assests/${employee.eimage}" alt="Employee Image" style="width: 50px; height: 50px;"></td>
                            
                        </tr>`
                    );
                });
            } else {
                alert('Error fetching employees: ' + response.message);
            }
        },
        error: function() {
            alert('Failed to fetch employees.');
        }
    });
}
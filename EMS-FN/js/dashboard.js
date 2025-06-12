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
    var ename = $('#ename').val();
    var eemail= $('#eemail').val();
    var eaddress = $('#eaddress').val();
    
    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/EMS_Web_exploded/api/v1/employee',
        contentType: 'application/json',
        data: JSON.stringify({
            ename: ename,
            eaddress: eaddress,
            eemail: eemail
        }),
        success: function(response) {
            console.log(response);
            if (response.code === '200') {
                alert('Employee saved successfully!');
                window.location.reload(); // Reload the page to see the updated list
            } else {
                alert('Error: ' + response.message);
            }
        },
    });
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
$(document).ready(function () {

    $.validator.addMethod(
        "emailValidate",
        function (value, element, params) {
            const regex = new RegExp("^\\w*@\\w{5}\\.\\w{3}");

            return regex.test(value) === params;
        },
        "Email is invalid. Please enter your correct email"
    );

    $("#forgotPass").validate({
        rules: {
            email: {
                required: true,
                emailValidate: true,
                maxlength: 50,
            },

            password: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },

            repassword: {
                required: true,
                minlength: 3,
                maxlength: 50,
                equalTo: "#password",
            },

        },
        messages: {
            email: {
                maxlength: "Email is not exceed 50 characters",
            },

            password: {
                required: "Please input your password",
                minlength: "At least 3 characters",
                maxlength: "Not exceeded 50 characters",
            },
            repassword: {
                required: "Please enter your password again",
                minlength: "At least 3 characters",
                maxlength: "Not exceed 50 characters",
                equalTo: "Password doesn't match!",
            },
        },
    });

    $(document).on("click", "#btnSubmit", function submitForm() {
        if ($("form").valid()) {
            $('#submitModal').modal('show');
        }
    });
});
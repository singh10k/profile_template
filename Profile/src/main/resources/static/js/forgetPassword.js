$(document).ready(function () {
       $('#checkUsername').click(function () {
              sendUserBasedOtp();
       });

       $('#reSendOtp').click(function () {
              sendUserBasedOtp();
       });

       function sendUserBasedOtp() {
              const username = $('#username').val();
              $.ajax({
                     type: 'POST',
                     url: 'username',
                     dataType: 'json',
                     cache: false,
                     data: {
                            username: username
                     },
                     success: function (data, textStatus, jqXHR) {
                            console.log(textStatus);
                            updateStatus(data)
                     },
                     error: function (jqXHR, textStatus, errorThrown) {
                            console.log(textStatus);
                     }
              });
       }

       function updateStatus(data) {
              if (data.status) {
                     if (data.otp_status) {
                            $('#otpField').show();
                            $('#invalidUsername').hide();
                            $('#errorField').hide();
                            $('#errorFieldOtp').html('');
                            $('#emailField').show();
                            $('#emailFieldDiv').html(data.email);
                     } else {
                            $('#errorField').show();
                            $('#errorFieldOtp').html(data.errorMsg);
                     }
              } else {
                     $('#invalidUsername').show();
                     $('#otpField').hide();
              }

       }

       $('#otpVerify').click(function(){
       const username = $('#username').val();
                     const otp = $('#otp').val();
                     $.ajax({
                            type: 'POST',
                            url: 'otpVerify',
                            dataType: 'json',
                            cache: false,
                            data: {
                                   username: username,
                                   otp: otp
                            },
                            success: function (data, textStatus, jqXHR) {
                                   console.log(textStatus);
                                   if (data.status) {
                                          $('#updatePassword').show();
                                          $('#forgetPasswordField').show();
                                          $('#otpField').hide();
                                          $('#errorField').hide();
                                          $('#errorFieldOtp').html(data.msg);
                                           $('#errorFieldOtp').html('');
                                           $('#reSendOtp').show();
                                   } else {
                                           $('#forgetPasswordField').hide();
                                           $('#reSendOtp').hide();
                                         $('#errorField').show();
                             $('#errorFieldOtp').html(data.msg);
                                   }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                   console.log(textStatus);
                            }
                     });
       })

       $('#updatePassword').click(function () {
              const username = $('#username').val();
              const password = $('#password').val();
              $.ajax({
                     type: 'POST',
                     url: 'forgetPassword',
                     dataType: 'json',
                     cache: false,
                     data: {
                            username: username,
                            password: password
                     },
                     success: function (data, textStatus, jqXHR) {
                            console.log(textStatus);
                            if (data.Status) {
                                   $('#updatePassField').show();
                                   $('#errorUpdatePassField').hide();
                            } else {
                                   $('#errorUpdatePassField').show();
                                   $('#updatePassField').hide();
                            }
                     },
                     error: function (jqXHR, textStatus, errorThrown) {
                            console.log(textStatus);
                     }
              });
       });
});
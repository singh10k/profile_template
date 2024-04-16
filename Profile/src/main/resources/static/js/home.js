function showLogin(){
	$.ajax({
            dataType: 'html',
            type: 'get',
            url: 'showLogin',
            traditional: true,
            cache: false,
            async: true,
            data: {},
            success: function (data, textStatus, jqXHR) {
                $('#loginPage').html(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                stopLoader();
            }
        });
}
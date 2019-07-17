$(document).ready(function () {
    $(document).on('click', '.product-detail', function () {
        var hostName = $(location).attr('origin');
        location.href = hostName + '/public/product-detail/' + parseInt($(this).attr('data-id'));
    });
});

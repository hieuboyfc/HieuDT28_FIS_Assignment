$(document).ready(function () {

    /* Mở form hãng sản xuất */
    $(document).on('click', '#btnAddNew', function () {
        let btnSubmitManufacturer = $('#btnSubmitManufacturer');
        btnSubmitManufacturer.removeClass('btnManufacturerEdit');
        btnSubmitManufacturer.addClass('btnManufacturerAdd');
        btnSubmitManufacturer.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        $('#manufacturerModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin hãng sản xuất');
        let formManufacturer = $('#formManufacturer');
        formManufacturer[0].reset();
        formManufacturer.validate().resetForm();
        $('#manufacturerModal').modal('show');
    });

    /* Thực hiện thêm dữ liệu */
    $('#formManufacturer').validate({
        rules: {
            txtManufacturer_Name: {
                required: true,
                maxlength: 50,
            }
        },
        messages: {
            txtManufacturer_Name: {
                required: 'Tên hãng sản xuất không được bỏ trống',
                maxlength: 'Tên hãng sản xuất phải nhỏ hơn 50 ký tự',
            }
        },
        submitHandler: function () {
            let data = {
                name: $.trim($('#inpManufacturer_Name').val()),
            };
            saveDataManufacturer(data);
        }
    });

    /* Sửa dữ liệu */
    $(document).on('click', '#btnEditManufacturer', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/manufacturer/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#manufacturerModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin hãng sản xuất');
                    let btnSubmitManufacturer = $('#btnSubmitManufacturer');
                    btnSubmitManufacturer.html('<i class="fa fa-edit"></i> Cập nhật');
                    btnSubmitManufacturer.attr('data-id', data.id);
                    btnSubmitManufacturer.removeClass('btnManufacturerAdd');
                    btnSubmitManufacturer.addClass('btnManufacturerEdit');
                    let formManufacturer = $('#formManufacturer');
                    formManufacturer.validate().resetForm();
                    formManufacturer.attr('data-id', id);
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu hãng sản xuất', 'error');
            }
        })
    });

    /* Xóa vai trò */
    $(document).on('click', '#btnDeleteManufacturer', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        deleteManufacturer(data.toString());
    });

    /* Phân trang */
    let totalpage = parseInt($('#data_paging').attr('data-total'));
    if (totalpage > 1) {
        $('.pagination').twbsPagination({
            totalPages: totalpage,
            visiblePages: 5,
            first: '|<<<',
            prev: '|<',
            next: '>|',
            last: '>>>|',
            onPageClick: function (event, page) {
                let curentPage = parseInt($('#data_paging').attr('data-page'));
                let showPage = $('#show_page').val();
                if (curentPage !== page) {
                    if (typeof showPage !== 'undefined') pageSize = parseInt(showPage);
                    let data = {
                        name: $.trim($('#manufacturer_search_name').val()).toLowerCase(),
                        pageIndex: page,
                        pageSize: pageSize,
                        column: typeof column !== 'undefined' ? column : 'id',
                        desending: typeof desending !== 'undefined' ? desending : 1
                    };
                    hieuboy_cms.common().loadAllDataPage(link + '/api/admin/manufacturer/loaddata', 'data_table_manufacturer', data);
                }
            }
        });
    }

    /* Show bản ghi */
    $(document).on('change', '#show_page', function () {
        reloadDataManufacturer();
    });

    /* Tìm kiếm user */
    $(document).on('click', '#btnManufacturerSearch', function () {
        reloadDataManufacturer();
    });

    /* Sắp xếp user */
    $(document).on('click', '.sort', function () {
        let _this = $(this);
        let col = _this.attr('data-col');
        hieuboy_cms.common().sortingList(_this, reloadDataManufacturer, col);
    });

});

function setDataForm(data) {
    $.LoadingOverlay('hide');
    $('#inpManufacturer_Name').val(data.name);
    $('#manufacturerModal').modal('show');
}

function saveDataManufacturer(data) {
    let btnSubmitManufacturer = $('#btnSubmitManufacturer');
    if (btnSubmitManufacturer.hasClass('btnManufacturerAdd')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/manufacturer/save',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới hãng sản xuất thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới loại sẩn phẩm thất bại', 'error');
            }
        }).done(function () {
            reloadDataManufacturer();
            $('#manufacturerModal').modal('hide');
        });
    }
    if (btnSubmitManufacturer.hasClass('btnManufacturerEdit')) {
        data.id = parseInt(btnSubmitManufacturer.attr('data-id'));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/manufacturer/update',
            data: data,
            success: function (resp) {
                if (resp.result) {
                    switch (resp.code) {
                        case 0:
                            hieuboy_cms.common().notification('Cập nhật hãng sản xuất thất bại', 'error');
                            break;
                        case 1:
                            reloadDataManufacturer();
                            $('#manufacturerModal').modal('hide');
                            hieuboy_cms.common().notification('Cập nhật hãng sản xuất thành công', 'success');
                            break;
                        case 2:
                            hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu hãng sản xuất', 'error');
                            break;
                    }
                }
            }
        })
    }
}

function deleteManufacturer(data) {
    swal({
        title: 'Thông báo',
        text: 'Bạn có muốn xóa hãng sản xuất?',
        icon: 'warning',
        dangerMode: true,
        buttons: {
            cancel: {
                text: 'Trở lại',
                visible: true,
            },
            confirm: {
                text: 'Đồng ý'
            },
        },
    }).then((isConfirm) => {
        if (isConfirm) {
            $.ajax({
                type: 'DELETE',
                url: link + '/api/admin/manufacturer/delete/' + data,
                success: function (resp) {
                    if (resp) hieuboy_cms.common().notification('Xóa hãng sản xuất thành công', 'success');
                    else hieuboy_cms.common().notification('Xóa hãng sản xuất thất bại', 'error');
                },
            }).done(function () {
                reloadDataManufacturer();
            });
        }
    });
}

function reloadDataManufacturer(column, desending) {
    $('.checkItemAll').prop('checked', false);
    $('#btnDelete').attr('disabled', 'disabled');
    let showPage = $('#show_page').val();
    if (typeof showPage !== 'undefined') pageSize = parseInt(showPage);
    let data = {
        name: $.trim($('#manufacturer_search_name').val()).toLowerCase(),
        pageIndex: 1,
        pageSize: pageSize,
        column: typeof column !== 'undefined' ? column : 'id',
        desending: typeof desending !== 'undefined' ? desending : 1
    };
    hieuboy_cms.common().loadDataAndPageAll(link + '/api/admin/manufacturer/loaddata', 'data_table_manufacturer', data);
}
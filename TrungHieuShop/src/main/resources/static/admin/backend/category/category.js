$(document).ready(function () {

    /* Mở form loại sản phẩm */
    $(document).on('click', '#btnAddNew', function () {
        let btnSubmitCategory = $('#btnSubmitCategory');
        btnSubmitCategory.removeClass('btnCategoryEdit');
        btnSubmitCategory.addClass('btnCategoryAdd');
        btnSubmitCategory.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        $('#categoryModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin loại sản phẩm');
        let formCategory = $('#formCategory');
        formCategory[0].reset();
        formCategory.validate().resetForm();
        $('#categoryModal').modal('show');
    });

    /* Thực hiện thêm dữ liệu */
    $('#formCategory').validate({
        rules: {
            txtCategory_Name: {
                required: true,
                maxlength: 50,
            }
        },
        messages: {
            txtCategory_Name: {
                required: 'Tên loại sản phẩm không được bỏ trống',
                maxlength: 'Tên loại sản phẩm phải nhỏ hơn 50 ký tự',
            }
        },
        submitHandler: function () {
            let data = {
                name: $.trim($('#inpCategory_Name').val()),
            };
            saveDataCategory(data);
        }
    });

    /* Sửa dữ liệu */
    $(document).on('click', '#btnEditCategory', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/category/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#categoryModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin loại sản phẩm');
                    let btnSubmitCategory = $('#btnSubmitCategory');
                    btnSubmitCategory.html('<i class="fa fa-edit"></i> Cập nhật');
                    btnSubmitCategory.attr('data-id', data.id);
                    btnSubmitCategory.removeClass('btnCategoryAdd');
                    btnSubmitCategory.addClass('btnCategoryEdit');
                    let formCategory = $('#formCategory');
                    formCategory.validate().resetForm();
                    formCategory.attr('data-id', id);
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu loại sản phẩm', 'error');
            }
        })
    });

    /* Xóa vai trò */
    $(document).on('click', '#btnDeleteCategory', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        deleteCategory(data.toString());
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
                        name: $.trim($('#category_search_name').val()).toLowerCase(),
                        pageIndex: page,
                        pageSize: pageSize,
                        column: typeof column !== 'undefined' ? column : 'id',
                        desending: typeof desending !== 'undefined' ? desending : 1
                    };
                    hieuboy_cms.common().loadAllDataPage(link + '/api/admin/category/loaddata', 'data_table_category', data);
                }
            }
        });
    }

    /* Show bản ghi */
    $(document).on('change', '#show_page', function () {
        reloadDataCategory();
    });

    /* Tìm kiếm user */
    $(document).on('click', '#btnCategorySearch', function () {
        reloadDataCategory();
    });

    /* Sắp xếp user */
    $(document).on('click', '.sort', function () {
        let _this = $(this);
        let col = _this.attr('data-col');
        hieuboy_cms.common().sortingList(_this, reloadDataCategory, col);
    });

});

function setDataForm(data) {
    $.LoadingOverlay('hide');
    $('#inpCategory_Name').val(data.name);
    $('#categoryModal').modal('show');
}

function saveDataCategory(data) {
    let btnSubmitCategory = $('#btnSubmitCategory');
    if (btnSubmitCategory.hasClass('btnCategoryAdd')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/category/save',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới loại sản phẩm thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới loại sẩn phẩm thất bại', 'error');
            }
        }).done(function () {
            reloadDataCategory();
            $('#categoryModal').modal('hide');
        });
    }
    if (btnSubmitCategory.hasClass('btnCategoryEdit')) {
        data.id = parseInt(btnSubmitCategory.attr('data-id'));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/category/update',
            data: data,
            success: function (resp) {
                if (resp.result) {
                    switch (resp.code) {
                        case 0:
                            hieuboy_cms.common().notification('Cập nhật loại sản phẩm thất bại', 'error');
                            break;
                        case 1:
                            reloadDataCategory();
                            $('#categoryModal').modal('hide');
                            hieuboy_cms.common().notification('Cập nhật loại sản phẩm thành công', 'success');
                            break;
                        case 2:
                            hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu loại sản phẩm', 'error');
                            break;
                    }
                }
            }
        })
    }
}

function deleteCategory(data) {
    swal({
        title: 'Thông báo',
        text: 'Bạn có muốn xóa loại sản phẩm?',
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
                url: link + '/api/admin/category/delete/' + data,
                success: function (resp) {
                    if (resp) hieuboy_cms.common().notification('Xóa loại sản phẩm thành công', 'success');
                    else hieuboy_cms.common().notification('Xóa loại sản phẩm thất bại', 'error');
                },
            }).done(function () {
                reloadDataCategory();
            });
        }
    });
}

function reloadDataCategory(column, desending) {
    $('.checkItemAll').prop('checked', false);
    $('#btnDelete').attr('disabled', 'disabled');
    let showPage = $('#show_page').val();
    if (typeof showPage !== 'undefined') pageSize = parseInt(showPage);
    let data = {
        name: $.trim($('#category_search_name').val()).toLowerCase(),
        pageIndex: 1,
        pageSize: pageSize,
        column: typeof column !== 'undefined' ? column : 'id',
        desending: typeof desending !== 'undefined' ? desending : 1
    };
    hieuboy_cms.common().loadDataAndPageAll(link + '/api/admin/category/loaddata', 'data_table_category', data);
}
$(document).ready(function () {

    /* Mở form sản phẩm */
    $(document).on('click', '#btnAddNew', function () {
        let btnSubmitProduct = $('#btnSubmitProduct');
        btnSubmitProduct.removeClass('btnProductEdit');
        btnSubmitProduct.addClass('btnProductAdd');
        btnSubmitProduct.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        $('#productModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin sản phẩm');
        $('#formProduct :input').prop('disabled', false);
        let formProduct = $('#formProduct');
        formProduct[0].reset();
        formProduct.validate().resetForm();
        $('#productModal').modal('show');
    });

    /* Thực hiện thêm dữ liệu */
    $('#formProduct').validate({
        rules: {
            txtProduct_Name: {
                required: true,
                maxlength: 50,
            },
            slProduct_Category: {
                valueNotEquals: '',
            },
            slProduct_Manufacturer: {
                valueNotEquals: '',
            },
            txtProduct_Quantity: {
                required: true,
                number: true,
            },
            txtProduct_Price: {
                required: true,
                number: true,
            },
            txtProduct_Discount: {
                number: true,
            },
            txtProduct_Content: {
                maxlength: 1000,
            },
            file: {
                extension: 'jpg|jpeg|gif|png',
                filesize: 5
            }
        },
        messages: {
            txtProduct_Name: {
                required: 'Tên sản phẩm không được bỏ trống',
                maxlength: 'Tên sản phẩm phải nhỏ hơn 50 ký tự',
            },
            slProduct_Category: {
                valueNotEquals: 'Loại sản phẩm không được bỏ trống'
            },
            slProduct_Manufacturer: {
                valueNotEquals: 'Hãng sản xuất không được bỏ trống'
            },
            txtProduct_Quantity: {
                required: 'Số lượng không được bỏ trống',
                number: 'Số lượng phải là số',
            },
            txtProduct_Price: {
                required: 'Đơn giá không được bỏ trống',
                number: 'Đơn giá phải là số',
            },
            txtProduct_Discount: {
                number: 'Giảm giá phải là số'
            },
            txtProduct_Content: {
                maxlength: 'Nội dung phải nhỏ hơn 1000 ký tự'
            },
            file: {
                extension: 'Hình ảnh phải là định dạng jpg, jpeg, gif, png',
                filesize: 'Hình ảnh không được lớn hơn 5MB'
            }
        },
        ignore: ':hidden:not(select)',
        errorPlacement: function (error, element) {
            if (element.attr('name') === 'slProduct_Category')
                error.insertAfter($('#slProduct_Category_chosen'));
            else if (element.attr('name') === 'slProduct_Manufacturer')
                error.insertAfter($('#slProduct_Manufacturer_chosen'));
            else
                error.insertAfter(element);
        },
        submitHandler: function () {
            let imageLink = $('#inpProduct_ImageLink');
            let category = $('#slProduct_Category');
            let manufacturer = $('#slProduct_Manufacturer');
            let status = $('#slProduct_Status');
            let data = new FormData();
            data.append('file', imageLink.val() ? imageLink[0].files[0] : undefined);
            data.append('name', $.trim($('#inpProduct_Name').val()));
            data.append('categoryID', category.val() != null ? category.val() : '');
            data.append('manufacturerID', manufacturer.val() != null ? manufacturer.val() : '');
            data.append('quantity', $.trim($('#inpProduct_Quantity').val()));
            data.append('price', $.trim($('#inpProduct_Price').val()));
            data.append('discount', $.trim($('#inpProduct_Discount').val()));
            data.append('status', status.val() != null ? status.val() : '');
            data.append('content', $.trim($('#inpProduct_Content').val()));
            saveDataProduct(data);
        }
    });

    /* Sửa dữ liệu */
    $(document).on('click', '#btnEditProduct', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/product/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#formProduct :input').prop('disabled', false);
                    $('#productModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin sản phẩm');
                    let btnSubmitProduct = $('#btnSubmitProduct');
                    btnSubmitProduct.html('<i class="fa fa-edit"></i> Cập nhật');
                    btnSubmitProduct.attr('data-id', data.id);
                    btnSubmitProduct.removeClass('btnProductAdd');
                    btnSubmitProduct.addClass('btnProductEdit');
                    let formProduct = $('#formProduct');
                    formProduct.validate().resetForm();
                    formProduct.attr('data-id', id);
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu sản phẩm', 'error');
            }
        })
    });

    /* Xem dữ liệu */
    $(document).on('click', '#btnViewProduct', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/product/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#productModal .modal-title').html('<i class="fa fa-search"></i> Xem chi tiết thông tin sản phẩm');
                    let btnSubmitProduct = $('#btnSubmitProduct');
                    btnSubmitProduct.hide();
                    $('#formProduct :input').not('#btnClose').prop('disabled', true);
                    let formProduct = $('#formProduct');
                    formProduct.validate().resetForm();
                    formProduct.attr('data-id', id);
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu sản phẩm', 'error');
            }
        })
    });


    /* Tải file đính kèm hơp đồng */
    $(document).on('click', '#btnViewImage', function () {
        var urlFile = $(this).attr('data-image');
        if ($.trim(urlFile).length > 0) {
            window.location = urlFile;
            target = "_blank";
            done = 1;
        }
    });


    /* Xóa sản phẩm */
    $(document).on('click', '#btnDeleteProduct', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        deleteProduct(data.toString());
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
                        name: $.trim($('#product_search_name').val()).toLowerCase(),
                        categoryID: $('#searchCategory').val(),
                        manufacturerID: $('#searchManufacturer').val(),
                        code: $.trim($('#product_search_code').val()).toLowerCase(),
                        status: $('#product_search_status').val(),
                        pageIndex: page,
                        pageSize: pageSize,
                        column: typeof column !== 'undefined' ? column : 'id',
                        desending: typeof desending !== 'undefined' ? desending : 1
                    };
                    hieuboy_cms.common().loadAllDataPage(link + '/api/admin/product/loaddata', 'data_table_product', data);
                }
            }
        });
    }

    /* Show bản ghi */
    $(document).on('change', '#show_page', function () {
        reloadDataProduct();
    });

    /* Tìm kiếm user */
    $(document).on('click', '#btnProductSearch', function () {
        reloadDataProduct();
    });

    /* Sắp xếp user */
    $(document).on('click', '.sort', function () {
        let _this = $(this);
        let col = _this.attr('data-col');
        hieuboy_cms.common().sortingList(_this, reloadDataProduct, col);
    });

});

function setDataForm(data) {
    $.LoadingOverlay('hide');
    $('#inpProduct_Name').val($.trim(data.name));
    $('#slProduct_Category').val(data.categoryID != null ? data.categoryID : '').trigger('chosen:updated');
    $('#slProduct_Manufacturer').val(data.manufacturerID != null ? data.manufacturerID : '').trigger('chosen:updated');
    $('#inpProduct_Quantity').val($.trim(data.quantity));
    $('#inpProduct_Price').val($.trim(data.price));
    $('#inpProduct_Discount').val($.trim(data.discount));
    $('#slProduct_Status').val(data.status != null ? data.status : '1').trigger('chosen:updated');
    /* $('#inpProduct_ImageLink').val(data.imageLink);*/
    $('#inpProduct_Content').val($.trim(data.content));
    $('#productModal').modal('show');
}

function saveDataProduct(data) {
    let btnSubmitProduct = $('#btnSubmitProduct');
    if (btnSubmitProduct.hasClass('btnProductAdd')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/product/save',
            data: data,
            contentType: false,
            processData: false,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới sản phẩm thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới sẩn phẩm thất bại', 'error');
            }
        }).done(function () {
            reloadDataProduct();
            $('#productModal').modal('hide');
        });
    }
    if (btnSubmitProduct.hasClass('btnProductEdit')) {
        data.append('id', parseInt(btnSubmitProduct.attr('data-id')));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/product/update',
            data: data,
            contentType: false,
            processData: false,
            success: function (resp) {
                if (resp.result) {
                    switch (resp.code) {
                        case 0:
                            hieuboy_cms.common().notification('Cập nhật sản phẩm thất bại', 'error');
                            break;
                        case 1:
                            reloadDataProduct();
                            $('#productModal').modal('hide');
                            hieuboy_cms.common().notification('Cập nhật sản phẩm thành công', 'success');
                            break;
                        case 2:
                            hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu sản phẩm', 'error');
                            break;
                    }
                }
            }
        })
    }
}

function deleteProduct(data) {
    swal({
        title: 'Thông báo',
        text: 'Bạn có muốn xóa sản phẩm?',
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
                url: link + '/api/admin/product/delete/' + data,
                success: function (resp) {
                    if (resp) hieuboy_cms.common().notification('Xóa sản phẩm thành công', 'success');
                    else hieuboy_cms.common().notification('Xóa sản phẩm thất bại', 'error');
                },
            }).done(function () {
                reloadDataProduct();
            });
        }
    });
}

function reloadDataProduct(column, desending) {
    $('.checkItemAll').prop('checked', false);
    $('#btnDelete').attr('disabled', 'disabled');
    let showPage = $('#show_page').val();
    if (typeof showPage !== 'undefined') pageSize = parseInt(showPage);
    let data = {
        name: $.trim($('#product_search_name').val()).toLowerCase(),
        categoryID: $('#searchCategory').val(),
        manufacturerID: $('#searchManufacturer').val(),
        code: $.trim($('#product_search_code').val()).toLowerCase(),
        status: $('#product_search_status').val(),
        pageIndex: 1,
        pageSize: pageSize,
        column: typeof column !== 'undefined' ? column : 'id',
        desending: typeof desending !== 'undefined' ? desending : 1
    };
    hieuboy_cms.common().loadDataAndPageAll(link + '/api/admin/product/loaddata', 'data_table_product', data);
}
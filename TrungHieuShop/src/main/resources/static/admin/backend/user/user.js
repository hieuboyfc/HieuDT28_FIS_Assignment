$(document).ready(function () {

    $.validator.setDefaults({
        ignore: ":hidden:not(select)",
        errorPlacement: function (error, element) {
            if (element.attr('name') === 'txtUser_RoleGroup')
                error.insertAfter("#slUser_RoleGroup_chosen");
            else
                error.insertAfter(element);
        }
    });

    /* Format ngày tháng */
    $('#inpUser_Birthday').datepicker({
        startDate: '01/01/1900',
        format: 'dd/mm/yyyy',
        endDate: '0'
    });

    $(document).on('click', '.btnBirthday', function () {
        let _this = $(this);
        if (_this.hasClass('btnBirthday')) {
            $('#inpUser_Birthday').datepicker('show');
        }
    });

    /* Modal lock */
    $(document).on('change', '#inpUser_Islock', function () {
        if ($(this).prop('checked'))
            $('#lockRegion').show();
        else
            $('#lockRegion').hide();
    });

    /* Mở form thêm mới người dùng */
    $(document).on('click', '#btnAddNew', function () {
        let btnSubmitUser = $('#btnSubmitUser');
        btnSubmitUser.removeClass('btnUserEdit');
        btnSubmitUser.addClass('btnUserAdd');
        btnSubmitUser.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        $('#userModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin người dùng');
        $('#password , #btnSubmitUser, .btnBirthday').show();
        let formUser = $('#formUser');
        formUser[0].reset();
        formUser.validate().resetForm();
        $('#formUser :input').not('#btnClose').prop('disabled', false).trigger("chosen:updated");
        $('#lockRegion, #lock').hide();
        $('#userModal').modal('show');
    });

    /* Thực hiện thêm dữ liệu */
    $('#formUser').validate({
        rules: {
            txtUser_Username: {
                required: true,
                minlength: 6,
                maxlength: 50,
                remote: {
                    url: link + '/api/admin/user/checkusername',
                    type: 'POST',
                    data: {
                        username: function () {
                            return $('#inpUser_Username').val();
                        }
                    }
                }
            },
            txtUser_Password: {
                required: true,
                minlength: 6,
                maxlength: 100
            },
            txtUser_ConfirmPassword: {
                required: true,
                minlength: 6,
                maxlength: 100,
                equalTo: '#inpUser_Password'
            },
            txtUser_Fullname: {
                required: true,
                maxlength: 50
            },
            txtUser_Address: {
                maxlength: 100
            },
            txtUser_Email: {
                required: true,
                maxlength: 50,
                email: true,
                remote: {
                    url: link + '/api/admin/user/checkemail',
                    type: 'POST',
                    data: {
                        email: function () {
                            return $('#inpUser_Email').val();
                        }
                    }
                }
            },
            txtUser_Phone: {
                required: true,
                minlength: 10,
                maxlength: 20,
                telphone: true
            },
            txtUser_Lockreason: {
                required: !$('#inpUser_Islock').is(':checked'),
                maxlength: 100
            },
            slUser_Position: {
                valueNotEquals2: "",
            },
            slUser_Organization: {
                valueNotEquals2: "",
            },
            txtUser_RoleGroup: {
                required: true,
            }
        },
        messages: {
            txtUser_Username: {
                required: 'Tên người dùng không được bỏ trống',
                minlength: 'Tên người dùng phải lớn hơn 6 ký tự',
                maxlength: 'Tên người dùng phải nhỏ hơn 650 ký tự',
                remote: 'Tên người dùng đã tồn tại',
            },
            txtUser_Password: {
                required: 'Mật khẩu không được bỏ trống',
                minlength: 'Mật khẩu phải lớn hơn 6 ký tự',
                maxlength: 'Mật khẩu phải nhỏ hơn 100 ký tự',
            },
            txtUser_ConfirmPassword: {
                required: 'Mật khẩu xác nhận không được bỏ trống',
                minlength: 'Mật khẩu xác nhận phải lớn hơn 6 ký tự',
                maxlength: 'Mật khẩu xác nhận phải nhỏ hơn 100 ký tự',
                equalTo: 'Mật khẩu xác nhận không đúng',
            },
            txtUser_Fullname: {
                required: 'Họ và tên không được bỏ trống',
                maxlength: 'Họ và tên phải nhỏ hơn 50 ký tự',
            },
            txtUser_Address: {
                maxlength: 'Địa chỉ liên hệ phải nhỏ hơn 100 ký tự',
            },
            txtUser_Email: {
                required: 'Địa chỉ email không được bỏ trống',
                maxlength: 'Địa chỉ email phải nhỏ hoen 50 ký tự',
                email: 'Địa chỉ email không đúng định dạng',
                remote: 'Địa chỉ email đã tồn tại',
            },
            txtUser_Phone: {
                required: 'Số điện thoại không được bỏ trống',
                minlength: 'Số điện thoại phải lớn hơn 10 ký tự',
                maxlength: 'Số điện thoại phải nhỏ hơn 20 ký tự',
                telphone: 'Số điện thoại không đúng định dạng',
            },
            txtUser_Lockreason: {
                required: 'Vui lòng nhập lý do khóa',
                maxlength: 'Lý do khóa phải nhỏ hơn 100 ký tự',
            },
            txtUser_RoleGroup: {
                required: 'Nhóm quyền không được bỏ trống',
            }
        },
        ignore: ':hidden:not(select)',
        errorPlacement: function (error, element) {
            if (element.attr('name') === 'txtUser_RoleGroup')
                error.insertAfter($('#slUser_RoleGroup_chosen'));
            else
                error.insertAfter(element);
        },
        submitHandler: function () {
            let gender = $('#slUser_Gender').val();
            let data = {
                username: $.trim($('#inpUser_Username').val()),
                password: $.trim($('#inpUser_Password').val()),
                fullname: $.trim($('#inpUser_Fullname').val()),
                birthday: $.trim($('#inpUser_Birthday').val()),
                email: $.trim($('#inpUser_Email').val()),
                phone: $.trim($('#inpUser_Phone').val()),
                gender: parseInt(typeof gender === undefined ? '0' : gender),
                address: $.trim($('#inpUser_Address').val()),
                islock: $('#inpUser_Islock').is(':checked'),
                lockreason: $.trim($('#inpUser_Lockreason').val()),
                lstRole: $('#slUser_RoleGroup').val().toString()
            };
            saveDataUser(data);
        }
    });

    /* Sửa người dùng */
    $(document).on('click', '#btnEditUser', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/user/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#formUser').validate().resetForm();
                    $('#formUser :input').not('#btnClose, #inpUser_Username, #inpUser_Email').prop('disabled', false).trigger("chosen:updated");
                    $('#inpUser_Username, #inpUser_Email').prop("disabled", true).trigger("chosen:updated");
                    let btnSubmitUser = $('#btnSubmitUser');
                    btnSubmitUser.addClass('btnUserEdit');
                    btnSubmitUser.removeClass('btnUserAdd');
                    btnSubmitUser.attr('data-id', data.id);
                    btnSubmitUser.html('<i class="fa fa-edit"></i> Cập nhật');
                    $('#userModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin người dùng');
                    $('#lock, #btnSubmitUser, .btnBirthday').show();
                    $('#password').hide();
                    if (data.islock)
                        $('#lock,#lockRegion').show();
                    else
                        $('#lockRegion').hide();
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy được thông tin của người dùng', 'error');
            }
        })
    });

    /* Xem chi tiết */
    $(document).on('click', '#btnViewUser', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/user/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#userModal .modal-title').html('<i class="fa fa-search"></i> Xem thông tin người dùng');
                    let formUser = $('#formUser');
                    formUser[0].reset();
                    formUser.validate().resetForm();
                    $('#formUser :input, .btnBirthday').not('#btnClose').prop('disabled', true).trigger("chosen:updated");
                    $('#password, #btnSubmitUser').hide();
                    if (data.islock)
                        $('#lock,#lockRegion').show();
                    else
                        $('#lock,#lockRegion').hide();
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy thông tin của người dùng', 'error');
            }
        });
    });

    /* Khóa người dùng */
    $(document).on('click', '#btnLockUser', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        lockOrUnlockOrReset('Bạn có muốn khóa người dùng?',
            '/api/admin/user/lockdata',
            {ids: data.toString()},
            'Khóa người người dùng thành công',
            'Khóa người dùng thất bại');
    });

    /* Mở khóa người dùng */
    $(document).on('click', '#btnUnlockUser', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        lockOrUnlockOrReset('Bạn có muốn mở khóa người dùng?',
            '/api/admin/user/unlockdata',
            {ids: data.toString()},
            'Mở khóa người dùng thành công',
            'Mở khóa người dùng thất bại');
    });

    /* Khóa hoặc mở khóa */
    $(document).on('click', '#btnLockOrUnlock', function () {
        let attr = $(this).attr('disabled');
        if (typeof attr === typeof undefined) {
            let selected = [];
            $('.checkItem:checked').each(function () {
                selected.push($(this).attr('data-id'));
            });
            if (selected.length === 0) hieuboy_cms.common().notification('Vui lòng chọn người dùng để Khóa/Mở khóa', 'error');
            else lockOrUnlockOrReset('Bạn có muốn Khóa/Mở khóa người dùng?',
                '/api/admin/user/lockorunlockdata',
                {ids: selected.toString()},
                'Khóa/Mở khóa người dùng thành công',
                'Khóa/Mở khóa người dùng thất bại');
        }
    });

    /* Reset mật khẩu người dùng */
    $(document).on('click', '#btnResetPassUser', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        lockOrUnlockOrReset('Bạn có muốn khôi phục mật khẩu?',
            '/api/admin/user/resetpassword',
            {ids: data.toString()},
            'Khôi phục mật khẩu thành công, mật khẩu mặc định là "vip.hieuboy"',
            'Khôi phục mật khẩu thất bại');
    });

    /* Chọn nhiều user để reset mật khẩu */
    $(document).on('click', '#btnResetPass', function () {
        let attr = $(this).attr('disabled');
        if (typeof attr === typeof undefined) {
            let selected = [];
            $('.checkItem:checked').each(function () {
                selected.push($(this).attr('data-id'));
            });
            if (selected.length === 0) hieuboy_cms.common().notification('Vui lòng chọn người dùng để khôi phục', 'error');
            else lockOrUnlockOrReset('Bạn có muốn khôi phục mật khẩu?',
                '/api/admin/user/resetpassword',
                {ids: selected.toString()},
                'Khôi phục mật khẩu thành công, mật khẩu mặc định là "vip.hieuboy"',
                'Khôi phục mật khẩu thất bại');
        }
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
                        username: $.trim($('#user_search_username').val()).toLowerCase(),
                        email: $.trim($('#user_search_email').val()).toLowerCase(),
                        phone: $.trim($('#user_search_phone').val()).toLowerCase(),
                        pageIndex: page,
                        pageSize: pageSize,
                        column: typeof column !== 'undefined' ? column : 'id',
                        desending: typeof desending !== 'undefined' ? desending : 1
                    };
                    hieuboy_cms.common().loadAllDataPage(link + '/api/admin/user/loaddata', 'data_table_user', data);
                }
            }
        });
    }

    /* Show bản ghi */
    $(document).on('change', '#show_page', function () {
        reloadDataUser();
    });

    /* Tìm kiếm user */
    $(document).on('click', '#btnUserSearch', function () {
        reloadDataUser();
    });

    /* Sắp xếp user */
    $(document).on('click', '.sort', function () {
        let _this = $(this);
        let col = _this.attr('data-col');
        hieuboy_cms.common().sortingList(_this, reloadDataUser, col);
    });

});

function setDataForm(data) {
    $.LoadingOverlay('hide');
    $('#inpUser_Username').val($.trim(data.username));
    $('#inpUser_Fullname').val($.trim(data.fullname));
    $('#inpUser_Birthday').val(data.birthday === null ? '' : moment(data.birthday).format("DD/MM/YYYY"));
    $('#inpUser_Email').val($.trim(data.email));
    $('#inpUser_Phone').val($.trim(data.phone));
    $('#slUser_Gender').val(data.gender == null ? '0' : data.gender).trigger('chosen:updated');
    $('#inpUser_Address').val($.trim(data.address));
    $('#slUser_RoleGroup').val(data.lstRole).trigger('chosen:updated');
    $('#inpUser_Islock').prop('checked', data.islock);
    $('#inpUser_Lockreason').val(data.lockreason);
    $('#userModal').modal('show');
}

function saveDataUser(data) {
    let btnSubmitUser = $('#btnSubmitUser');
    if (btnSubmitUser.hasClass('btnUserAdd')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/user/save',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới người dùng thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới người dùng thất bại', 'error');
            }
        }).done(function () {
            reloadDataUser();
            $('#userModal').modal('hide');
        });
    }
    if (btnSubmitUser.hasClass('btnUserEdit')) {
        data.id = parseInt(btnSubmitUser.attr('data-id'));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/user/update',
            data: data,
            success: function (resp) {
                if (resp.result) {
                    switch (resp.code) {
                        case 0:
                            hieuboy_cms.common().notification('Cập nhật người dùng thất bại', 'error');
                            break;
                        case 1:
                            reloadDataUser();
                            $('#userModal').modal('hide');
                            hieuboy_cms.common().notification('Cập nhật người dùng thành công', 'success');
                            break;
                        case 2:
                            hieuboy_cms.common().notification('Người tạo mới có quyền sửa', 'error');
                            break;
                        case 3:
                            hieuboy_cms.common().notification('Không lấy được dữ liệu của người dùng', 'error');
                            break;
                    }
                }
            }
        });
    }
}

function lockOrUnlockOrReset(text, api, data, success, error) {
    swal({
        title: 'Thông báo',
        text: text,
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
                type: 'PUT',
                url: link + api,
                data: data,
                success: function (resp) {
                    if (resp) hieuboy_cms.common().notification(success, 'success');
                    else hieuboy_cms.common().notification(error, 'error');
                },
            }).done(function () {
                reloadDataUser();
            });
        }
    });
}

function reloadDataUser(column, desending) {
    $('.checkItemAll').prop('checked', false);
    $('#btnLockOrUnlock, #btnResetPass').attr('disabled', 'disabled');
    let showPage = $('#show_page').val();
    if (typeof showPage !== 'undefined') pageSize = parseInt(showPage);
    let data = {
        username: $.trim($('#user_search_username').val()).toLowerCase(),
        email: $.trim($('#user_search_email').val()).toLowerCase(),
        phone: $.trim($('#user_search_phone').val()).toLowerCase(),
        pageIndex: 1,
        pageSize: pageSize,
        column: typeof column !== 'undefined' ? column : 'id',
        desending: typeof desending !== 'undefined' ? desending : 1
    };
    hieuboy_cms.common().loadDataAndPageAll(link + '/api/admin/user/loaddata', 'data_table_user', data);
}


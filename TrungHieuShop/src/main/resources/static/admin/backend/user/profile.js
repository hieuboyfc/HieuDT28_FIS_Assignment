$(document).ready(function () {

    /* Chọn hình ảnh */
    $(document).on('change', '#files', function () {
        let input = this;
        let url = $(this).val();
        let ext = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
        let files = $('#files');
        if (files[0].files[0] === undefined) {
            $('.avatar').attr('src', '/login/library/images/no-avatar.png');
            hieuboy_cms.common().notification('Vui lòng chọn ảnh đại diện', 'error');
        } else if (files[0].files[0] && !(ext === 'gif' || ext === 'png' || ext === 'jpeg' || ext === 'jpg'))
            hieuboy_cms.common().notification('Ảnh đại diện phải là định dạng gif, png, jpeg, jpg', 'error');
        else if (files[0].files[0].size > 5242880)
            hieuboy_cms.common().notification('Ảnh đại diện phải nhỏ hơn 5MB', 'error');
        else {
            let reader = new FileReader();
            reader.onload = function (resp) {
                $('.avatar').attr('src', resp.target.result);
            };
            reader.readAsDataURL(input.files[0]);
        }
    });

    /* Upload hình ảnh */
    $(document).on('click', '#btnUploadAvatar', function () {
        let files = $('#files');
        let ext = files.val().substring(files.val().lastIndexOf('.') + 1).toLowerCase();
        if (files[0].files[0] === undefined) {
            $('.avatar').attr('src', '/login/library/images/no-avatar.png');
            hieuboy_cms.common().notification('Vui lòng chọn ảnh đại diện', 'error');
        } else if (files[0].files[0].size > 5242880)
            hieuboy_cms.common().notification('Ảnh đại diện phải là định dạng gif, png, jpeg, jpg', 'error');
        else if (files[0].files[0] && !(ext === 'gif' || ext === 'png' || ext === 'jpeg' || ext === 'jpg'))
            hieuboy_cms.common().notification('Ảnh đại diện phải nhỏ hơn 5MB', 'error');
        else {
            $.LoadingOverlay('show');
            let data = new FormData();
            data.append('file', files[0].files[0]);
            $.ajax({
                type: 'POST',
                url: link + '/api/admin/user/profile/upload-file',
                data: data,
                contentType: false,
                processData: false,
                success: function (resp) {
                    if (resp) {
                        $('.avatar').attr('data-avatar', resp);
                        hieuboy_cms.common().notification("Upload ảnh đại diện thành công", 'success');
                    } else hieuboy_cms.common().notification('Upload ảnh đại diện thất bại', 'error');
                },
            }).done(function () {
                $.LoadingOverlay('hide');
            });
        }
    });

    /* Validate và thực hiện thay đổi thông tin cá nhân */
    $('#formProfile').validate({
        rules: {
            txtProfile_Fullname: {
                required: true,
                maxlength: 50,
            },
            txtProfile_Email: {
                required: true,
                email: true
            },
            txtProfile_Phone: {
                required: true,
                telphone: true
            },
            txtProfile_Address: {
                required: true
            },
        },
        messages: {
            txtProfile_Fullname: {
                required: 'Họ và tên không được để trống',
                maxlength: 'Họ và tên phải nhỏ hơn 50 ký tự',
            },
            txtProfile_Email: {
                required: 'Địa chỉ email không được để trống',
                email: 'Địa chỉ email không đúng định dạng',
            },
            txtProfile_Phone: {
                required: 'Số điện thoại không được để trống',
                telphone: 'Số điện thoại không đúng định dạng',
            },
            txtProfile_Address: {
                required: 'Địa chỉ liên hệ không được để trống',
            },
        },
        submitHandler: function () {
            let data = {
                fullname: $.trim($("#inpProfile_Fullname").val()),
                email: $.trim($("#inpProfile_Email").val()),
                phone: $.trim($("#inpProfile_Phone").val()),
                birthday: $('#inpProfile_Birthday').val(),
                address: $('#inpProfile_Address').val(),
                avatar: $('.avatar').attr('data-avatar'),
            };
            $.ajax({
                type: 'POST',
                url: link + '/api/admin/user/profile/change-info',
                data: data,
                success: function (resp) {
                    if (resp.result) {
                        hieuboy_cms.common().notification('Cập nhật thông tin tài khoản thành công.</br>Vui lòng đăng nhập lại để cập nhật dữ liệu mới nhất!', 'success');
                        setTimeout(function () {
                            location.reload();
                        }, 2000);
                    } else {
                        hieuboy_cms.common().notification('Cập nhật thông tin tài khoản thất bại', 'error');
                    }
                }
            });
        }
    });

    $('#formChangePassword').validate({
        rules: {
            txtProfile_OldPassword: {
                required: true,
                minlength: 6,
                maxlength: 100,
                remote: {
                    type: 'POST',
                    url: link + '/api/admin/user/checkpassword',
                    data: {
                        password: function () {
                            return $("#inpProfile_OldPassword").val();
                        }
                    }
                }
            },
            txtProfile_Password: {
                required: true,
                minlength: 6,
                maxlength: 100
            },
            txtProfile_RePassword: {
                required: true,
                minlength: 6,
                maxlength: 100,
                equalTo: '#inpProfile_Password',
            }
        },
        messages: {
            txtProfile_OldPassword: {
                required: 'Mât khẩu cũ không được để trống',
                minlength: 'Mật khẩu cũ phải lớn hơn 6 ký tự',
                maxlength: 'Mật khẩu cũ phải nhỏ hơn 100 ký tự',
                remote: 'Mật khẩu cũ không đúng',
            },
            txtProfile_Password: {
                required: 'Mật khẩu mới không được để trống',
                minlength: 'Mật khẩu mới phải lớn hơn 6 ký tự',
                maxlength: 'Mật khẩu mới phải nhỏ hơn 100 ký tự',
            },
            txtProfile_RePassword: {
                required: 'Xác nhận mật khẩu mới không được để trống',
                minlength: 'Xác nhận mật khẩu mới phải lớn hơn 6 ký tự',
                maxlength: 'Xác nhận mật khẩu mới phải nhỏ hơn 100 ký tự',
                equalTo: 'Xác nhận mật khẩu mới không trùng khớp'
            },
        },
        submitHandler: function () {
            let data = {
                password: $.trim($('#inpProfile_Password').val()),
            };
            $.ajax({
                type: 'POST',
                url: link + '/api/admin/user/profile/change-password',
                data: data,
                success: function (resp) {
                    if (resp.result) {
                        hieuboy_cms.common().notification('Thay đổi thông tin mật khẩu thành công.</br>Vui lòng đăng nhập lại để cập nhật dữ liệu mới nhất!', 'success');
                        setTimeout(function () {
                            location.reload();
                        }, 2000);
                    } else {
                        hieuboy_cms.common().notification('Thay đổi thông tin mật khẩu thất bại', 'error');
                    }
                }
            });
        }
    });

});
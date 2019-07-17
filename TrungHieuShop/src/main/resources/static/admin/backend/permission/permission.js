$(document).ready(function () {

    loadSelectPermission('slPermission_GroupID');
    loadSelectPermission('slGroupPermission_ParentID');

    $(document).on('click', '#btnAddGroup', function () {
        $('#groupPermissionModal').modal('show');
        $('#permissionModal').modal('hide');
    });

    /* Mở form thêm mới chức năng */
    $(document).on('click', '#btnAddPermission', function () {
        let formPermission = $('#formPermission');
        formPermission[0].reset();
        formPermission.validate().resetForm();
        $('#permissionModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin chức năng');
        let btnPermissionSubmit = $('#btnPermissionSubmit');
        btnPermissionSubmit.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        btnPermissionSubmit.removeClass('btnEditPermision');
        btnPermissionSubmit.addClass('btnAddPermision');
        $('#permissionModal').modal('show');
    });

    /* Validate và thực hiện thêm dữ liệu */
    $('#formPermission').validate({
        rules: {
            txtPermission_Name: {
                required: true,
                maxlength: 100,
            },
            txtPermission_Link: {
                required: true,
                maxlength: 100,
                remote: {
                    url: link + '/api/admin/permission/checklink',
                    type: 'POST',
                    data: {
                        link: function () {
                            return $('#inpPermission_Link').val();
                        }
                    }
                }
            },
            slPermission_GroupID: {
                valueNotEquals: '0',
            }
        },
        messages: {
            txtPermission_Name: {
                required: 'Tên chức năng không được để trống',
                maxlength: 'Tên chức năng phải nhỏ hơn 100 ký tự',
            },
            txtPermission_Link: {
                required: 'Đường dẫn truy cập không được bỏ trống',
                maxlength: 'Đường dẫn truy cập phải nhỏ hơn 100 ký tự',
                remote: 'Đường dẫn truy cập đã tồn tại',
            },
            slPermission_GroupID: {
                valueNotEquals: 'Vui lòng chọn nhóm chức năng',
            }
        },
        ignore: ':hidden:not(select)',
        errorPlacement: function (error, element) {
            if (element.attr('name') === 'slPermission_GroupID')
                error.insertAfter($('#slPermission_GroupID_chosen'));
            else
                error.insertAfter(element);
        },
        submitHandler: function () {
            let data = {
                name: $.trim($('#inpPermission_Name').val()),
                link: $.trim($('#inpPermission_Link').val()),
                groupID: $('#slPermission_GroupID').val(),
            };
            savePermission(data);
        }
    });

    /* Sửa chức năng */
    $(document).on('click', '#btnEdit', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/permission/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    let permissionLink = $('#permissionLink');
                    permissionLink.empty().append('<input type="text" class="form-control" id="inpPermission_Link" placeholder="VD: /api/admin/home" maxlength="100"/>');
                    $('#permissionModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin chức năng');
                    let btnPermissionSubmit = $('#btnPermissionSubmit');
                    btnPermissionSubmit.html('<i class="fa fa-edit"></i> Cập nhật');
                    btnPermissionSubmit.attr('data-id', data.id);
                    btnPermissionSubmit.removeClass('btnAddPermision');
                    btnPermissionSubmit.addClass('btnEditPermision');
                    $('#formPermission').validate().resetForm();
                    setDataFormPermission(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu chức năng', 'error');
            }
        });
    });

    /* Xóa chức năng */
    $(document).on('click', '#btnDelete', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        swal({
            title: 'Thông báo',
            text: 'Bạn có muốn xóa chức năng?',
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
                    url: link + '/api/admin/permission/delete/' + id,
                    success: function (resp) {
                        if (resp) hieuboy_cms.common().notification('Xóa chức năng thành công', 'success');
                        else hieuboy_cms.common().notification('Xóa chức năng thất bại', 'error');
                    },
                }).done(function () {
                    setTimeout(function () {
                        location.reload();
                    }, 500);
                });
            }
        });
    });

    /* Khóa chức năng */
    $(document).on('click', '#btnLock', function () {
        lockOrUnlock('btnLock', 'Bạn có muốn khóa chức năng', 'Khóa chức năng thành công', 'Khóa chức năng thất bại');
    });

    /* Mở khóa chức năng */
    $(document).on('click', '#btnUnlock', function () {
        lockOrUnlock('btnUnlock', 'Bạn có muốn mở khóa chức năng', 'Mở khóa chức năng thành công', 'Mở khóa chức năng thất bại');
    });

});

function setDataFormPermission(data) {
    $.LoadingOverlay('hide');
    $.trim($('#inpPermission_Name').val(data.name));
    $.trim($('#inpPermission_Link').val(data.link));
    $('#slPermission_GroupID').val(data.groupID).trigger('chosen:updated');
    loadSelectGroupPermission('slPermission_GroupID');
    $('#permissionModal').modal('show');
}

function savePermission(data) {
    let btnPermissionSubmit = $('#btnPermissionSubmit');
    if (btnPermissionSubmit.hasClass('btnAddPermision')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/permission/save',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới chức năng thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới chức năng thất bại', 'error');
            }
        }).done(function () {
            setTimeout(function () {
                location.reload();
            }, 500);
            $('#permissionModal').modal('hide');
        });
    }
    if (btnPermissionSubmit.hasClass('btnEditPermision')) {
        data.id = parseInt(btnPermissionSubmit.attr('data-id'));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/permission/update',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Cập nhật chức năng thành công', 'success');
                else hieuboy_cms.common().notification('Cập nhật chức năng thất bại', 'error');
            }
        }).done(function () {
            setTimeout(function () {
                location.reload();
            }, 500);
            $('#permissionModal').modal('hide');
        });
    }
}

function lockOrUnlock(lockOrUnlock, text, success, error) {
    let _this = $('#' + lockOrUnlock);
    let id = _this.attr('data-id');
    let data = {id: id};
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
                url: link + '/api/admin/permission/lockorunlockdata',
                data: data,
                success: function (resp) {
                    if (resp) hieuboy_cms.common().notification(success, 'success');
                    else hieuboy_cms.common().notification(error, 'error');
                },
            }).done(function () {
                setTimeout(function () {
                    location.reload();
                }, 500);
            });
        }
    });
}

function loadSelectPermission(tagAppend) {
    $.get(link + '/api/admin/permission/group/loadselect', function (resp) {
        let append = $('#' + tagAppend);
        append.empty();
        append.html(resp);
        append.trigger('chosen:updated');
    });
}
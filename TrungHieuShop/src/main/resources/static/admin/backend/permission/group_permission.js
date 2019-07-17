$(document).ready(function () {

    loadSelectGroupPermission('slGroupPermission_ParentID');

    /* Đóng/Mở danh sách chức năng */
    $('#permissionTree').nestable({
        maxDepth: 0,
        noDragClass: 'dd-nodrag',
        handleClass: 'handleClass'
    }).nestable('collapseAll');

    /* Mở form thêm mới nhóm chức năng */
    $(document).on('click', '#btnAddGroupPermission', function () {
        let formGroupPermission = $('#formGroupPermission');
        formGroupPermission[0].reset();
        formGroupPermission.validate().resetForm();
        let btnGroupPermissionSubmit = $('#btnGroupPermissionSubmit');
        btnGroupPermissionSubmit.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        btnGroupPermissionSubmit.removeClass('btnEditGroupPermission');
        btnGroupPermissionSubmit.addClass('btnAddGroupPermission');
        $('#slGroupPermission_ParentID').val(0).trigger('chosen:updated');
        $('#groupPermissionModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin nhóm chức năng');
        $('#groupPermissionModal').modal('show');
    });

    /* Validate và thực hiện thêm dữ liệu */
    $('#formGroupPermission').validate({
        rules: {
            txtGroupPermission_Name: {
                required: true,
                maxlength: 100,
            }
        },
        messages: {
            txtGroupPermission_Name: {
                required: 'Tên nhóm chức năng không được để trống',
                maxlength: 'Tên nhóm chức năng phải nhỏ hơn 100 ký tự',
            }
        },
        submitHandler: function () {
            let data = {
                name: $.trim($('#inpGroupPermission_Name').val()),
                parentID: $('#slGroupPermission_ParentID').val(),
            };
            saveDataGroupPermission(data);
        }
    });

    /* Lấy dữ liệu và sửa nhóm chức năng */
    $(document).on('click', '#btnEditGroup', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/permission/group/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#groupPermissionModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin nhóm chức năng');
                    let btnGroupPermissionSubmit = $('#btnGroupPermissionSubmit');
                    btnGroupPermissionSubmit.html('<i class="fa fa-edit"></i> Cập nhật');
                    btnGroupPermissionSubmit.attr('data-id', data.id);
                    btnGroupPermissionSubmit.removeClass('btnAddGroupPermission');
                    btnGroupPermissionSubmit.addClass('btnEditGroupPermission');
                    $('#formGroupPermission').validate().resetForm();
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy thông tin nhóm chức năng', 'error');
            }
        });
    });

    /* Xóa dữ liệu nhóm chức năng */
    $(document).on('click', '#btnDeleteGroup', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        swal({
            title: 'Thông báo',
            text: 'Bạn có muốn xóa thông tin nhóm chức năng?',
            icon: 'warning',
            dangerMode: true,
            buttons: {
                cancel: {
                    text: 'Trở về',
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
                    url: link + '/api/admin/permission/group/delete/' + id,
                    success: function (resp) {
                        if (resp) hieuboy_cms.common().notification('Xóa nhóm chức năng thành công', 'success');
                        else hieuboy_cms.common().notification('Xóa nhóm chức năng thất bại', 'error');
                    },
                }).done(function () {
                    setTimeout(function () {
                        location.reload();
                    }, 500);
                });
            }
        });
    });

});

function setDataForm(data) {
    $.LoadingOverlay('hide');
    $.trim($('#inpGroupPermission_Name').val(data.name));
    $('#slGroupPermission_ParentID').val(data.parentID).trigger('chosen:updated');
    $('#groupPermissionModal').modal('show');
}

function saveDataGroupPermission(data) {
    let btnGroupPermissionSubmit = $('#btnGroupPermissionSubmit');
    if (btnGroupPermissionSubmit.hasClass('btnAddGroupPermission')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/permission/group/save',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới nhóm chức năng thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới nhóm chức năng thất bại', 'error');
            }
        }).done(function () {
            setTimeout(function () {
                location.reload();
            }, 500);
            $('#groupPermissionModal').modal('hide');
        });
    }
    if (btnGroupPermissionSubmit.hasClass('btnEditGroupPermission')) {
        data.id = parseInt(btnGroupPermissionSubmit.attr('data-id'));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/permission/group/update',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Cập nhật nhóm chức năng thành công', 'success');
                else hieuboy_cms.common().notification('Cập nhật nhóm chức năng thất bại', 'error');
            }
        }).done(function () {
            setTimeout(function () {
                location.reload();
            }, 500);
            $('#groupPermissionModal').modal('hide');
        });
    }
}

function loadSelectGroupPermission(tagAppend) {
    $.get(link + '/api/admin/permission/group/loadselect', function (resp) {
        let append = $('#' + tagAppend);
        append.empty();
        append.html(resp);
    });
}
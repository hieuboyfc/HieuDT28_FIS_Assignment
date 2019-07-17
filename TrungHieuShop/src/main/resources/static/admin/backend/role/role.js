$(document).ready(function () {

    /* Mở form thêm mới người dùng */
    $(document).on('click', '#btnAddNew', function () {
        // Load Tree
        $.get(link + '/api/admin/role/loadtree', function (resp) {
            loadTree(resp);
        });
        let btnSubmitRole = $('#btnSubmitRole');
        btnSubmitRole.removeClass('btnRoleEdit');
        btnSubmitRole.addClass('btnRoleAdd');
        btnSubmitRole.show();
        btnSubmitRole.html('<i class="fa fa-check-square-o"></i> Thêm mới');
        $('#roleModal .modal-title').html('<i class="fa fa-check-square-o"></i> Thêm mới thông tin vai trò');
        let formRole = $('#formRole');
        formRole[0].reset();
        formRole.validate().resetForm();
        $('#roleModal').modal('show');
    });

    /* Tự động sửa dữ liệu khi click chọn chức năng */
    $(document).on('click', '.tree-item', function () {
        let roleId = $('#formRole').attr('data-id');
        let permissionId = $(this).attr('data-id');
        let check = false;
        if ($(this).hasClass('tree-selected')) {
            check = true;
        }
        $.post(link + '/api/admin/role/update', {roleId: roleId, permissionId: permissionId, value: check});
    });

    /* Change input name role*/
    $(document).on('change', '#inpRole_Rolename', function () {
        let btnSubmitRole = $('#btnSubmitRole');
        if (btnSubmitRole.hasClass('btnRoleEdit')) {
            if ($(this).val() === '')
                btnSubmitRole.hide();
            else btnSubmitRole.show();
        }
    });

    /* Thực hiện thêm dữ liệu */
    $('#formRole').validate({
        rules: {
            txtRole_Rolename: {
                required: true,
                maxlength: 50,
            }
        },
        messages: {
            txtRole_Rolename: {
                required: 'Tên vai trò không được bỏ trống',
                maxlength: 'Tên vai trò phải nhỏ hơn 50 ký tự',
            }
        },
        submitHandler: function () {
            let btnSubmitRole = $('#btnSubmitRole');
            let lstPermission = [];
            if (btnSubmitRole.hasClass('btnRoleAdd')) {
                $('.tree-selected').each(function () {
                    if (typeof $(this).attr('data-id') !== 'undefined') {
                        lstPermission.push(parseFloat($(this).attr('data-id')));
                    }
                });
                if (lstPermission.length <= 0) {
                    hieuboy_cms.common().notification('Vui lòng chọn chức năng', 'error');
                    return;
                }
            }
            let data = {
                name: $.trim($('#inpRole_Rolename').val()),
                lstPermission: lstPermission.toString()
            };
            saveDataRole(data);
        }
    });

    /* Sửa dữ liệu */
    $(document).on('click', '#btnEditRole', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: link + '/api/admin/role/getdetail/' + id,
            success: function (resp) {
                if (resp.result) {
                    let data = resp.resultData;
                    $('#roleModal .modal-title').html('<i class="fa fa-edit"></i> Cập nhật thông tin vai trò');
                    let btnSubmitRole = $('#btnSubmitRole');
                    btnSubmitRole.html('<i class="fa fa-edit"></i> Cập nhật');
                    btnSubmitRole.attr('data-id', data.id);
                    btnSubmitRole.removeClass('btnRoleAdd');
                    btnSubmitRole.addClass('btnRoleEdit');
                    let formRole = $('#formRole');
                    formRole.validate().resetForm();
                    formRole.attr('data-id', id);
                    //Chọn danh sách các permission trong tree
                    $('.tree-permission').removeClass('tree-selected');
                    $('.tree-permission .icon-item').removeClass('fa-check').addClass('fa-times');
                    //Reload tree
                    let treeData = JSON.parse(resp.message);
                    loadTree(treeData);
                    setDataForm(data);
                } else hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu vai trò', 'error');
            }
        })
    });

    /* Xóa vai trò */
    $(document).on('click', '#btnDeleteRole', function () {
        let _this = $(this);
        let id = _this.attr('data-id');
        let data = [id];
        deleteRole(data.toString());
    });

    /* Xóa nhiều vai trò */
    $(document).on('click', '#btnDelete', function () {
        let attr = $(this).attr('disabled');
        if (typeof attr === typeof undefined) {
            let selected = [];
            $('.checkItem:checked').each(function () {
                selected.push($(this).attr('data-id'));
            });
            if (selected.length === 0) hieuboy_cms.common().notification('Vui lòng chọn vai trò để xóa', 'error');
            else deleteRole(selected.toString());
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
                        name: $.trim($('#role_search_rolename').val()).toLowerCase(),
                        pageIndex: page,
                        pageSize: pageSize,
                        column: typeof column !== 'undefined' ? column : 'id',
                        desending: typeof desending !== 'undefined' ? desending : 1
                    };
                    hieuboy_cms.common().loadAllDataPage(link + '/api/admin/role/loaddata', 'data_table_role', data);
                }
            }
        });
    }

    /* Show bản ghi */
    $(document).on('change', '#show_page', function () {
        reloadDataRole();
    });

    /* Tìm kiếm user */
    $(document).on('click', '#btnRoleSearch', function () {
        reloadDataRole();
    });

    /* Sắp xếp user */
    $(document).on('click', '.sort', function () {
        let _this = $(this);
        let col = _this.attr('data-col');
        hieuboy_cms.common().sortingList(_this, reloadDataRole, col);
    });

});

function setDataForm(data) {
    $.LoadingOverlay('hide');
    $('#btnSubmitRole').hide();
    $('#inpRole_Rolename').val(data.name);
    $('#roleModal').modal('show');
}

function saveDataRole(data) {
    let btnSubmitRole = $('#btnSubmitRole');
    if (btnSubmitRole.hasClass('btnRoleAdd')) {
        $.ajax({
            type: 'POST',
            url: link + '/api/admin/role/save',
            data: data,
            success: function (resp) {
                if (resp.result) hieuboy_cms.common().notification('Thêm mới vai trò thành công', 'success');
                else hieuboy_cms.common().notification('Thêm mới vai trò dùng thất bại', 'error');
            }
        }).done(function () {
            reloadDataRole();
            $('#roleModal').modal('hide');
        });
    }
    if (btnSubmitRole.hasClass('btnRoleEdit')) {
        data.id = parseInt(btnSubmitRole.attr('data-id'));
        $.ajax({
            type: 'PUT',
            url: link + '/api/admin/role/edit',
            data: data,
            success: function (resp) {
                if (resp.result) {
                    switch (resp.code) {
                        case 0:
                            hieuboy_cms.common().notification('Cập nhật vai trò thất bại', 'error');
                            break;
                        case 1:
                            reloadDataRole();
                            $('#roleModal').modal('hide');
                            hieuboy_cms.common().notification('Cập nhật vai trò thành công', 'success');
                            break;
                        case 2:
                            hieuboy_cms.common().notification('Có lỗi khi lấy dữ liệu vai trò', 'error');
                            break;
                    }
                }
            }
        })
    }
}

function deleteRole(data) {
    swal({
        title: 'Thông báo',
        text: 'Bạn có muốn xóa vai trò?',
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
                url: link + '/api/admin/role/delete/' + data,
                success: function (resp) {
                    if (resp) hieuboy_cms.common().notification('Xóa vai trò thành công', 'success');
                    else hieuboy_cms.common().notification('Xóa vai trò thất bại', 'error');
                },
            }).done(function () {
                reloadDataRole();
            });
        }
    });
}

function reloadDataRole(column, desending) {
    $('.checkItemAll').prop('checked', false);
    $('#btnDelete').attr('disabled', 'disabled');
    let showPage = $('#show_page').val();
    if (typeof showPage !== 'undefined') pageSize = parseInt(showPage);
    let data = {
        name: $.trim($('#role_search_rolename').val()).toLowerCase(),
        pageIndex: 1,
        pageSize: pageSize,
        column: typeof column !== 'undefined' ? column : 'id',
        desending: typeof desending !== 'undefined' ? desending : 1
    };
    hieuboy_cms.common().loadDataAndPageAll(link + '/api/admin/role/loaddata', 'data_table_role', data);
}

function loadTree(dataTree) {
    $('.role-tree').empty();
    let html = '<ul id="roleTree" class="mCustomScrollbar" style="height: 400px; padding: 10px;"></ul>';
    $('.role-tree').html(html);
    let treeData = function (options, callback) {
        let $data = null;
        if (!("text" in options) && !("type" in options)) {
            $data = dataTree;//the root tree
            callback({data: $data});
            return;
        } else if ("type" in options && options.type === 'folder') {
            if ('additionalParameters' in options && 'children' in options.additionalParameters)
                $data = options.additionalParameters.children || {};
            else $data = {}//no data
        }
        if ($data != null)
            callback({data: $data});
    };
    $('#roleTree').ace_tree({
        dataSource: treeData,
        multiSelect: true,
        cacheItems: true,
        'open-icon': 'ace-icon tree-minus',
        'close-icon': 'ace-icon tree-plus',
        'itemSelect': true,
        'folderSelect': false,
        'selected-icon': 'ace-icon fa fa-check',
        'unselected-icon': 'ace-icon fa fa-times',
        loadingHTML: '<div class="tree-loading"><i class="ace-icon fa fa-refresh fa-spin blue"></i></div>'
    });
}
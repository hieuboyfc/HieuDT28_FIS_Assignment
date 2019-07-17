$(document).ready(function () {

    /* User */
    $('#slUser_RoleGroup, #slUser_Gender').chosen({
        disable_search: false,
        width: '100%',
        placeholder_text_multiple: 'Vui lòng chọn dữ liệu',
        include_group_label_in_selected: true,
        search_contains: true,
        enable_split_word_search: true,
        no_results_text: 'Không tìm thấy kết quả'
    });

    /* Group - Permission */
    $('#slGroupPermission_ParentID, #slPermission_GroupID').chosen({
        disable_search: false,
        width: '100%',
        include_group_label_in_selected: true,
        search_contains: true,
        enable_split_word_search: true,
        no_results_text: 'Không tìm thấy kết quả'
    });

    $('.chosen-select').chosen({
        disable_search: false,
        width: '100%',
        include_group_label_in_selected: true,
        search_contains: true,
        enable_split_word_search: true,
        no_results_text: 'Không tìm thấy kết quả'
    });

});

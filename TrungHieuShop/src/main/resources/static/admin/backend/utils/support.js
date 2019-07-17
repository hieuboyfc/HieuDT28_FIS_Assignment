/*
* Create 10/07/2019
* Hieu Boy
*/
$(document).ready(function () {

    /* Click vào checkbox item */
    $(document).on('click', '.checkItem', function () {
        let _this = $(this);
        let checkAll = true;
        let checkOne = false;
        let count_checked = 0;
        var checkItem = $('.checkItem');
        checkItem.each(function () {
            if (!$(this).prop('checked')) checkAll = false;
        });
        checkItem.each(function () {
            if ($(this).prop('checked')) {
                count_checked++;
                checkOne = true;
            } else $(this).removeClass('first-checked');
        });
        if (count_checked === 1 && _this.prop('checked')) _this.addClass('first-checked');
        else {
            if (count_checked >= 1) {
                let first = 1;
                checkItem.each(function () {
                    if ($(this).prop('checked')) {
                        if (first === 1) $(this).addClass('first-checked');
                        first++;
                    }
                });
            }
            _this.removeClass('first-checked');
        }
        if (checkOne) {
            if (count_checked > 1 && count_checked <= 3) $('#btnMerge').removeAttrs('disabled');
            else $('#btnMerge').attr('disabled', 'disabled');
            $('#btnResetPass, #btnLockOrUnlock, #btnDelete').removeAttrs('disabled');
        } else $('#btnResetPass, #btnLockOrUnlock, #btnDelete').attr('disabled', 'disabled');
        $('.checkItemAll').prop('checked', checkAll);
    });

    /* Click vào checkbox item */
    $(document).on('click', '.checkItemAll', function () {
        $('.checkItemAll').each(function () {
            if ($('.checkItemAll').prop('checked')) {
                $('.checkItem').prop('checked', true);
                $('#btnResetPass, #btnLockOrUnlock, #btnDelete').removeAttrs('disabled');
            } else {
                $('.checkItem').prop('checked', false);
                $('#btnResetPass, #btnLockOrUnlock, #btnDelete, #btnMerge').attr('disabled', 'disabled');
            }
        });
    });

    //add number 0 to time or date
    Number.prototype.padLeft = function (base, chr) {
        let len = (String(base || 10).length - String(this).length) + 1;
        return len > 0 ? new Array(len).join(chr || '0') + this : this;
    };

    $(document).ajaxError(function () {
        $.LoadingOverlay('hide');
        hieuboy_cms.common().notification('Hệ thống đang bị lỗi vui lòng liên hệ với admin để khắc phục sự cố này !', 'info');
        /*setTimeout(location.reload(), 2000);*/
    });
});
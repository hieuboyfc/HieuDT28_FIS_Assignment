/*
* Create 10/07/2019
* Hiếu Boy
*/
$(document).ready(function () {

    /*$.validator.setDefaults({
        ignore: ":hidden:not(.chosen-select)"
    });*/

    $.validator.addMethod('filesize', function (value, element, param) {
        return this.optional(element) || ((element.files[0].size / (1204 * 1024)) <= param)
    }, 'Dung lượng ảnh không vượt quá {0}');

    $.validator.addMethod("valueNotEquals", function (value, element, param) {
        return (this.optional(element) || value != param) && value !== '';
    }, "Giá trị không hợp lệ");

    $.validator.addMethod('telphone', function (value, element) {
        return this.optional(element) || /^(\(?0[1-9]{1}[0-9]{1}\)?|(\(?\+84\)?))([ .-]?)([0-9]{7,20})$/.test(value);
    }, 'Số điện thoại không đúng định dạng');

    $.validator.addMethod('faxphone', function (value, element) {
        return this.optional(element) || /^(\(?0[1-9]{1}[0-9]{1})\)?([ .-]?)([0-9]){7,20}$/.test(value);
    }, 'Số điện thoại cố định không đúng định dạng');

    $.validator.addMethod('formatSGT', function (value, element) {
        if ($('#slTypePapers').val() !== undefined) {
            if (parseInt($('#slTypePapers').val()) === 1) {
                return this.optional(element) || (value.length === 9 && /[0-9]{9}$/.test(value));
            }
            return this.optional(element) || (value.length === 12 && /[0-9]{12}$/.test(value));
        }
        if ($('#slPersonal_accnt_doctype').val() !== undefined) {
            if (parseInt($('#slPersonal_accnt_doctype').val()) === 1) {
                return this.optional(element) || (value.length === 9 && /[0-9]{9}$/.test(value));
            }
            return this.optional(element) || (value.length === 12 && /[0-9]{12}$/.test(value));
        }
    }, 'Số giấy tờ không đúng định dạng');

    $.validator.addMethod('mst', function (value, element) {
        return this.optional(element) || ((/([0-9]{13})$/.test(value)) || (/([0-9]{9}-[0-9]{3})$/.test(value)));
    }, 'Mã số thuế không đúng định dạng');

    $.validator.addMethod('sgpdkkd', function (value, element) {
        return this.optional(element) || (/([0-9])$/.test(value));
    }, 'Số GP-ĐKKD không đúng định dạng');

    $.validator.addMethod('checkDate', function (value, element) {
        if ($('#inpBirthday').val() !== undefined && $('#inpDateCreate').val() !== undefined) {
            let dob = $("#inpBirthday").datepicker("getDate");
            let cmt = $("#inpDateCreate").datepicker("getDate");
            return this.optional(element) || typeof cmt === null || typeof dob === null || (cmt - dob > 0);
        }
    }, 'Ngày cấp số giấy tờ không được nhỏ hơn và bằng ngày sinh');

});

function getValueMerge(elementCheck, elementValue) {
    let result = '';
    $(elementCheck).each(function () {
        if ($(this).prop('checked')) {
            result = $(this).parents('li').find('.' + elementValue).val();
        }
    });
    return result;
}

let validator = (function () {
    let validate = function () {
    };
    validate.prototype.validateFaxPhone = function (value) {
        if (value !== '')
            return /^(\(?0[1-9]{1}[0-9]{1})\)?([ .-]?)([0-9]){7,20}$/.test(value);
        return true;
    }
    validate.prototype.validateCMT = function (value, type) {
        if (type === '1') {
            if (value !== '')
                return (/^\w[0-9]{8}$/.test(value));
        } else {
            if (value !== '')
                return (/^\w[0-9]{11}$/.test(value));
        }
        return true;
    }
    validate.prototype.validateMST = function (value) {
        if (value !== '')
            return ((/([0-9]{13})$/.test(value)) || (/([0-9]{9}-[0-9]{3})$/.test(value)));
        return true;
    }
    validate.prototype.validateGPKD = function (value) {
        if (value !== '')
            return (/([0-9]{9,30})$/.test(value));
        return true;
    }
    validate.prototype.validateEmail = function (value) {
        if (value !== '')
            return (value.length <= 50 && /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(value));
        return true;
    }
    validate.prototype.checkDate = function (cmt, dob) {
        var dob = $('.txtmgpotential_dbo').datepicker("getDate");
        var cmt = $('.txtmgpotential_docdate').datepicker("getDate");
        if (cmt === null || dob === null || (cmt - dob > 0))
            return true;
        return false;
    }
    return {
        check: function () {
            let check = new validate();
            return check;
        }
    };
})();

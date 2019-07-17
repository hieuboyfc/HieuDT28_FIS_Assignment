/*
* Create 10/07/2019
* Hieu Boy
*/
let link = '';
let pageSize = 2;
let hieuboy_cms = (function () {
    let hieuboy_cms_js = function () {
    };

    /* Phân trang */
    hieuboy_cms_js.prototype.loadAllDataPage = function (linkApi, tagAppend, data) {
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: linkApi,
            data: data,
            success: function (resp) {
                if (resp) {
                    let pageHome = $('.page_home');
                    $('#' + tagAppend + ' tbody').empty().html(resp);
                    if (typeof pageHome.val() !== 'undefined')
                        pageHome.show();
                    else {
                        let html = hieuboy_cms.common().listPage();
                        $('.page_option').append(html);
                    }
                    $.LoadingOverlay('hide');
                }
            }
        });
    }

    /* Load dữ liệu và phân trang */
    hieuboy_cms_js.prototype.loadDataAndPageAll = function (linkApi, tagAppend, data) {
        $.LoadingOverlay('show');
        $.ajax({
            type: 'GET',
            url: linkApi,
            data: data,
            success: function (resp) {
                if (resp) {
                    $('#' + tagAppend + ' tbody').empty().html(resp);
                    let totalpage = parseInt($('#' + tagAppend + ' tbody').find('#data_paging').attr('data-total'));
                    let pageHome = $('.page_home');
                    if (totalpage > 1) {
                        if (typeof pageHome.val() !== 'undefined')
                            pageHome.show();
                        else {
                            let html = hieuboy_cms.common().listPage();
                            $('.page_option').append(html);
                        }
                        hieuboy_cms.common().showPaginationAll(totalpage, linkApi, tagAppend, data);
                    } else
                        pageHome.hide();
                    $.LoadingOverlay('hide')
                }
            }
        });
    }

    /* Hiển thị tất cả các trang */
    hieuboy_cms_js.prototype.showPaginationAll = function (pageTotal, linkApi, tagAppend, data) {
        $('.pagination').remove();
        $('.dataTables_paginate').html('<div class="pagination"></div>');
        $('.pagination').twbsPagination({
            totalPages: pageTotal,
            visiblePages: 5,
            first: '|<<<',
            prev: '|<',
            next: '>|',
            last: '>>>|',
            onPageClick: function (event, page) {
                let curentPage = parseInt($('#data_paging').attr('data-page'));
                if (curentPage !== page) {
                    data.pageIndex = page;
                    $.LoadingOverlay('show');
                    $.ajax({
                        type: 'GET',
                        url: linkApi,
                        data: data,
                        success: function (resp) {
                            if (resp) {
                                $('#' + tagAppend + ' tbody').empty().html(resp);
                                let pageHome = $('.page_home');
                                if (typeof pageHome.val() !== 'undefined')
                                    pageHome.show();
                                else {
                                    let html = hieuboy_cms.common().listPage();
                                    $('.page_option').append(html);
                                }
                                $.LoadingOverlay('hide');
                            }
                        }
                    });
                }
            }
        });
    }

    /* Danh sách phân trang */
    hieuboy_cms_js.prototype.listPage = function () {
        return '<div class="page_home"><div class="col-xs-12 col-md-4 form-inline"><label>Hiển thị <select class="form-control" id="show_page"><option value="2">2</option><option value="20">20</option><option value="50">50</option><option value="100">100</option><option value="150">150</option><option value="200">200</option></select> Bản ghi</label></div><div class="col-xs-12 col-md-8"><div class="dataTables_paginate"><div class="pagination"></div></div></div></div>';
    }

    /* Sắp xếp */
    hieuboy_cms_js.prototype.sortingList = function (_class, fn_reloadData, col) {
        let _this = _class;
        var col = _this.attr('data-col');
        if (_this.hasClass('sorting')) {
            fn_reloadData(col, 0);
            _this.removeClass('sorting');
            $('.sort').removeClass('sorting_desc');
            $('.sort').removeClass('sorting_asc');
            $('.sort').not(_this).addClass('sorting');
            _this.addClass('sorting_asc');
        } else if (_this.hasClass('sorting_asc')) {
            fn_reloadData(col, 1);
            _this.removeClass('sorting');
            $('.sort').removeClass('sorting_asc');
            $('.sort').removeClass('sorting_desc');
            $('.sort').not(_this).addClass('sorting');
            _this.addClass('sorting_desc');
        } else if (_this.hasClass('sorting_desc')) {
            fn_reloadData(col, 0);
            _this.removeClass('sorting');
            $('.sort').removeClass('sorting_desc');
            $('.sort').removeClass('sorting_asc');
            $('.sort').not(_this).addClass('sorting');
            _this.addClass('sorting_asc');
        }
    }

    /* Thông báo */
    hieuboy_cms_js.prototype.notification = function (text, style) {
        let icon = 'fa fa-adjust';
        if (style === 'error') icon = 'fa fa-exclamation';
        else if (style === 'warning') icon = 'fa fa-warning';
        else if (style === 'success') icon = 'fa fa-check';
        else if (style === 'info') icon = 'fa fa-question';
        else icon = 'fa fa-adjust';
        $.notify({
            title: 'Thông báo',
            text: text,
            image: "<i class='" + icon + "'></i>"
        }, {
            style: 'metro',
            className: style,
            globalPosition: 'top right',
            showAnimation: 'show',
            showDuration: 0,
            hideDuration: 0,
            autoHideDelay: 5000,
            autoHide: true,
            clickToHide: true
        });
    }

    return {
        common: function () {
            let common = new hieuboy_cms_js();
            return common;
        }
    };

})();
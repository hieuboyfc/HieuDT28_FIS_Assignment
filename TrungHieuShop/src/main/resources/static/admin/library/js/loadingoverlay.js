/***************************************************************************************************
LoadingOverlay - A flexible loading overlay jQuery plugin
    Author          : Gaspare Sganga
    Version         : 1.5.3
    License         : MIT
    Documentation   : http://gasparesganga.com/labs/jquery-loading-overlay/
****************************************************************************************************/
(function($, undefined){
    // Default Settings
    var _defaults = {
        color           : "rgba(255, 255, 255, 0.5)",
        custom          : "",
        fade            : true,
        fontawesome     : "",
        image           : "data:image/gif;base64,R0lGODlhlgCWANU9AAQCBLy+vMTCxPz+/Pz6/OTi5PTy9MTGxNza3IyOjNTS1PT29Nze3NTW1MzKzBQWFOzq7Hx+fOzu7OTm5AQGBLS2tFRWVMzOzBQSFHx6fAwODFxaXBwaHAwKDDw6PJyanBweHJSWlGxqbKyurGRiZExOTCQmJLSytHRydDw+PFxeXGxubDQ2NISChISGhGRmZCwqLExKTHR2dCwuLERCRCQiJKSipJyenDQyNIyKjERGRKyqrFRSVP///wAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh/wtYTVAgRGF0YVhNUDw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDc3NywgMjAxMC8wMi8xMi0xNzozMjowMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNSBXaW5kb3dzIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjlFRThENEVGRTk4MjExRTFCQTdCOEFDQkJCNzFFMkE3IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjlFRThENEYwRTk4MjExRTFCQTdCOEFDQkJCNzFFMkE3Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OUVFOEQ0RURFOTgyMTFFMUJBN0I4QUNCQkI3MUUyQTciIHN0UmVmOmRv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",
        imagePosition   : "center center",
        maxSize         : "100px",
        minSize         : "20px",
        resizeInterval  : 50,
        size            : "50%",
        zIndex          : 9999
    };
    
    $.LoadingOverlaySetup = function(settings){
        $.extend(true, _defaults, settings);
    };
    
    $.LoadingOverlay = function(action, options){
        switch (action.toLowerCase()) {
            case "show":
                var settings = $.extend(true, {}, _defaults, options);
                _Show("body", settings);
                break;
                
            case "hide":
                _Hide("body", options);
                break;
        }
    };
    
    $.fn.LoadingOverlay = function(action, options){
        switch (action.toLowerCase()) {
            case "show":
                var settings = $.extend(true, {}, _defaults, options);
                return this.each(function(){
                    _Show(this, settings);
                });
                
            case "hide":
                return this.each(function(){
                    _Hide(this, options);
                });
        }
    };
    
    
    function _Show(container, settings){
        container = $(container);
        var wholePage   = container.is("body");
        var count       = container.data("LoadingOverlayCount");
        if (count === undefined) count = 0;
        if (count == 0) {
            var overlay = $("<div>", {
                class   : "loadingoverlay",
                css     : {
                    "background-color"  : settings.color,
                    "position"          : "relative",
                    "display"           : "flex",
                    "flex-direction"    : "column",
                    "align-items"       : "center",
                    "justify-content"   : "center"
                }
            });
            if (settings.zIndex !== undefined) overlay.css("z-index", settings.zIndex);
            if (settings.image) overlay.css({
                "background-image"      : "url(" + settings.image + ")",
                "background-position"   : settings.imagePosition,
                "background-repeat"     : "no-repeat"
            });
            if (settings.fontawesome) $("<div>", {
                class   : "loadingoverlay_fontawesome " + settings.fontawesome
            }).appendTo(overlay);
            if (settings.custom) $(settings.custom).appendTo(overlay);
            if (wholePage) {
                overlay.css({
                    "position"  : "fixed",
                    "top"       : 0,
                    "left"      : 0,
                    "width"     : "100%",
                    "height"    : "100%"
                });
            } else {
                overlay.css("position", (container.css("position") == "fixed") ? "fixed" : "absolute");
            }
            _Resize(container, overlay, settings, wholePage);
            if (settings.resizeInterval > 0) {
                var resizeIntervalId = setInterval(function(){
                    _Resize(container, overlay, settings, wholePage);
                }, settings.resizeInterval);
                container.data("LoadingOverlayResizeIntervalId", resizeIntervalId);
            }
            if (!settings.fade) {
                settings.fade = [0, 0];
            } else if (settings.fade === true) {
                settings.fade = [400, 200];
            } else if (typeof settings.fade == "string" || typeof settings.fade == "number") {
                settings.fade = [settings.fade, settings.fade];
            }
            container.data({
                "LoadingOverlay"                : overlay,
                "LoadingOverlayFadeOutDuration" : settings.fade[1]
            });
            overlay
                .hide()
                .appendTo("body")
                .fadeIn(settings.fade[0]);
        }
        count++;
        container.data("LoadingOverlayCount", count);
    }

    function _Hide(container, force){
        container = $(container);
        var count = container.data("LoadingOverlayCount");
        if (count === undefined) return;
        count--;
        if (force || count <= 0) {
            var resizeIntervalId = container.data("LoadingOverlayResizeIntervalId");
            if (resizeIntervalId) clearInterval(resizeIntervalId);
            container.data("LoadingOverlay").fadeOut(container.data("LoadingOverlayFadeOutDuration"), function(){
                $(this).remove()
            });
            container.removeData(["LoadingOverlay", "LoadingOverlayCount", "LoadingOverlayFadeOutDuration", "LoadingOverlayResizeIntervalId"]);
        } else {
            container.data("LoadingOverlayCount", count);
        }
    }
    
    function _Resize(container, overlay, settings, wholePage){
        if (!wholePage) {
            var x = (container.css("position") == "fixed") ? container.position() : container.offset();
            overlay.css({
                top     : x.top + parseInt(container.css("border-top-width"), 10),
                left    : x.left + parseInt(container.css("border-left-width"), 10),
                width   : container.innerWidth(),
                height  : container.innerHeight()
            });
        }
        var c    = wholePage ? $(window) : container;
        var size = "auto";
        if (settings.size && settings.size != "auto") {
            size = Math.min(c.innerWidth(), c.innerHeight()) * parseFloat(settings.size) / 100;
            if (settings.maxSize && size > parseInt(settings.maxSize, 10)) size = parseInt(settings.maxSize, 10) + "px";
            if (settings.minSize && size < parseInt(settings.minSize, 10)) size = parseInt(settings.minSize, 10) + "px";
        }
        overlay.css("background-size", size);
        overlay.children(".loadingoverlay_fontawesome").css("font-size", size);
    }
    
}(jQuery));
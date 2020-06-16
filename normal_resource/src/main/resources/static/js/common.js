var G = {};
(function () {
    G.locate = function (url, param) {
        var qs = [];
        for (var key in param) {
            if (param.hasOwnProperty(key)) {
                qs.push(key + '=' + param[key]);
            }
        }
        window.location.href = url + '?' + qs.join('&');
    }
})();



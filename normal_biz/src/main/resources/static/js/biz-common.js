/**
 * order paid function
 */
(function (w) {
    var $ = w.$;

    if (!$) {
        console.error("jquery not included");
        return;
    }
    if (!(w && w.WebSocket)) {
        console.error('browser not support websocket');
        return;
    }
    //init config
    var config = {
        baseUrl: 'ws://localhost:7001',
        timeout4hb: 2000,
        timeout4closeWind: 5,
        expireSeconds: 10,
        queryInterval: 2000
    };

    $(document).ready(function () {
        if ($('.n-container')) {
            //倒计时
            var countDownNum = setInterval(function () {
                if (config.expireSeconds-- == 0) {
                    clearInterval(countDownNum);
                    $('.n-qrcode').css({
                        background: '#fff url(data:image/png;base64,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) 50% no-repeat;'
                    });

                    console.info('count down exit');
                    return;
                }
                var min = parseInt(config.expireSeconds / 60),
                    second = config.expireSeconds % 60,
                    expireTime = (min < 10 ? '0' + min : min) + ':' + (second < 10 ? '0' + second : second);
                $('#n-expire').text(expireTime);

            }, 1000);
            //轮询
            var queryNum = setInterval(function () {
                var href = window.location.href,
                    from = href.indexOf('?') + 1,
                    keyValue = href.substr(from).split('=');
                if (config.expireSeconds == 0) {
                    clearInterval(queryNum);
                    return;
                }
                $.get(config.baseUrl + '/resource/queryRealUrl', {
                    ordId: keyValue[1]
                }, function (rst) {
                    if (rst.success && rst.data && rst.data.realUrl) {
                        clearInterval(queryNum);
                        $('.n-note').show('slow');
                        window.location.href = rst.data.realUrl;
                    }
                });
            }, config.queryInterval);
        }
    });

    w.createOrder = function (order) {
        $.post(config.baseUrl + '/order/createOrder', order);
    };


    return w;

})(window);







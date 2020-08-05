/*$(document).ready(function () {
    //向上滚动
    setInterval(function (obj) {
        let n = $(".download-info").find("li").height();
        $(obj).find("ul:first").animate({
            marginTop: -n
        }, 700, function () {
            $(this).css({
                marginTop: "0px"
            }).find("li:first").appendTo(this);
        });
    }, 2000);
    //
    let searchBtn = $('#searchBtn'),
        searchInput = $('.pansearch'),
        pageBtns = $('.page-more a'),
        labelsContainer = $('#labels'),
        labels = $('label');

    let baseurl = 'http://127.0.0.1:8080/';

    let queryFun = function (url, pageQueryParam, callback) {
            $.get(baseurl + url, pageQueryParam, function (result) {
                if (!result.success) {
                    console.error('请求失败，result: ' + result);
                    return;
                }
                callback(result.data);
            });
        },
        renderListFun = function (page) {
            let main = $('main');

            if (page && page.totalRecord == 0) {
                main.empty();
                main.append('<span>未找到相应资源</span>');
                return;
            }

            if (page && page.results) {
                let ul = '';
                main.empty();
                $.each(page.results, function (i, item) {
                    ul += '<ul class="list-box">\n' +
                        '            <li>\n' +
                        '                <a href="#" class="article-title">' + item.resName + '</a>\n' +
                        '                <dl>\n' +
                        '                    <dd>\n' +
                        (function () {
                            let labels = '';
                            $.each(item.resLabels, function (i, label) {
                                labels += '<a href="#" class="blue">' + label + '</a>\n'
                            });
                            return labels;
                        })() +

                        '                    </dd>\n' +
                        '                </dl>\n' +
                        '            </li>\n' +
                        '</ul>';
                });
                main.prepend(ul);
            }

        },
        renderLabelsFun = function (data) {
            $.each(data, function (i, item) {
                labelsContainer.append(`<label idx='${item.idx}' > ${item.text}</label>`)
            })
        };

    //listeners here

    //init view
    queryFun('resource/resources', {
        pageNo: 0
    }, renderListFun);

    queryFun('resource/labels', {}, renderLabelsFun);


    searchInput.on('keyup', function (event) {
        let input = searchInput.val();
        if (event.keyCode == 13) {
            if (input) {
                input = input.replace(/\//g, '^');
                queryFun('resource/resources', {
                    keyword: input
                }, renderListFun);
            }
        }
    });

    searchBtn.on('click', function (event) {
        let input = searchInput.val();
        if (input) {
            input = input.replace(/\//g, '^');
            queryFun('resource/resources', {
                keyword: input
            }, renderListFun)
        }
    });

    let selectedLabels = [];
    labels.each(function () {
        let label = $(this);

        if (label.attr('selected')) {
            selectedLabels.push(label.attr('idx'))
        }

        label.on('click', function () {
            if (label.hasClass('selected')) {
                label.removeClass('selected');
                selectedLabels.remove(label.attr('idx'));
            } else {
                label.addClass('selected');
                selectedLabels.push(label.attr('idx'));
            }
            queryFun('resource/resources', {
                labels: selectedLabels
            })
        })
    });

    //page btns action
    pageBtns.each(function () {
        let pageBtn = this;
        $(pageBtn).on('click', function () {

        });
    });

    $(window).resize(function () {
        $("#list").css("min-height", $(window).height() - 105);
    }).resize();


});*/





<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="x-dns-prefetch-control" content="on"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <title>资源捕手专注于it资源及培训资料分享下载</title>
    <meta name="keywords" content="培训资源,电子书,编程,视频教程,技术文章,文档,手册,源码,软件,工具">
    <meta name="description" content="资源捕手专注与分享IT编程学习资源包括电子书,编程,视频教程,技术文章,文档,手册,源码,软件,工具">
    <link rel="Shortcut Icon" href="images/favicon.ico">
    <link rel="apple-touch-icon-precomposed" href="images/logo5.jpg">
    <link href="css/style.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <![endif]-->
    <script src="js/jquery-1.8.3.min.js"></script>
    <script src="js/jquery.form.js"></script>

    <style type="text/css">
        .page-more a {
            display: inline;
            color: #6483a2;
            margin-right: 2px;
        }

        .page-more a:hover {
            background: #bccbd9;
        }

        label {
            display: inline;
            text-overflow: ellipsis;
            position: relative;
            margin-top: 15px;
            margin-right: 10px;
            font-size: 15px;
            line-height: 30px;
            background: rgba(225, 228, 230, 0.98);
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<header class="cd-header">
    <nav class="nav autocd">
        <div class="logo">
            <a href="/" title="首页">资源捕手</a>
        </div>
    </nav>
</header>
<div id="banner" class="pt60">
    <div class="bannerbody">
        <i class="fa fa-search icon-search"></i>
        <input type="text" value="" placeholder="搜你想搜-做一个优秀的资源导航门户" class="pansearch"/>
        <a href="javascript:void(0);" class="panbtn" id="searchBtn">搜索一下</a>
    </div>
</div>
<div class="cd-currentMsg">
    <span class="tags-title"><i class="fa fa-fire"></i>&nbsp;&nbsp;热门下载</span><em class="seperate">|</em>
    <div class="download-info">
        <ul>
            <li>lr***&nbsp;&nbsp;下载了&nbsp;&nbsp;java编程思想</li>
            <li>ra***&nbsp;&nbsp;下载了&nbsp;&nbsp;小型进销存系统C#+winform_c# win</li>
            <li>vi***de&nbsp;&nbsp;下载了&nbsp;&nbsp;经济法基础（套装共4册）</li>
        </ul>
    </div>
</div>
<div class="cd-container clearfix">
    <main>
        <#if page.totalRecord==0>
              <span>未能找到您要的内容. </span>
        <#else>
            <#list page.results as item>
                <ul class="list-box">
                    <li>
                        <a href="${item.realUrl}" class="article-title">${item.resName}</a>
                        <dl>
                            <dd>
                                <a href="/index/index/content/id/990760"><img src="images/favicon.ico" alt="头像">robot</a>
                                <a href="/search/list/language/1" class="blue">编程与开发</a>

                                <a href="/search/list/type/2" class="blue">电子书</a>
                            </dd>
                        </dl>
                    </li>
                </ul>
            </#list>
            <div class="page-more">
                <a href="#" id="page-first">首页</a>
                <a href="#" id='page-before'>上一页</a>
                <a href="#" id="page-after">下一页</a>
                <a href="#" id="page-last">尾页</a>
            </div>
        </#if>

    </main>

    <aside>
        <div class="home-login">
            <h3>一份支持，十分动力</h3>
            <h3>全站<span style="color:red">988972</span>个资源任性搜</h3>
        </div>
        <div class="column-box">
            <h6>资源标签</h6>
            <div id="labels"></div>
        </div>
    </aside>
</div>
<div class="cd-elevator">
    <a href="javascript:void(0)" class="backtop">
        <i class="fa fa-chevron-up"></i>
        <span>返回顶部</span>
    </a>
</div>

<footer class="cd-footer clearfix">
    <div class="main-width clearfix">
        <div class="footer-about">
            <h3>关于资源捕手</h3>
            <p>本站所有资源均来自网络上传，仅用于分享学习交流，请勿用作商业用途，否则后果自负</p>
        </div>
        <div class="footer-friends">
            <h3>友情链接</h3>
            <p>
                <a href="http://code.coder100.com/" target="_blank" title="码农集市源码下载平台">码农集市源码下载平台</a>
                <a href="http://book.coder100.com/" target="_blank" title="码农集市电子书下载平台">码农集市电子书下载平台</a>
                <a href="" target="_blank" title="csdn免积分下载器">csdn免积分下载器</a>
                <a href="" target="_blank" title="pudn免积分下载器">pudn免积分下载器</a>
                <a href="http://www.verypan.com/" target="_blank" title="百度网盘搜索引擎">百度网盘搜索引擎</a>
            </p>
        </div>
    </div>
    <div class="footer-bottom">
        Copyright &copy; 2015 - 2020 All rights reserved.&nbsp;&nbsp;备案号：<a rel="nofollow"
                                                                            href="http://www.beian.miit.gov.cn/"
                                                                            target="_blank">浙ICP备08106551号-3</a>
    </div>
</footer>

<script src="js/common.js"></script>
</body>
</html>

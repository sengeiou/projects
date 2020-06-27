<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>main</title>
    <style>
        body {
            background-color: rgb(238, 238, 238);
            color: rgb(64, 64, 64);
            font: 16px / 1.42857 "Microsoft YaHei", 微软雅黑, "Helvetica Neue", Helvetica, Arial, sans-serif;
        }

        .container {
            display: grid;
            grid-template-rows: 50px 1fr;
            grid-template-columns: 4fr  1fr;
            grid-row-gap: 10px;
            grid-column-gap: 10px;
        }

        .menu {
            grid-row-start: 1;
            grid-row-end: 2;
            grid-column-start: 1;
            grid-column-end: 3;
            background: #333333;
        }

        .menu>ul>li {
            list-style: none;
            float: left;
            margin-left: 10px;
          
        }
        .menu a{
            color: #fff;
        }

        a {
            text-decoration: none;
            color: #333333;
        }

        a:hover {
            color: #007b9c;
        }

        .post-detail {
            background-color: #fff;
            grid-row-start: 2;
            grid-row-end: 3;
            grid-column-start: 1;
            grid-column-end: 2;

        }

        p {
            font-size: 18px
        }

        .link {
            background-color: #fff;
            grid-row-start: 2;
            grid-row-end: 3;
            grid-column-start: 2;
            grid-column-end: 3;
        }

        .link a {
            display: block;
            margin-left: 10px;
        }

    </style>
</head>

<body>
    <div class="container">
        <div class="menu">
            <ul>
                <li><a href="/post/list">首页</a></li>
                <li><a href="/post/aboutMe">关于</a></li>
            </ul>
        </div>
        <div class="post-detail">
                ${post?no_esc}

        </div>

        <#--<div class="link">
            <a href="#">xxx</a><a href="#">yyy</a><a href="#">zzz</a>
        </div>-->
    </div>
</body>

</html>
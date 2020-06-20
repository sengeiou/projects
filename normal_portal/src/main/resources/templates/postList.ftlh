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

        .post-list {
            background-color: #fff;
            grid-row-start: 2;
            grid-row-end: 3;
            grid-column-start: 1;
            grid-column-end: 2;

        }

        .post{
            padding-left: 5px;
            border-bottom: 1px solid darkgray;
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
        .title {
            border-bottom: #ccc dashed 1px;
        }

        .create-date{
            text-align: right;
            color: #ccc;
            font-size: 14px;
        }
    </style>

</head>

<body>
<div class="container">
    <div class="menu">
        <ul>
            <li><a href="#">post</a></li>
            <li><a href="#">about</a></li>
        </ul>
    </div>
    <div class="post-list">

        <div class="post">
            <div class="title">
                <h2>xxx</h2>
                <div class="create-date">2020-06-05</div>
            </div>
            <div>
                <p>
                    ddd
                </p>
            </div>
        </div>
    </div>

     <@page data=data url="list"> </@page>

    <div class="link">
        <a href="#">xxx</a><a href="#">yyy</a><a href="#">zzz</a>
    </div>
</div>
</body>

</html>
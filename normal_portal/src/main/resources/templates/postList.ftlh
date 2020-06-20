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
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    从文章标题中不难发现这篇文章要和大家聊的是两个东东，即响应式和网格。从布局系统来说，响应式布局和网格布局都不是什么新东西了。但是他们结合在一起，可以让响应式布局变得更灵活。正如@Keir Watson所说，只要20行代码就可以实现响应式网格布局。不过，我们今天来看两个比较有意思的布局，看上去并不是很容易实现的布局效果。著作权归作者所有。
                    商业转载请联系作者获得授权,非商业转载请注明出处。
                    原文: https://www.w3cplus.com/ © w3cplus.com
                </p>
            </div>
        </div>

        <div class="post">
            <div class="title">
                <h2>xxx</h2>
                <div class="create-date">2020-06-05</div>
            </div>
            <div>
                <p>
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    sssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfssssasdfasdfs
                    从文章标题中不难发现这篇文章要和大家聊的是两个东东，即响应式和网格。从布局系统来说，响应式布局和网格布局都不是什么新东西了。但是他们结合在一起，可以让响应式布局变得更灵活。正如@Keir Watson所说，只要20行代码就可以实现响应式网格布局。不过，我们今天来看两个比较有意思的布局，看上去并不是很容易实现的布局效果。著作权归作者所有。
                    商业转载请联系作者获得授权,非商业转载请注明出处。
                    原文: https://www.w3cplus.com/ © w3cplus.com
                </p>
            </div>
        </div>

    </div>
    <!--todo: 有条件展现 -->
    <div class="page">
        <a href="#">首页</a><a href="#">上一页</a><a href="#">下一页</a><a href="#">尾页</a>
    </div>

    <div class="link">
        <a href="#">xxx</a><a href="#">yyy</a><a href="#">zzz</a>
    </div>
</div>
</body>

</html>
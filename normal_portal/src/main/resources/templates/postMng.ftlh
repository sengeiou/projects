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
            grid-template-columns: 1fr 1fr 1fr;
        }

        .center {
            grid-column-start: 2;
            grid-column-end: 3;
        }

        .add {
            border-bottom: 1px solid darkgray;
            padding-bottom: 5px;
        }

        label {
            display: block;
        }

        input {
            display: block;
        }

    </style>
</head>

<body>
<div class="container">
    <div class="center">
        <div class="add">
            <form action="/post/add" method="post" enctype="multipart/form-data">
                <label>Token: </label>
                <input name="token"/>
                <label>Post: </label>
                <input type="file" name="post"/>
                <input type="submit">
            </form>
        </div>
        <div class="delete">
            <form action="/post/delete" method="post">
                <label>Token: </label>
                <input name="token"/>
                <label>Id: </label>
                <input name="id"/>
                <input type="submit">
            </form>
        </div>

    </div>
</div>
</body>

</html>
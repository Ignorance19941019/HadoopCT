<%--
  Created by IntelliJ IDEA.
  User: jerry
  Date: 2019/1/18
  Time: 6:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap 101 Template</title>
    <link href="/css/bootstrap.css" rel="stylesheet">

</head>
<body>

<form>
    <div class="form-group">
        <label for="tel">电话号码</label>
        <input type="text" class="form-control" id="tel" placeholder="请输入电话号码">
    </div>
    <div class="form-group">
        <label for="calltime">查询时间</label>
        <input type="text" class="form-control" id="calltime" placeholder="请输入查询时间">
    </div>
    <button type="button" class="btn btn-default" onclick="querydata()">查询</button>
</form>

<script src="/js/jquery-3.2.0.min.js"></script>
<script src="/js/bootstrap.js"></script>
<script>
    function querydata() {
        window.location.href = "/view?tel="+$("#tel").val()
        + "&calltime=" +$("#calltime").val();
    }
</script>

</body>
</html>

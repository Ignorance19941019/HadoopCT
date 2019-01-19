<%--
  Created by IntelliJ IDEA.
  User: jerry
  Date: 2019/1/18
  Time: 6:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main1" style="width: 600px;height:400px;"></div>
<div id="main2" style="width: 600px;height:400px;"></div>


<script src="/js/jquery-3.2.0.min.js"></script>
<script src="/js/bootstrap.js"></script>
<script src="/js/echarts.min.js"></script>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart1 = echarts.init(document.getElementById('main1'));
    var myChart2 = echarts.init(document.getElementById('main2'));

    // 指定图表的配置项和数据

    var option1 = {
        title: {
            text: '通话次数统计'
        },
        tooltip: {},
        legend: {
            data:['通话次数']
        },
        xAxis: {
            data: [
                <c:forEach items="${calllogs}" var="calllog">
                ${calllog.dateid},
                </c:forEach>
            ]
        },
        yAxis: {},
        series: [{
            name: '通话次数',
            type: 'bar',
            data: [
                <c:forEach items="${calllogs}" var="calllog">
                ${calllog.sumcall},
                </c:forEach>
            ],
            itemStyle: {
                normal: {
                    color: '#37A2DA'
                }
            }
        }]
    };

    var option2 = {
        title: {
            text: '通话时长统计'
        },
        tooltip: {},
        legend: {
            data:['通话时长']
        },
        xAxis: {
            data: [
                <c:forEach items="${calllogs}" var="calllog">
                ${calllog.dateid},
                </c:forEach>
            ]
        },
        yAxis: {},
        series: [{
            name: '通话时长',
            type: 'bar',
            data: [
                <c:forEach items="${calllogs}" var="calllog">
                ${calllog.sumduration},
                </c:forEach>
            ]
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart1.setOption(option1);
    myChart2.setOption(option2);
</script>


</body>
</html>
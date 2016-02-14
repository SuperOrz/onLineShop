<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'left.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/left.css'/>">
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
	<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>" type="text/css" media="all">
<script language="javascript">
/*
 *对象名必须与第一个参数相同
 */
var bar = new Q6MenuBar("bar", "网上书城");
$(function() {
	bar.colorStyle = 2;//指定配色模式，共有0,1,2,3,4五种
	bar.config.imgDir = "<c:url value='/menu/img/'/>";
	bar.config.radioButton=true;//多个一级分类时候排斥
	/*
	 *参数1一级分类名
	 *参数2二级分类名
	 *参数3点击二级分类后要跳转的页面
	 *参数4连接页面在哪个框架叶显示
	 */
	<c:forEach items = "${parents}" var= "parent">
		<c:forEach items = "${parent.children}" var = "child">
			bar.add("${parent.cname}", "${child.cname}", "/goods/admin/AdminBookServlet?method=findByCategory&cid=${child.cid}", "body");
		</c:forEach>
	</c:forEach>

	
	$("#menu").html(bar.toString());
});
</script>
  </head>
  
  <body onload="load()">
<div id="menu"></div>
  </body>
</html>

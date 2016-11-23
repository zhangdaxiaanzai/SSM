<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <!-- 引入jstl -->
   	<%@include file="common/tag.jsp" %> 
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
   
    <title>秒杀列表页</title>
   	<%@include file="common/common.jsp" %>
  </head>
  <body>
  <!-- 页面显示部分 -->
  	<div class="container">
  		<div class="panel panel-default">
  			<div class="panel-heading text-center">
  				<h2>秒杀列表</h2>
  			</div>
  			<div class="panel-body">
				<table id="table-1" class="table table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>详情页</th>
							
						</tr>
					</thead>
					
					<tbody>
					<c:forEach var="sk" items="${listSeckill}">
						<tr>
							<td>${sk.name}</td>
							<td>${sk.number}</td>
							<td>
								<fmt:formatDate value="${sk.startTime} pattern="yyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								<fmt:formatDate value="${sk.endTime} pattern="yyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								<fmt:formatDate value="${sk.createTime} pattern="yyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								<a class="btn btn-info" href="/seckill/${sk.seckillId}/detailSeckill" target="_blank">详情</a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
  			</div>
  		</div>
  	
  	
  	
  	</div>
  
  	
  </body>
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>

	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</html>
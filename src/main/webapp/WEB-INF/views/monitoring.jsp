<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>ALTIBASE-JDBC</title>

    <!-- Bootstrap core CSS -->
    <link href="./resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="form-validation.css" rel="stylesheet">
  </head>

  <body class="bg-light">

    <div class="container">
      <div class="py-5 text-center">
        <img class="d-block mx-auto mb-4" src="./resources/img/favicon.ico" alt="" width="50" height="50">
        <h2>ACCORDION</h2>
        <p class="lead"></p>
      </div>

      <div class="row">
        <div class="col-md-12 order-md-1">
        	<h4 class="mb-3">ALTIBASE Mornitoring</h4>
            <div class="mb-3">
                <label for="session">세션 정보</label>
                <div class="mb-3" style="overflow-y: hidden;overflow-x: auto;background-color: white;">
                <table class="table">
                	<thead>
                		<tr>
                			<th scope="col">No</th>
	                		<c:forEach items="${sessionColList}" var="col" varStatus="status" >
	                			<th scope="col">${col}</th>	
	                		</c:forEach>
                		</tr>
                	</thead>
                	<tbody>
                		<c:choose>
                			<c:when test="${fn:length(sessionList) > 0}">
	                			<tr>
			                		<c:forEach items="${sessionList}" var="sessions" varStatus="status" >
			                				<td>${status.count}</td>
			                				<c:forEach items="${sessions}" var="session">
			                					<td>${session}</td>
			                				</c:forEach>
			                		</c:forEach>
	                			</tr>
                			</c:when>
                			<c:otherwise>
                				<tr>
									<td colspan="${fn:length(sessionColList)}" style="text-align: center;">검색된 결과가 없습니다.</td>
								</tr>
                			</c:otherwise>
                		</c:choose>
                	</tbody>
                </table>
                </div>
            </div>
            
            <hr class="mb-4">
            <br/>
            <br/>
            
            <div class="mb-3">
                <label for="transaction">Transaction 및 lock 정보</label>
                <div class="mb-3" style="overflow-y: hidden;overflow-x: auto;background-color: white;">
                <table class="table">
                	<thead>
                		<tr>
                			<th scope="col">No</th>
	                		<c:forEach items="${transactionColList}" var="col" varStatus="status" >
	                			<th scope="col">${col}</th>	
	                		</c:forEach>
                		</tr>
                	</thead>
                	<tbody>
		                <c:choose>
		                	<c:when test="${fn:length(transactionList) > 0}">
		                		<c:forEach items="${transactionList}" var="transactions" varStatus="status" >
		                			<tr>
		                				<td>${status.count}</td>
		                				<c:forEach items="${transactions}" var="transaction">
		                					<td>${transaction}</td>
		                				</c:forEach>
		                			</tr>
		                		</c:forEach>
		                	</c:when>
							<c:otherwise>
								<tr>
									<td colspan="${fn:length(transactionColList)}" style="text-align: center;">검색된 결과가 없습니다.</td>
								</tr>
							</c:otherwise>		                
		                </c:choose>
                	</tbody>
                </table>
                </div>
            </div>
             
            <hr class="mb-4">
            <br/>
            <br/>
            
            <div class="mb-3">
                <label for="memory">알티베이스의 메모리 사용 현황</label>
                <div class="mb-3" style="overflow-y: hidden;overflow-x: auto;background-color: white;">
                <table class="table">
                	<thead>
                		<tr>
                			<th scope="col">No</th>
	                		<c:forEach items="${memoryColList}" var="col" varStatus="status" >
	                			<th scope="col">${col}</th>	
	                		</c:forEach>
                		</tr>
                	</thead>
                	<tbody>
                		<c:choose>
                			<c:when test="${fn:length(memoryList) > 0 }">
		                		<c:forEach items="${memoryList}" var="memorys" varStatus="status" >
		                			<tr>
			                			<td>${status.count}</td>
		                				<c:forEach items="${memorys}" var="memory">
		                					<td>${memory}</td>
		                				</c:forEach>
		                			</tr>
		                		</c:forEach>
	               			</c:when>
	               			<c:otherwise>
								<tr>
									<td colspan="${fn:length(memoryColList)}" style="text-align: center;">검색된 결과가 없습니다.</td>
								</tr>
	               			</c:otherwise>
                		</c:choose>
                	</tbody>
                </table>
                </div>
            </div>
             
            <hr class="mb-4">
            <br/>
            <br/>
            
            <div class="mb-3">
                <label for="tablespace">메모리 테이블스페이스 사용량</label>
                <div class="mb-3" style="overflow-y: hidden;overflow-x: auto;background-color: white;">
                <table class="table">
                	<thead>
                		<tr>
                			<th scope="col">No</th>
	                		<c:forEach items="${tablespaceColList}" var="col" varStatus="status" >
	                			<th scope="col">${col}</th>	
	                		</c:forEach>
                		</tr>
                	</thead>
                	<tbody>
                		<c:choose>
                			<c:when test="${fn:length(tablespaceList) > 0 }">
		                		<c:forEach items="${tablespaceList}" var="tablespaces" varStatus="status" >
		                			<tr>
		                				<td>${status.count}</td>
		                				<c:forEach items="${tablespaces}" var="tablespace" varStatus="status" >
		                					<td>${tablespace}</td>
		                				</c:forEach>
		                			</tr>
		                		</c:forEach>
                			</c:when>
	               			<c:otherwise>
								<tr>
									<td colspan="${fn:length(tablespaceColList)}" style="text-align: center;">검색된 결과가 없습니다.</td>
								</tr>
	               			</c:otherwise>
                		</c:choose>
                	</tbody>
                </table>
                </div>
            </div>
             
            <hr class="mb-4">
            <br/>
            <br/>
            
            <div class="mb-3">
                <label for="replication">이중화 대상 테이블 목록</label>
                <div class="mb-3" style="overflow-y: hidden;overflow-x: auto;background-color: white;">
                <table class="table">
                	<thead>
                		<tr>
                			<th scope="col">No</th>
	                		<c:forEach items="${replicationColList}" var="col" varStatus="status" >
	                			<th scope="col">${col}</th>	
	                		</c:forEach>
                		</tr>
                	</thead>
                	<tbody>
                		<c:choose>
                			<c:when test="${fn:length(replicationList) > 0 }">
		                		<c:forEach items="${replicationList}" var="replications" varStatus="status" >
		                			<tr>
	                					<td>${status.count}</td>
		                				<c:forEach items="${replications}" var="replication">
		                					<td>${replication}</td>
		                				</c:forEach>
		                			</tr>
		                		</c:forEach>
                			</c:when>
                			<c:otherwise>
								<tr>
									<td colspan="${fn:length(replicationColList)}" style="text-align: center;">검색된 결과가 없습니다.</td>
								</tr>
                			</c:otherwise>
                		</c:choose>
                	</tbody>
                </table>
                </div>
            </div>
            
            <hr class="mb-4">
            <br/>
            <br/>
            
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="./resources/js/jquery-1.8.0.min.js" ></script>
    <!-- <script src="../../../../assets/js/vendor/popper.min.js"></script> -->
    <script src="./resources/js/bootstrap.min.js"></script>
    <!-- <script src="../../../../assets/js/vendor/holder.min.js"></script> -->
    <script>
       $(function() {
    	   $(document).on('change', '#crud', function(){
    		   var crud = $(this).val();
    		   var sql = '';
    		   if(crud == 's') {
    			   sql = 'select * from test';
    		   } else {
    			   sql = 'insert into test(K01) values(1)';
    		   }
   			   $('#sql').text(sql);
    	   });
	        $(document).on('click', '#executeBtn', function(){
	        	var form = document.getElementById("form");
	        	var validate = form.checkValidity();
	        	if (validate === false) {
	                event.preventDefault();
	                event.stopPropagation();
	               // return;
	            }
	        	form.classList.add('was-validated');
	        	
	        	if(validate == false ) {
	        		return;
	        	}
	        	
		        var crud = $('#crud').val();
		        var serverIp = $('#serverIp').val();
		        var serverPort = $('#serverPort').val();
		        var id = $('#id').val();
		        var password = $('#password').val();
		        var jdbcDriver = $('#jdbcDriver').val();
		        var schema = $('#schema').val();
		        var sql = $('#sql').val();
		        
				$.ajax({
					url: '${pageContext.request.contextPath}' + '/execute',
		            type: 'POST',
		            dataType: 'json',
		            data:  { 
		            	'crud' : crud,
		            	'serverIp' : serverIp,
		            	'serverPort' : serverPort,
		            	'id' : id,
		            	'password' : password,
		            	'jdbcDriver' : jdbcDriver,
		            	'schema' : schema,
		            	'sql' : sql
		            },
		            success: function (result) {
		        		var colums = result.colums;
		        		var datas = result.datas;
		        		
		        		var html = '';
		        		
		        		if(result.error != null){
			        		html = '<p>'+result.error+'</p>';
		        		} else if(crud == 's') {
		        		
			        		html += '<table class="table">';
			        		html += ' <thead>';
			        		html += '		<tr>';
			        		$.each(colums, function(index, value){
			        			html += '		<th scope="col">' + value+ '</th>';
			        		});
			        		html += '		</tr>';
			        		html += ' </thead>';
			        		
			        		html += ' <tbody>';
			        		if(datas.length > 0 ) {
				        		$.each(datas, function(index, data){
				        			html += '		<tr>';
				        			$.each(data, function(index, value) {
					        			html += '		<td>' + value+ '</td>';
				        			});
					        		html += '		</tr>';
				        		});
			        		} else {
			        			html += '		<tr>';
			        			html += '		<td colspan="'+colums.length+'" style="text-align: center;">검색된 데이터가 없습니다.</td>';
				        		html += '		</tr>';
			        		}
			        		html += ' </tbody>';
			        		html += '</table>';
		        		} else if(crud != 's') {
		        			var row = result.row;
		        			html += '<p>'+row+'row가 성공적으로 업데이트 되었습니다.</p>';
		        		}
		            	$('#result').html(html);
		            },
		            error : function (request, status, error) {
		              		$('#result').text('code:'+request.status+'\n'+'message:'+request.responseText+'\n'+'error:'+error);
		            }
		        });
	        }); 
      }); 
    </script>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
          <h4 class="mb-3">ALITBASE JDBC TEST</h4>
          <form class="needs-validation" id="form" role="form" data-toggle="validator"><!-- novalidate -->
            <div class="mb-3">
                <label for="crud">CRUD</label>
                <select class="custom-select d-block w-100" id="crud" name="crud" required>
                  <option value="s">Select</option>
                  <option value="cud">Insert/Update/Delete</option>
                </select>
                <div class="invalid-feedback">
                  CRUD를 선택하세요.
                </div>
              </div>
              
            <div class="row">
              <div class="col-md-6 mb-3">
				<label for="serverIp">Server IP</label>
	            <input type="text" class="form-control" id="serverIp" name="serverIp" placeholder="" value="10.20.200.201" required>
	            <div class="invalid-feedback">
	             	Server IP를 입력하세요.
	            </div>
              </div>
              <div class="col-md-6 mb-3">
				<label for="serverPort">Server Port</label>
              	<input type="text" class="form-control" id="serverPort" name="serverPort" placeholder=""  value="31001" required>
              	<div class="invalid-feedback">
                	Server Port를 입력하세요.
              	</div>
              </div>
            </div>
              
            <div class="row">
              <div class="col-md-6 mb-3">
				<label for="id">ID</label>
	            <input type="text" class="form-control" id="id" name="id" placeholder="" value="altibase" required>
	            <div class="invalid-feedback">
	             	ID를 입력하세요.
	            </div>
              </div>
              <div class="col-md-6 mb-3">
				<label for="password">Password</label>
              	<input type="password" class="form-control" id="password" name="password" placeholder=""  value="altibase" required>
              	<div class="invalid-feedback">
                	Password를 입력하세요.
              	</div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6 mb-3">
				<label for="jdbcDriver">JDBC Driver</label>
              	<input type="text" class="form-control" id="jdbcDriver" name="jdbcDriver" placeholder="" value="Altibase.jdbc.driver.AltibaseDriver" required>
              	<div class="invalid-feedback">
                	JDBC Driver를 입력하세요.
              	</div>
              </div>
              <div class="col-md-6 mb-3">
				<label for="serverPort">Schema</label>
              	<input type="text" class="form-control" id="schema" name="schema" placeholder=""  value="mydb" required>
              	<div class="invalid-feedback">
                	Schema를 입력하세요.
              	</div>
              </div>
            </div>
            
            <div class="mb-3">
              <label for="sql">SQL</label>
              <input type="text" class="form-control" id="sql" name="sql" placeholder="" value="select * from test" required>
              <div class="invalid-feedback">
                	SQL을 입력하세요.
              </div>
            </div>

            <button class="btn btn-primary btn-lg btn-block" type="button" id="executeBtn">Execute</button>
            <hr class="mb-4">
            
          </form>
            <label for="result">Result</label>
            <div class="mb-3" style="overflow-y: hidden;overflow-x: auto;background-color: white;">
              <!-- <textarea rows="" cols="" class="form-control"></textarea> -->
              <%-- <div class="well" id="result" name="result">${result}
              </div> --%>
			  <div  id="result" style="min-height: 300px;">
			  </div>
            </div>
            
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
   			   $('#sql').val(sql);
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

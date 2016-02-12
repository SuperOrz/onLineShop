/**
 * 
 */
$(function(){
	//获取.errorClass的标签，判断是否显示
	$(".errorClass").each(function(){
		showError($(this));
	});
	//切换注册按钮的图片
	$("#submitBtn").hover(
			function(){
				$(this).attr("src","/goods/images/regist2.jpg");
				},
			function(){
				$(this).attr("src","/goods/images/regist1.jpg");
			}
	);
	//输入框得到焦点，隐藏错误信息
	$(".inputClass").focus(function(){
		var errorId = $(this).attr("id")+"Error";
		$("#"+errorId).text("");
		showError($("#"+errorId));
	});
	$(".inputClass").blur(function(){
		var id = $(this).attr("id");
		var funcName = "validate" + id.substring(0,1).toUpperCase() + id.substring(1) +"()";
		eval(funcName);
	});
	$("#registForm").submit(function(){
		var bool = true;
		if(!validateLoginname()){
			bool = false;
		}
		if(!validateLoginpass()){
			bool = false;
		}
		if(!validateReloginpass()){
			bool = false;
		}
		if(!validateVerifyCode()){
			bool = false;
		}
		if(!validateEmail()){
			bool = false;
		}
		return bool;
	});
	
});
/*
 * 登录名校验
 */
function validateLoginname(){
	var id = "loginname";
	var value = $("#"+id).val();
	//非空校验
	if(!value){
		 $("#"+id + "Error").text("用户名不能为空");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	//长度校验
	if(value.length < 3 || value.length >20){
		$("#"+id + "Error").text("用户名必须为3-20位");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateLoginname",loginname:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result) {
			if(!result){
				$("#"+id + "Error").text("用户名已经注册");
				 showError( $("#"+id + "Error") );
				 return false;
			}
		}		
	});
	return true;
	
}

/*
 * 密码校验
 */
function validateLoginpass(){
	var id = "loginpass";
	var value = $("#"+id).val();
	//非空校验
	if(!value){
		 $("#"+id + "Error").text("密码不能为空");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	//长度校验
	if(value.length < 3 || value.length >20){
		$("#"+id + "Error").text("密码必须为3-20位");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	return true;
}
/*
 * 确认密码校验
 */
function validateReloginpass(){
	var id = "reloginpass";
	var value = $("#"+id).val();
	//非空校验
	if(!value){
		 $("#"+id + "Error").text("确认密码不能为空");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	//一致校验
	if(value!= $("#loginpass").val()){
		$("#"+id + "Error").text("两次输入的密码必须相同");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	return true;
}
/*
 * 验证码校验
 */
function validateVerifyCode(){
	var id = "verifyCode";
	var value = $("#"+id).val();
	//非空校验
	if(!value){
		 $("#"+id + "Error").text("验证码不能为空");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	//长度校验
	if(value.length != 4){
		$("#"+id + "Error").text("验证码错误");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateVerifyCode",verifyCode:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id + "Error").text("错误的验证码");
				 showError( $("#"+id + "Error") );
				 return false;
			}	
		}		
	});
	return true;
}
/*
 * Email校验
 */
function validateEmail(){
	var id = "email";
	var value = $("#"+id).val();
	//非空校验
	if(!value){
		 $("#"+id + "Error").text("Email不能为空");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	//格式校验
	if(!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)){
		$("#"+id + "Error").text("Email格式不正确");
		 showError( $("#"+id + "Error") );
		 return false;
	}
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateEmail",email:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id + "Error").text("email已经被注册");
				 showError( $("#"+id + "Error") );
				 return false;
			}	
		}		
	});
	return true;
}


function showError(ele){
	var text = ele.text();
	if(!text){
		ele.css("display","none");//无内容不显示
	}else{
		ele.css("display","");//有内容显示
	}
}
/*
 * 换一张验证码
 * 先拿到img元素
 * 然后将src重新指向生成验证码的Servlet
 * 最后加上一个参数，防止浏览器缓存
 */
function _hyz(){
	$("#verifyImg").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
}
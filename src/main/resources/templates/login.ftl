<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<#global basepath>${req.contextPath}</#global>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>用户登录</title>
</head>
<script type="text/javascript" src="/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<body>

<div style="margin: 0 auto; padding-bottom: 0%; padding-top: 7.5%; width: 380px;">
    <div>
        <div>
            <div></div>
            <h3>Login</h3>
        </div>

        <div>
            <form class="form-horizontal m-t-20" method="post" id="loginForm">

                <div class="form-group">
                    <div class="col-xs-12">
                        <input id="username" class=" input-lg input-border" name="username" type="text" required=""
                               placeholder="请输入账号 :)">
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-12">
                        <input id="password" class=" input-lg input-border" name="password" type="password" required=""
                               placeholder="请输入密码">
                    </div>
                </div>
                <div class="form-group text-center m-t-40">
                    <div class="col-xs-12">
                        <input type="button" value="登录" onclick="login()">
                    </div>
                </div>
            </form>
        </div>

    </div>
</div>
<!-- Main  -->
<script type="text/javascript">
   function login () {
       var postData = {};
       postData.loginId=$("#username").val();
       postData.password=$("#password").val();
       nova.postJson("novaWeb/login", postData, success, fail, null);
    };

   function success(data) {
       window.location.href = '/novaWeb/index';
   }
   function fail(message) {
       alert(message);
   }

</script>
</body>
</html>
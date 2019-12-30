<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>宅技术不宅生活</title>
</head>
<script type="text/javascript" src="/js/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<body>
<div>
    fortune：
    <div>
        ${fortune}
    </div>
</div>
<div>---------------------------------------------------------------</div>
<div></div>
<div>
    <form action="download" id="download" method="get">
        <button type="submit">生成图片</button></br>
        <textarea type="text" id="text" name="text"></textarea>
    </form>
</div>
<div>---------------------------------------------------------------</div>
<div>
    <td>原文：<input type="text" id="plaintext" name="plaintext"></input><br></td>
    <td>加密方式：<input type="radio" name="encryModeEnum" value="MD5">MD5</input> </td>
    <td><input type="radio" name="encryModeEnum" value="SHA256">SHA256</input><br></td>
    <td><input type="button" onclick="encrypt()" value="加密"></input><br></td>
    <td>密文：<input type="text" id="encrypt" name="encrypt" style="width: 400px"></input></td>
</div>
<div>---------------------------------------------------------------</div>

<div><a href="javascript:void(0);" onclick="gotoImageMask()">gotoImageMask</a></div>


<script>
    function encrypt(){
        var data = {};
        data.plaintext=$("#plaintext").val();
        data.encryModeEnum=$("input[name='encryModeEnum']:checked").val();

        nova.postJson("encrypt", data, encryptSuccess, encryptFail, null);
    }

    function encryptSuccess(data) {
        $("#encrypt").val(data);
    }
    function encryptFail(message) {
        alert(message);
    }
    
    function gotoImageMask() {
        window.open('/novaWeb/imagemask');
    }

</script>
</body>
</html>


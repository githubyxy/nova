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
<script type="text/javascript" src="/js/imagemask.js"></script>
<body>
<div id='main' class='section dark'>
    <div class='step'>choose an word file</div>
    <img id='preview' class='preview hide'/>
<#--    <div class='sectionbody'>-->
<#--        <input type='file' id='file'/>-->
<#--    </div>-->
    <form  id="doc2pdf" method="post" enctype="multipart/form-data">
        <input type='file' id='file'/>
        <td><input type="button" onclick="doc2pdf()" value="转pdf"></input></td>
    </form>
</div>

<#--<div>-->
<#--    -->
<#--    <td><input type="button" onclick="doc2pdf()" value="转pdf"></input><br></td>-->
<#--</div>-->

<script type="text/javascript">
    function doc2pdf(){
        $("#doc2pdf").submit();
    }

    function encryptSuccess(data) {
    }
    function encryptFail(message) {
        alert(message);
    }
</script>
</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

    Hello, ${key.gname}
    <hr/>
    <#if age < 18 >
        未成年
    </#if>
    <#if (age >= 18)>
        成年
    </#if>

    <#if age < 18>
        未成年
        <#elseif age < 40>
        成年
        <#elseif age < 60>
        中年
        <#else>
        老年
    </#if>

    <hr/>
    循环：
    <#list goodslist as goods>
        ${goods.id} - ${goods.gname} <br/>
    </#list>

    <hr/>
    日期处理：
    ${now?date}
    ${now?time}
    ${now?datetime}
    ${now?string('yyyy年MM月dd日 HH时mm分ss秒SSS毫秒')}

</body>
</html>
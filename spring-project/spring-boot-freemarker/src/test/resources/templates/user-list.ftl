[
<#list users as user>
    {
        "id": ${user.id?c},
        "username":"${user.username}",
        "password":"${user.password}",
        "enabled": ${user.enabled!"false"},
        "remark": ${user.remark!"null"},
        "gmtCreated":"${user.gmtCreated}",
        "gmtModified":"${user.gmtModified}"
    }
</#list>
]

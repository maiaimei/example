listing numbers, e.g. 1..5:
<#list 1..5 as n>
    ${n}
</#list>

listing numbers, e.g. 1..num:
<#list 1..num as n>
    ${n}
</#list>

listing a array:
<#list [10,20,50,100] as n>
    ${n}
</#list>

list the key-value pairs of a hash (since 2.3.25):
<#list hash as key, value>
    key: ${key}, value: ${value}
</#list>

listing a sequence (or collection):
<#list users as user>
    <#-- Part repeated for each item -->
    username: ${user.username}
<#else>
    <#-- Part executed when there are 0 items -->
    no users
</#list>

listing a sequence (or collection):
<#if users??>
[
<#list users as user>
    {
        "id": ${user.id?c},
        "username": "${user.username}",
        "password": "${user.password}",
        "gmtCreated": "${user.gmtCreated}",
        "gmtModified": "${user.gmtModified}"
    }<#if user_index < (users?size - 1)>,</#if>
</#list>
]
</#if>

listing a sequence (or collection):
<#list users>
    <#-- Part executed once if we have more than 0 items -->
[
    <#items as user>
        <#-- Part repeated for each item -->
    {
        "id": ${user.id?c},
        "username": "${user.username}",
        "password": "${user.password}",
        "gmtCreated": "${user.gmtCreated}",
        "gmtModified": "${user.gmtModified}"
    }<#sep>, </#sep>
    </#items>
]
    <#-- Part executed once if we have more than 0 items -->
<#else>
    <#-- Part executed when there are 0 items -->
[]
</#list>

break directive:
<#list 1..10 as x>
    ${x}
    <#if x == 3>
        <#break>
    </#if>
</#list>

continue directive:
<#list 1..5 as x>
    <#if x == 3>
        <#continue>
    </#if>
    ${x}
</#list>

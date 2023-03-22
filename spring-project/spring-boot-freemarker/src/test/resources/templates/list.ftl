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
<#list emptyUsers as user>
    <#-- Part repeated for each item -->
    username: ${user.username}
<#else>
    <#-- Part executed when there are 0 items -->
    no users
</#list>

listing a sequence (or collection):
<#list users>
    <#-- Part executed once if we have more than 0 items -->
    <#items as user>
        <#-- Part repeated for each item -->
        username: ${user.username}
    </#items>
    <#-- Part executed once if we have more than 0 items -->
<#else>
    <#-- Part executed when there are 0 items -->
    no users
</#list>

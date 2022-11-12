<p>尊敬的京东用户您好：</p>
<p>感谢您在京东(<a href="https://www.jd.com/" target="_blank">JD.COM</a>)购物！</p>
<p>我们已经收到了您的订单，会尽快为您安排发货。您选择的是在线支付，订单信息以“我的订单”页面显示为准，您也可以随时进入页面对订单进行修改等操作。</p>
<p style="background-color: rgb(240, 133, 149);color: #fff;">订单编号: ${orderNo} 支付方式: 在线支付</p>
<table>
    <#list commodities as commodity>
        <tr>
            <td>
                <img width="80" border="0" src="${commodity.src}" alt="${commodity.title}">
            </td>
            <td>${commodity.price} * ${commodity.count}</td>
        </tr>
    </#list>
</table>

**发送邮件**

- 发送简单文本邮件

- 发送Html格式邮件，可带附件

- 发送FreeMarker模板邮件

  [FreeMarker 中文官方参考手册](http://freemarker.foofun.cn/)

**接收邮件**

javaMail收邮件主要有两种协议，一种是POP3，一种是IMAP。

POP3和IMAP协议的区别：

- POP3协议允许电子邮件客户端下载服务器上的邮件，但是在客户端的操作（如移动邮件、标记已读等），不会反馈到服务器上。IMAP协议提供电子邮件客户端与服务器间的双向通信，客户端的操作都会同步反应到服务器上。
- POP3不支持判断邮件是否为已读的，也就是说你不能直接从收件箱里面取到未读邮件。IMAP支持判断邮件是否为已读、未读或其他。


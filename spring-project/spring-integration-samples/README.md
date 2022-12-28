# spring-integration-samples

## SFTP

### CentOS7 安装配置SFTP服务器

在CentOS7中，sftp只是ssh的一部分，所以采用yum来安装ssh服务即可。

```shell
# 查看是否安装SSH
ssh -V
# 安装ssh
yum install -y openssl openssh-server
# ssh的服务名是sshd，相关的操作如下：
systemctl stop  sshd.service        # 停止sshd服务
systemctl restart sshd.service      # 重启sshd服务
systemctl status sshd.service       # 查看sshd服务状态
systemctl enable sshd.service       # 设置开机自启动sshd服务
systemctl disable sshd.service      # 禁用开机自启动sshd服务

# 新建SFTP目录并授权
mkdir -p /data/sftp/sftpuser
chown root:root /data/sftp/sftpuser
chmod 755 /data/sftp/sftpuser

# 新建用户组sftp
groupadd sftp
# 新建用户sftpuser，并且设置不支持ssh系统登录，只能登录sftp服务器
# -g 用户组； -d 指定家目录； -s 不能登陆系统； -M 不创建家目录
useradd -g sftp -d /data/sftp/sftpuser -M -s /sbin/nologin sftpuser
# 设置密码
# echo "新密码" | passwd --stdin 用户名
echo "sftpuser" | passwd --stdin sftpuser

# 由于/data/sftp/sftpuser的用户是root，其它用户都没有写的权限
# 所以要在该目录下新建一个目录用于文件的上传下载
mkdir -p /data/sftp/sftpuser/upload
chown sftpuser:sftp /data/sftp/sftpuser/upload
chmod 755 /data/sftp/sftpuser/upload

# 配置SSH和SFTP服务器
cp /etc/ssh/sshd_config /etc/ssh/sshd_config.backup
vim /etc/ssh/sshd_config
# 配置SSH
PermitRootLogin yes
RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeysFile	.ssh/authorized_keys
# 配置SFTP
# 注释掉这行
# Subsystem      sftp    /usr/libexec/openssh/sftp-server
# 文件末尾添加以下内容
Subsystem sftp internal-sftp
Match Group sftp
ChrootDirectory /data/sftp/%u
ForceCommand internal-sftp
# 下面两项是与安全有关
AllowTcpForwarding no
X11Forwarding no

# 关闭Selinux策略
setsebool -P ftpd_full_access on
sed -i s#enforcing#disabled#g /etc/sysconfig/selinux
setenforce 0 && getenforce
getenforce

# 重启SFTP服务
systemctl restart sshd.service

# 本机测试SFTP
[root@sftpserver upload]# sftp sftpuser:sftpuser@192.168.1.28
sftpuser:sftpuser@192.168.1.28's password:
Connected to 192.168.1.28.
sftp> pwd
Remote working directory: /
sftp> cd upload/
sftp> pwd
Remote working directory: /upload
sftp> exit
```

[CentOS7 安装配置SFTP服务器详解](https://blog.csdn.net/weixin_45688268/article/details/126355365)

# 在Docker中安装FastDFS

[https://registry.hub.docker.com/r/season/fastdfs](https://registry.hub.docker.com/r/season/fastdfs)

[Docker中搭建FastDFS文件系统(多图教程)](https://www.jb51.net/article/211928.htm)

## 拉取镜像

```shell
docker pull season/fastdfs:1.2
```

## 创建目录

```shell
mkdir -p /opt/fastdfs/tracker/data /opt/fastdfs/storage/data /opt/fastdfs/store_path
```

## 创建跟踪服务器

```shell
docker run -it -d \
--name tracker \
--net host \
-p 22122:22122 \
--restart=always \
-v /opt/fastdfs/tracker/data:/fastdfs/tracker/data \
season/fastdfs:1.2 tracker
```

## 创建存储服务器

```shell
docker run -it -d \
--name storage \
--net host \
--restart=always \
-v /opt/fastdfs/storage/data:/fastdfs/storage/data \
-v /opt/fastdfs/store_path:/fastdfs/store_path \
-e TRACKER_SERVER="192.168.1.21:22122" \
season/fastdfs:1.2 storage
```

## 修改跟踪服务器配置

将tracker容器中的client.conf复制出来

```shell
docker cp tracker:/etc/fdfs/client.conf /opt/fastdfs/
```

修改client.conf

```shell
vim /opt/fastdfs/client.conf
```

找到tracker_server并修改

```shell
# tracker_server=192.168.0.197:22122
tracker_server=192.168.1.21:22122
```

将修改后的client.conf复制到tracker容器

```shell
docker cp /opt/fastdfs/client.conf tracker:/etc/fdfs/
```

## 测试文件上传

进入tracker容器

```shell
docker exec -it tracker bash
```

创建helloworld.txt文件

```shell
echo "helloworld" > helloworld.txt
```

通过 fdfs_upload_file 命令将 helloworld.txt 文件上传至服务器

```sh
fdfs_upload_file /etc/fdfs/client.conf helloworld.txt
```

上传成功结果类似于

```shell
[root@fastdfs fastdfs]# docker exec -it tracker bash
root@fastdfs:/# echo "helloworld" > helloworld.txt
root@fastdfs:/# fdfs_upload_file /etc/fdfs/client.conf helloworld.txt
group1/M00/00/00/wKgBFWJUHKCAcwL0AAAAC7kDAGc043.txt
```

在宿主机查看下上传的文件

```shell
cd /opt/fastdfs/store_path/data/00/00/ && ll
```

## 创建Nginx

创建nginx目录

```shell
mkdir -p /opt/fastdfs/nginx/
```

将storage容器中的nginx配置文件复制出来

```shell
docker cp storage:/etc/nginx/conf/nginx.conf /opt/fastdfs/nginx/
```

修改nginx.conf

```shell
vim /opt/fastdfs/nginx/nginx.conf
```

找到local节点，修改为

```conf
location / {
   root /fastdfs/store_path/data;
   ngx_fastdfs_module;
}
```

创建nginx容器

```shell
docker run -id --name fastdfs_nginx \
--restart=always \
--net host \
-p 80:80 \
-e TRACKER_SERVER=192.168.1.21:22122 \
-v /opt/fastdfs/store_path:/fastdfs/store_path \
-v /opt/fastdfs/nginx/nginx.conf:/etc/nginx/conf/nginx.conf \
season/fastdfs:1.2 nginx
```

## 扩容Group

[FASTDFS ————扩容group](https://blog.csdn.net/yongyong169/article/details/85231862)

[fastdfs group通过添加硬盘扩容](https://www.cnblogs.com/dingxiaoyue/p/4926709.html)

# SpringBoot整合FastDFS

导入依赖

```xml
<dependency>
    <groupId>com.github.tobato</groupId>
    <artifactId>fastdfs-client</artifactId>
    <version>1.27.2</version>
</dependency>
```

加配置

```yaml
fdfs:
  connect-timeout: 1000
  so-timeout: 3000
  thumb-image:
    width: 60
    height: 60
  tracker-list:
    - 192.168.1.21:22122
```

加配置类

```java
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDFSClientConfig {
}
```

上传、下载、删除、预览代码实现：cn.maiaimei.example.controller.FastDFSController

启动虚拟机：192.168.1.21 - fastdfs(docker)

启动网关层：cn.maiaimei.example.GatewayApplication80

启动应用层：cn.maiaimei.example.FastDFSApplication9002
# pytest

## pytest及插件

```shell
pip install -r requirements.txt
```

pytest

pytest-xdist：测试用例分布执行，多CPU分发

pytest-parallel：多进程或多线程执行测试实例

pytest-ordering：用于改变测试用例的执行顺序

pytest-rerunfailures：用于失败用例重试

pytest-repeat ：重复执行用例的插件

pytest-assume：assume多重断言，断言失败后边的代码会继续执行

pytest-html：生成html格式的报告

allure-pytest：用于生成美观的测试报告，须在本地安装allure且配置环境变量

pyyaml：PyYAML模块是Python的YAML解析器和生成器

requests：用来发 送 HTTP 请求

## pytest测试套件默认规则

测试套件默认规则：测试文件必须以test_开头，测试类名字必须是Test开头，测试方法名字必须是test开头，否则运行时会找不到测试套件。

## pytest测试用例的运行方式

- 主函数模式

- 命令行模式

- 通过读取pytest.ini配置文件运行


## pytest及allure

1、安装allure-pytest插件

```shell
pip install allure-pytest
```

2、下载、解压、配置path路径

[https://github.com/allure-framework/allure2/releases](https://github.com/allure-framework/allure2/releases)

```shell
allure --version
```

3、生成报告

1. 生成JSON格式的临时报告

   ```ini
   # --clean-alluredir 清除临时报告
   # --alluredir=./temp 在temp目录下生成JSON格式的临时报告
   addopts = -vs --clean-alluredir --alluredir=./temp
   ```

2. 生成allure报告

   ```python
   import os
   import pytest
   
   if __name__ == '__main__':
       # 运行所有测试用例，相当于命令行模式的 pytest
       pytest.main()
   
       # 生成allure报告
       os.system("allure generate ./temp -o ./report --clean")
   ```

   [allure 命令行参数说明](https://blog.csdn.net/xcliang9418/article/details/121809201)

3. 定制allure报告

# 相关资料

[Full pytest documentation](https://docs.pytest.org/en/latest/contents.html)

[pytest单元测试框架简介](https://blog.csdn.net/qq_37982823/article/details/122790906)

[pytest多进程或多线程执行测试实例](https://www.jb51.net/article/253929.htm)

[pytest及allure的使用](https://blog.csdn.net/qq_45868731/article/details/123057652)

[2022最新pytest接口自动化测试框架，三天带你精通pytest，带你写出最好的代码！](https://www.bilibili.com/video/BV1py4y1t7bJ)

[2022全网最新精通Pytest自动化测试框架之Pytest数据驱动装饰器接口测试](https://www.bilibili.com/video/BV1pS4y1h7hG)
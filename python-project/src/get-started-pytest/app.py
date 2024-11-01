"""
主函数模式运行测试用例
"""
import os

import pytest

if __name__ == '__main__':
    # 运行所有测试用例，相当于命令行模式的 pytest
    # pytest.main()

    # 运行所有测试用例，相当于命令行模式的 pytest -vs
    pytest.main(["-vs"])

    # 运行指定文件的测试用例，相当于命令行模式的 pytest -vs api/test_api_user_management1.py::TestAPIUserManagement::test_pagequery_user
    # pytest.main(["-vs", "api/test_api_user_management1.py::TestAPIUserManagement::test_pagequery_user"])

    # 运行指定文件的测试用例，相当于命令行模式的 pytest -vs ui/test_ui_login.py
    # pytest.main(["-vs", "ui/test_ui_login.py"])

    # 运行指定目录的测试用例，相当于命令行模式的 pytest -vs api
    # pytest.main(["-vs", "api"])

    # 失败用例重试，相当于命令行模式的 pytest -vs --reruns=2，需要安装插件pytest-rerunfailures
    # pytest.main(["-vs", "--reruns=2"])

    # 多线程执行测试用例，相当于命令行模式的 pytest -vs -n=2，需要安装插件pytest-xdist
    # pytest.main(["-vs", "-n=2"])

    # TODO: 待验证：多线程执行测试用例，相当于命令行模式的 pytest -vs --workers=2 --tests-per-worker=4，需要安装插件pytest-parallel
    # pytest.main(["-vs", '--workers=2', '--tests-per-worker=4'])

    # 分组执行测试用例，相当于命令行模式的 pytest -vs -m=smoke
    # pytest.main(["-vs", "-m=smoke"])

    # 生成allure报告
    os.system("allure generate ./report/temp -o ./report/dist --clean")

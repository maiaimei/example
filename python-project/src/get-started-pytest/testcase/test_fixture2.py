"""
前后置（夹具、固件）
使用@pytest.fixture()装饰器实现部分或全部测试用例的前后置
Pytest之Fixture参数详解及使用: https://blog.csdn.net/qq_42610167/article/details/119818358
"""
import pytest


@pytest.fixture(scope="class", autouse=True)
def my_fixture3():
    print("在所有测试用例之前执行一次 before all functions")
    yield
    print("在所有测试用例之后执行一次 after all functions")


@pytest.fixture(scope="function", autouse=True)
def my_fixture2():
    print("在每个测试用例之前执行 before each function")
    yield
    print("在每个测试用例之后执行 after each function")


@pytest.fixture(scope="function")
def my_fixture1():
    print("在某个测试用例之前执行 before some function")
    yield
    print("在某个测试用例之后执行 after some function")


@pytest.fixture(scope="function", params=["张三", "李四", "王五"])
def my_fixture4(request):
    return request.param


@pytest.fixture(scope="function", params=["张三", "李四", "王五"])
def my_fixture5(request):
    print("在某个测试用例之前执行 before some function")
    yield request.param
    print("在某个测试用例之后执行 after some function")


@pytest.fixture(scope="function", params=["张三", "李四", "王五"], ids=["zs", "ls", "ww"])
def my_fixture6(request):
    print("在某个测试用例之前执行 before some function")
    yield request.param
    print("在某个测试用例之后执行 after some function")


class TestFixture:
    @pytest.mark.skip
    def test1(self):
        print("test1")

    @pytest.mark.skip
    def test2(self):
        print("test2")

    @pytest.mark.skip
    def test3(self, my_fixture1):
        print("test3")

    @pytest.mark.skip
    def test4(self, my_fixture4):
        print("你好，" + str(my_fixture4))

    @pytest.mark.skip
    def test5(self, my_fixture5):
        print("你好，" + str(my_fixture5))

    def test6(self, my_fixture6):
        print("你好，" + str(my_fixture6))

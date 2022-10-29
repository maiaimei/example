"""
前后置（夹具、固件）
setup_method/teardown_method
setup_class/teardown_class
"""


class TestFixture:
    def setup_method(self):
        print("在每个测试用例之前执行")

    def teardown_method(self):
        print("在每个测试用例之后执行")

    def setup_class(self):
        print("在所有测试用例之前执行一次")

    def teardown_class(self):
        print("在所有测试用例之后执行一次")

    def test1(self):
        print("test1")

    def test2(self):
        print("test2")

    def test3(self):
        print("test3")

"""
pytest执行测试用例的顺序，默认从上到下，使用pytest-ordering插件可以改变测试用例的执行顺序，在方法上标记@pytest.mark.run(order=1)即可
"""

import pytest


class TestOrdering:
    @pytest.mark.run(order=2)
    def test_one(self):
        print("1")

    @pytest.mark.run(order=3)
    def test_two(self):
        print("2")

    @pytest.mark.run(order=1)
    def test_three(self):
        print("3")

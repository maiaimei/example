"""
跳过测试用例
"""

import pytest


class TestSkip:
    age = 17

    @pytest.mark.skip(reason="无条件跳过")
    def test1(self):
        print("1")

    @pytest.mark.skipif(age < 18, reason="有条件跳过")
    def test2(self):
        print("2")

    def test3(self):
        print("3")

    def test4(self):
        print("4")

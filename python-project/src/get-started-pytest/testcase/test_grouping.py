"""
分组测试，在方法上标记 @pytest.mark.xxx
执行 pytest -vs -m=xxx 即可
若执行多个分组，执行 pytest -vs -m="myGroup or smoke"，如此类推
"""

import pytest


class TestGrouping:
    @pytest.mark.smoke
    def test1(self):
        print("1")

    @pytest.mark.myGroup
    def test2(self):
        print("2")

    @pytest.mark.smoke
    def test3(self):
        print("3")

    def test4(self):
        print("4")

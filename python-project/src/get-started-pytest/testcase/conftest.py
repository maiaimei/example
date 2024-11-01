"""
某包前后置（夹具、固件）
通过conftest.py和@pytest.fixture()实现公用的前后置
"""
import pytest


@pytest.fixture(scope="function", params=["三", "四", "五"], ids=["s", "s", "w"])
def part_fixture(request):
    print("part_fixture前置")
    yield request.param
    print("part_fixture后置")

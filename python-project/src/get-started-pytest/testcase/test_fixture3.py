"""
前后置（夹具、固件）
通过conftest.py和@pytest.fixture()实现公用的前后置
"""


class TestFixture:
    def test(self, global_fixture, part_fixture):
        print("你好，" + str(global_fixture) + str(part_fixture))

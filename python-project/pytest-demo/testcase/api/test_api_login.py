"""
测试登录
"""


class TestAPILogin:
    def test_login_success(self):
        print("API-测试登录成功")

    def test_login_fail(self):
        print("API-测试登录失败，用户名或密码错误")
        assert 1 == 2

    def test_logout(self):
        print("API-测试注销")

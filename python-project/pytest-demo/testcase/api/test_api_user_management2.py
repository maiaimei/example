"""
测试用户管理
通过 @pytest.mark.parametrize("cfg", read_yaml("/data/test_create_user.yaml")) 实现数据驱动
"""
import pytest

# 跳过整个模块
from util.yaml_util import read_yaml


# pytestmark = pytest.mark.skip('sikp TestAPIUserManagement2')


class TestAPIUserManagement2:
    baseUrl = "http://localhost:8080/users"
    id = None

    @pytest.mark.run(order=1)
    @pytest.mark.parametrize("cfg", read_yaml("/data/test_create_user.yaml"))
    def test_create_user(self, auth_session, cfg):
        print("API-创建用户")
        url = cfg["base_url"] + cfg["request"]["url"]
        json = cfg["request"]["json"]
        resp = auth_session.post(url=url, json=json)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["username"] == json["username"]
        TestAPIUserManagement2.id = resp_json["data"]["id"]

    @pytest.mark.run(order=2)
    def test_update_user(self, auth_session):
        print("API-更新用户")
        json = {
            "id": TestAPIUserManagement2.id,
            "nickname": "pip",
            "username": "pip"
        }
        resp = auth_session.put(url=TestAPIUserManagement2.baseUrl, json=json)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["username"] == json["username"]

    @pytest.mark.run(order=5)
    def test_delete_user(self, auth_session):
        print("API-删除用户")
        resp = auth_session.delete(TestAPIUserManagement2.baseUrl + "/" + TestAPIUserManagement2.id)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"] == "删除用户成功"

    @pytest.mark.run(order=3)
    def test_get_user(self, auth_session):
        print("API-获取用户")
        resp = auth_session.get(url=TestAPIUserManagement2.baseUrl + "/" + TestAPIUserManagement2.id)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["username"] == "pip"

    @pytest.mark.run(order=4)
    def test_pagequery_user(self, auth_session):
        print("API-分页查询用户")
        json = {
            "username": "pip"
        }
        resp = auth_session.post(url=TestAPIUserManagement2.baseUrl + "/pagequery?current=1&size=10", json=json)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["total"] == 1

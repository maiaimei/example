"""
测试用户管理
"""
import pytest
import requests

# 跳过整个模块
pytestmark = pytest.mark.skip('sikp TestAPIUserManagement1')


class TestAPIUserManagement1:
    baseUrl = "http://localhost:8080/users"
    id = None

    @pytest.mark.run(order=1)
    def test_create_user(self):
        print("API-创建用户")
        json = {
            "nickname": "python",
            "username": "python",
            "password": "123456"
        }
        resp = requests.post(url=TestAPIUserManagement1.baseUrl, json=json)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["username"] == json["username"]
        TestAPIUserManagement1.id = resp_json["data"]["id"]

    @pytest.mark.run(order=2)
    def test_update_user(self):
        print("API-更新用户")
        json = {
            "id": TestAPIUserManagement1.id,
            "nickname": "pip",
            "username": "pip"
        }
        resp = requests.put(url=TestAPIUserManagement1.baseUrl, json=json)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["username"] == json["username"]

    @pytest.mark.run(order=5)
    def test_delete_user(self):
        print("API-删除用户")
        resp = requests.delete(TestAPIUserManagement1.baseUrl + "/" + TestAPIUserManagement1.id)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"] == "删除用户成功"

    @pytest.mark.run(order=3)
    def test_get_user(self):
        print("API-获取用户")
        resp = requests.get(url=TestAPIUserManagement1.baseUrl + "/" + TestAPIUserManagement1.id)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["username"] == "pip"

    @pytest.mark.run(order=4)
    def test_pagequery_user(self):
        print("API-分页查询用户")
        json = {
            "username": "pip"
        }
        resp = requests.post(url=TestAPIUserManagement1.baseUrl + "/pagequery?current=1&size=10", json=json)
        resp_json = resp.json()
        assert resp.status_code == 200
        assert resp_json["data"]["total"] == 1

"""
全局前后置（夹具、固件）
通过conftest.py和@pytest.fixture()实现公用的前后置
"""
import pytest
import requests


@pytest.fixture(scope="function", params=["张", "李", "王"], ids=["z", "l", "w"])
def global_fixture(request):
    print("global_fixture前置")
    yield request.param
    print("global_fixture后置")


@pytest.fixture(scope="session")
def session():
    return requests.Session()


@pytest.fixture(scope="session")
def auth_session(session):
    print("auth_session前置")
    # resp = session.post(url="http://localhost/login",
    #                     json={
    #                         "username": "admin",
    #                         "password": "admin"
    #                     })
    # 设置请求头
    # session.headers["x-auth-token"] = resp.json()["token"]
    session.headers["x-auth-token"] = "123456"
    yield session
    print("auth_session后置")

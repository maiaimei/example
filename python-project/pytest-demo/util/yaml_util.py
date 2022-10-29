import os

import yaml

extract_yaml_path = "../extract.yaml"


def get_extract_yaml(key):
    with open(extract_yaml_path, mode="r", encoding="utf-8") as f:
        data = yaml.load(stream=f, Loader=yaml.FullLoader)
        return data[key]


def read_extract_yaml():
    with open(extract_yaml_path, mode="r", encoding="utf-8") as f:
        return yaml.load(stream=f, Loader=yaml.FullLoader)


def write_extract_yaml(data):
    with open(extract_yaml_path, mode="a", encoding="utf-8") as f:
        yaml.dump(data=data, stream=f)


def clear_extract_yaml():
    with open(extract_yaml_path, mode="w", encoding="utf-8") as f:
        f.truncate()


def read_yaml(path):
    with open(os.getcwd() + path, mode="r", encoding="utf-8") as f:
        return yaml.load(stream=f, Loader=yaml.FullLoader)

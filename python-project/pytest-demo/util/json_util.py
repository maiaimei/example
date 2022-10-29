import json


def read_file(path):
    with open(path, mode="r", encoding="utf-8") as f:
        return json.loads(f.read())

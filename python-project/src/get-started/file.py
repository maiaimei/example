#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""
Python 文件操作
"""

import os


def get_file_properties(path):
    # 打开一个文件
    fo = open(path, "a+")
    print("文件名: ", fo.name)
    print("文件编码: ", fo.encoding)
    print("访问模式: ", fo.mode)
    print("是否已关闭: ", fo.closed)
    # print("末尾是否强制加空格 : ", fo.softspace)  # 在Python 3.x.x中，softspace属性已经被移除
    fo.close()
    print("是否已关闭: ", fo.closed)


def read_file_content(path):
    try:
        # 打开文件
        with open(file=path, mode='r', encoding='utf-8') as file:
            # 读取文件内容
            content = file.read()
        # 输出文件内容
        print(content)
    except FileNotFoundError:
        print("文件不存在")
    except Exception as e:
        print("发生错误：", str(e))


def write_file_content(path, content):
    try:
        # 打开文件
        with open(file=path, mode='a+', encoding='utf-8') as file:
            # 写入文件内容
            file.write(content)
            # 查找当前位置
            position = file.tell()
            print("当前文件位置 : ", position)
            # 把指针再次重新定位到文件开头
            position = file.seek(0, 0)
            # 读取文件内容
            result = file.read()
        # 输出文件内容
        print(result)
    except FileNotFoundError:
        print("文件不存在")
    except Exception as e:
        print("发生错误：", str(e))


def list_files(path, result):
    # os.listdir(path) 返回path指定的文件夹包含的文件或文件夹的名字的列表。
    files = os.listdir(path)
    for file in files:
        filepath = path+file
        result.append(filepath)
        if (is_directory(filepath)):
            list_files(filepath+"/", result)
    return result


def is_directory(path):
    return os.path.isdir(path)


# get_file_properties("C:/Users/lenovo/Desktop/tmp/b/2.txt")
# read_file_content("C:/Users/lenovo/Desktop/tmp/b/2.txt")
# write_file_content("C:/Users/lenovo/Desktop/tmp/b/3.txt", "line 9\n")

# 文件重命名：os.rename(current_file_name, new_file_name)
# os.rename("C:/Users/lenovo/Desktop/tmp/b/3.txt",
#           "C:/Users/lenovo/Desktop/tmp/b/three.txt")

# 删除文件：os.remove(file_name)
# os.remove("C:/Users/lenovo/Desktop/tmp/b/three.txt")

# 创建目录：os.mkdir("newdir")
# os.mkdir("C:/Users/lenovo/Desktop/tmp/d")

# 删除目录：os.rmdir('dirname')
# os.rmdir("C:/Users/lenovo/Desktop/tmp/d")

# 改变目录：os.chdir("newdir") TBD：使用场景？
# os.chdir("C:/Users/lenovo/Desktop/tmp/d")

# 获取当前工作目录：os.getcwd()
# print(os.getcwd())

files = []
list_files("C:/Users/lenovo/Desktop/tmp/", files)
for file in files:
    print(file)

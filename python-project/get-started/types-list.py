#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""
Python 列表
一个可以放置任意数据类型的有序集合
列表是动态的，长度可变，可以随意增删改元素
支持负数索引
支持切片操作
列表与元组互转: list(your_tuple), tuple(your_list)
列表的存储空间略大于元组，性能略逊于元组
如果存储的数据或数量可变，选用列表
"""

# 使用[]初始化列表，[]是内置的C函数，可以直接调用，效率更高。
my_list = [2, 1, 7]

# 使用list()创建列表，list()是函数的调用，Python函数的调用会创建堆，并且会进行一系列参数检查的操作。
#my_list = list()

# 添加
my_list.append(6.6)
my_list.append(4)
my_list.append(6.6)
my_list.append(5)
# my_list.append('a')
# my_list.append(True)

# 删除
# my_list.remove(3)

# 修改
#my_list[1] = 'one'

# 常见内置函数
# print(my_list.count(6.6))  # 统计指定值出现的次数。
# print(my_list.index(6.6))  # 返回指定值第一个索引。
# my_list.reverse()          # 原地倒转列表。
# my_list.sort()             # 按升序对列表进行排序。列表元素类型必须一致，否则报错。
# print(list(reversed(my_list)))  # reversed()对列表进行倒转并返回一个新的列表
# print(list(sorted(my_list)))  # sorted()对列表进行升序排序并返回一个新的列表

# print(my_list)       # 输出完整列表
# print(my_list[0])    # 输出列表第一个元素
# 支持负数索引
# print(my_list[-1])   # 输出倒数第一个元素
# print(my_list[-2])   # 输出倒数第二个元素
# 支持切片操作
# print(my_list[1:3])  # 输出列表第二个至第三个元素
# print(my_list[2:])   # 输出列表第二个开始至列表末尾的所有元素

# 遍历-使用for循环
# for item in my_list:
#     print(item)

# 遍历-使用for循环+range函数+len函数
# for index in range(len(my_list)):
#     print("索引为", index, "的值为", my_list[index])

# 遍历-使用for循环+enumerate函数
# for index, value in enumerate(my_list):
#     print("索引为", index, "的值为", value)

# 遍历-使用while循环
# index = 0
# while index < len(my_list):
#     print("索引为", index, "的值为", my_list[index])
#     index += 1

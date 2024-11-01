#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""
Python 元组
一个可以放置任意数据类型的有序集合
元组是静态的，长度固定，不可随便增删改元素
支持负数索引
支持切片操作
列表与元组互转: list(your_tuple), tuple(your_list)
元组相对于列表更加轻量级，性能稍优
如果存储的数据和数量不变，选用元组
"""

# 使用()初始化元组
# my_tuple = ('Hello', 'Python', 1, True, ['a', 'b'])
my_tuple = ('j', 'o', 'e', 'd', 'a', 'e')

# 常见内置函数
# print(my_tuple.count('e'))  # 统计指定值出现的次数。
# print(my_tuple.index('e'))  # 返回指定值第一个索引。
# print(tuple(reversed(my_tuple)))  # reversed()对元组进行倒转并返回一个新的元组
# print(tuple(sorted(my_tuple)))  # sorted()对元组进行升序排序并返回一个新的元组

# print(my_tuple)               # 输出完整元组
# print(my_tuple[0])            # 输出元组第一个元素
# 支持负数索引
# print(my_tuple[-1])           # 输出倒数第一个元素
# print(my_tuple[-2])           # 输出倒数第二个元素
# 支持切片操作
# print(my_tuple[1:3])          # 输出元组第二个至第三个元素
# print(my_tuple[2:])           # 输出元组第三个开始至元组末尾的所有元素

# 使用for循环遍历元组并打印每个元素
# for item in my_tuple:
#     print(item)


# 元组与列表的性能
# 当元组与列表存储相同元素时比较存储空间，元组的存储空间比列表少。这是因为列表是动态的，它需要存储指针，来指向对应元素。
my_list = ['j', 'o', 'e', 'd', 'a', 'e']
print("my_tuple的存储空间", my_tuple.__sizeof__())  # my_tuple的存储空间 72
print("my_list的存储空间", my_list.__sizeof__())  # my_list的存储空间 88

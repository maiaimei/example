#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""
Python 集合
元素不重复
"""

# 创建集合
# my_set = set()
# my_set.add(1)
# my_set.add('one')
# my_set.add(('Mon', 'Tue', 'Wed', 'Thur', 'Fir', 'Sat', 'Sun'))
# my_set.add(1)
# my_set.add('one')

# 创建并初始化集合，不能使用{}，空{}创建的是字典
# my_set = {1, 'one', ('Mon', 'Tue', 'Wed', 'Thur',
#                      'Fir', 'Sat', 'Sun'), 1, "one"}

# print("集合长度：", len(my_set))

# 遍历
# for item in my_set:
#     print(item)

# 创建并初始化两个集合my_set_1和my_set_2
# my_set_1 = {'a', 'b', 'c', 'd'}
# my_set_2 = {'c', 'd', 'e', 'f', 'g'}
# print("my_set_1和my_set_2的交集", my_set_1.intersection(my_set_2))
# print("my_set_2和my_set_1的交集", my_set_2 & my_set_1)
# print("my_set_1和my_set_2的并集", my_set_1.union(my_set_2))
# print("my_set_2和my_set_1的并集", my_set_2 | my_set_1)
# print("my_set_1和my_set_2的差集", my_set_1.difference(my_set_2))
# print("my_set_2和my_set_1的差集", my_set_2 - my_set_1)
# # 对称差运算用于获取两个集合中不共有的元素，并将其组成一个新的集合返回
# print("my_set_1和my_set_2的补集", my_set_1.symmetric_difference(my_set_2))
# print("my_set_2和my_set_1的补集", my_set_2 ^ my_set_1)

# 列表转集合
my_list = ['1', 'one', 'one', '1']
print(my_list)
my_list_to_set = set(my_list)
print(my_list_to_set)

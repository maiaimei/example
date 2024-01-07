#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""
Python 字典
"""

# 创建字典
# my_dic = dict()
my_dic = {}
my_dic["key"] = "value"
my_dic[1] = 'one'
my_dic[('Mon', 'Tue', 'Wed', 'Thur', 'Fir', 'Sat', 'Sun')] = 'week'

# 初始化字典
# my_dic = {"key": "value", 1: 'one',
#           ('Mon', 'Tue', 'Wed', 'Thur', 'Fir', 'Sat', 'Sun'): 'week'}

# 添加
my_dic[2] = '2'

# 修改
my_dic[2] = 'Two'

# 删除
del my_dic[2]

# 遍历
for key in my_dic:
    print("键为", key, "的值为", my_dic[key])

print("字典长度为", len(my_dic))
# 清空字典
my_dic.clear()
print("字典长度为", len(my_dic))

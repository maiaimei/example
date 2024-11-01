#!/usr/bin/python
# -*- coding: UTF-8 -*-
"""
Python I/O操作
标准输入函数：raw_input 和 input
raw_input([prompt]) 函数从标准输入读取一个行，并返回一个字符串（去掉结尾的换行符）
raw_input在Python 3.x.x中是不支持的，它是Python 2.x.x的函数。在Python 3.x.x中使用input替换
input([prompt]) 函数和 raw_input([prompt]) 函数基本类似，但是 input 可以接收一个Python表达式作为输入，并将运算结果返回。
"""

str = input("请输入")
print("你输入的是：%s" % (str))

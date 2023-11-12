package cn.maiaimei.example.service;

import cn.maiaimei.example.registrar.Operation;

@Operation("#a + #b")
public interface AddOperation {

  int add(int a, int b);
}

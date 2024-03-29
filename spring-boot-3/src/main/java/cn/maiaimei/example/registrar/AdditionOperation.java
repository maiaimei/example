package cn.maiaimei.example.registrar;

@Operation("#a + #b")
public interface AdditionOperation {

  int addition(int a, int b);
}

package cn.maiaimei.example.registrar;

@Operation("#a * #b")
public interface MultiplicationOperation {

  int multiplication(int a, int b);
}

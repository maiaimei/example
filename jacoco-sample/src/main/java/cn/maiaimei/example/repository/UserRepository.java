package cn.maiaimei.example.repository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  public String list() {
    return "list user";
  }
}

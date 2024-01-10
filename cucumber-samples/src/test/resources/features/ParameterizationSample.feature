Feature: Parameterization Sample

  Scenario: Account authorization by binding roles

    Given add role 'admin', binding permissions '1001, 1002'
    And add account 'test@qq.com'

    When account 'test@qq.com' binding role 'admin'

    Then authentication account 'test@qq.com', With permission '1001'
    And  authentication account 'test@qq.com', With permission '1002'
    But  authentication account 'test@qq.com', Without permission '1003'

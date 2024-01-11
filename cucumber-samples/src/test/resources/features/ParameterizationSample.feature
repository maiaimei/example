Feature: 参数化 范例

  Scenario: Account authorization by binding roles

    Given [Parameterization Sample] add role 'admin', binding permissions '1001, 1002'
    And [Parameterization Sample] add account 'test@qq.com'

    When [Parameterization Sample] account 'test@qq.com' binding role 'admin'

    Then [Parameterization Sample] authentication account 'test@qq.com', with permission '1001'
    And  [Parameterization Sample] authentication account 'test@qq.com', with permission '1002'
    But  [Parameterization Sample] authentication account 'test@qq.com', without permission '1003'

Feature: Background 范例

  Background:
    Given [Background Sample] add account 'test@qq.com'

  Scenario: Account authorization by binding roles

    Given [Background Sample] add role 'admin', binding permissions '1001, 1002'

    When [Background Sample] account 'test@qq.com' binding role 'admin'

    Then [Background Sample] authentication account 'test@qq.com', with permission '1001'
    And  [Background Sample] authentication account 'test@qq.com', with permission '1002'
    But  [Background Sample] authentication account 'test@qq.com', without permission '1003'

  Scenario: Account authorization by binding organizations

    Given [Background Sample] add organization 'dev', binding permissions '2001, 2002'

    When [Background Sample] account 'test@qq.com' binding organization 'dev'

    Then [Background Sample] authentication account 'test@qq.com', with permission '2001'
    And  [Background Sample] authentication account 'test@qq.com', with permission '2002'
    But  [Background Sample] authentication account 'test@qq.com', without permission '2003'

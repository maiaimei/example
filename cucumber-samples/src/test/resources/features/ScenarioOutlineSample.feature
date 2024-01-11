Feature: Scenario Outline 范例。可以在多个 Step 上共用同一个 "简单" 参数，且每一个 Example 都视为一个 Scenario。

  Scenario Outline: Account authorization by binding roles

    Given [Scenario Outline Sample] add role <role> binding permissions <permissions>

    When [Scenario Outline Sample] add account <account>
    And [Scenario Outline Sample] account <account> binding role <role>

    Then [Scenario Outline Sample] authentication account <account>, with permission <has_permission>

    Examples:
      | role | permissions      | account    | has_permission |
      | 分析师  | 1001, 1002, 1003 | abc@qq.com | 1001           |
      | 开发者  | 2001, 2002, 2003 | cde@qq.com | 2001           |
      | 管理员  | 3001, 3002, 3003 | fgh@qq.com | 3001           |

Feature: Chrome Sample

  Scenario Outline: Baidu Search
    Given open baidu
    When input keyword <keyword>
    Then get result

    Examples:
      | keyword |
      | 全职法师    |
      | 炼气十万年   |

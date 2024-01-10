Feature: Baidu Search

  Scenario: Simple Search
    Given open baidu
    When input keyword <keyword>
    Then get result

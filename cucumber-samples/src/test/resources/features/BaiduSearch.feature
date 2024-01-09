Feature: Search

  Scenario: Search
    Given open baidu
    When input key word <keyword>
    Then show content

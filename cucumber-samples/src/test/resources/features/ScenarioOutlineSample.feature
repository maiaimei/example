Feature: Scenario Outline Sample

  Scenario: Account authorization by binding roles

    Given add role <role>
    And add permissions <permissions>
    And add account <account>

    When role <role> binding permissions <permissions>
    And account <account> binding role <role>

    Then authentication account <account>, With permission <has_permission>

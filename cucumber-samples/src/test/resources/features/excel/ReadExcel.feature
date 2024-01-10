Feature: User is using an excel spreadsheet with cucumber driving it

  Scenario Outline: Data Driven with excel and data sets

    When I am on the mainscreen
    Then I input username and password with excel row<row_index> dataset

    Examples:
      | row_index |
      | 1         |
      | 2         |
      | 3         |
      | 4         |

Feature: 列表化

  Scenario: [直列表] Authorization

    Given [直列表] delete account
      | waiting001@qq.com |
      | waiting002@qq.com |
      | waiting003@qq.com |

  Scenario: [横列表] Authorization

    Given [横列表] delete account
      | waiting001@qq.com | waiting002@qq.com | waiting003@qq.com |

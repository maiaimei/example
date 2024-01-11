Feature: 对象化

  Scenario: [对象化] Authorization

    Given [对象化] add account
      | username        | role      | project    |
      | waiting1@qq.com | admin     | default    |
      | waiting2@qq.com | analyst   | production |
      | waiting3@qq.com | developer | default    |

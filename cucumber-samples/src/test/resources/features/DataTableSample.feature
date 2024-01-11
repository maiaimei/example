Feature: 表格化（DataTable）

  Scenario: [List - Map（常用）] Authorization

    Given [List - Map（常用）] add account
      | username          | password | role      | project_id |
      | waiting001@qq.com | a123456  | admin     | 1          |
      | waiting002@qq.com | b123456  | analyst   | 2          |
      | waiting003@qq.com | c123456  | developer | 3          |

  Scenario: [List - List] Authorization

    Given [List - List] add account
      | username          | password | role      | project_id |
      | waiting001@qq.com | a123456  | admin     | 1          |
      | waiting002@qq.com | b123456  | analyst   | 2          |
      | waiting003@qq.com | c123456  | developer | 3          |

  Scenario: [Map - List] Authorization

    Given [Map - List] add account
      | username          | password | role      | project_id |
      | waiting001@qq.com | a123456  | admin     | 1          |
      | waiting002@qq.com | b123456  | analyst   | 2          |
      | waiting003@qq.com | c123456  | developer | 3          |
    

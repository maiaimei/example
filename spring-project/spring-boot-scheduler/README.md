# spring-boot-scheduler

## Cron表达式

Cron表达式是一个字符串，字符串以5或6个空格隔开，分为6或7个域，每一个域代表一个含义。

从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份

各域含义：

| 字段                   | 允许值                                 | 允许的特殊字符             |
| ---------------------- | -------------------------------------- | -------------------------- |
| 秒（Second）           | 0~59的整数                             | , - * /   四个字符         |
| 分（Minute）           | 0~59的整数                             | , - * /   四个字符         |
| 小时（Hour）           | 0~23的整数                             | , - * /   四个字符         |
| 日期（DayofMonth）     | 1~31的整数（但是你需要考虑你月的天数） | ,- * ? / L W C   八个字符  |
| 月份（Month）          | 1~12的整数或者 JAN-DEC                 | , - * /   四个字符         |
| 星期（DayofWeek）      | 1~7的整数或者 SUN-SAT （1=SUN）        | , - * ? / L C #   八个字符 |
| 年(可选，留空)（Year） | 1970~2099                              | , - * /   四个字符         |

特殊字符含义：

| 特殊字符 | 备注                                                         |
| :------: | ------------------------------------------------------------ |
|    *     | 表示匹配该域的任意值。假如在Minute域使用*, 即表示每分钟都会触发事件。 |
|    ？    | 只能用在DayofMonth和DayofWeek两个域。<br/>它也匹配域的任意值，但实际不会。<br/>因为DayofMonth和DayofWeek会相互影响。<br/>例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： <br/>13 13 15 20 * ?<br/>, 其中最后一位只能用？，而不能使用*，<br/>如果使用*表示不管星期几都会触发，实际上并不是这样。 |
|    -     | 表示范围。例如在Minute域使用5-20，表示从5分到20分钟每分钟触发一次 |
|    /     | 表示起始时间开始触发，然后每隔固定时间触发一次。<br/>例如在Minute域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次 |
|    ,     | 表示列出枚举值。例如：在Minute域使用5,20，则意味着在5和20分每分钟触发一次。 |
|    L     | 表示最后，只能出现在DayofWeek和DayofMonth域。<br/>如果在DayofWeek域使用5L,意味着在最后的一个星期四触发。 |
|    W     | 表示有效工作日(周一到周五),只能出现在DayofMonth域，<br/>系统将在离指定日期的最近的有效工作日触发事件。<br/>例如：<br/>在 DayofMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。<br/>如果5日是星期天，则在6日(周一)触发；<br/>如果5日在星期一到星期五中的一天，则就在5日触发。<br/>另外一点，W的最近寻找不会跨过月份 。 |
|    LW    | 这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。 |
|    #     | 用于确定每个月第几个星期几，只能出现在DayofMonth域。<br/>例如在4#2，表示某月的第二个星期三。 |

常用cron表达式例子

（1）0/2 * * * * ? 表示每2秒 执行任务

（1）0 0/2 * * * ? 表示每2分钟 执行任务

（1）0 0 2 1 * ? 表示在每月的1日的凌晨2点调整任务

（2）0 15 10 ? * MON-FRI 表示周一到周五每天上午10:15执行作业

（3）0 15 10 ? 6L 2002-2006 表示2002-2006年的每个月的最后一个星期五上午10:15执行作

（4）0 0 10,14,16 * * ? 每天上午10点，下午2点，4点

（5）0 0/30 9-17 * * ? 朝九晚五工作时间内每半小时

（6）0 0 12 ? * WED 表示每个星期三中午12点

（7）0 0 12 * * ? 每天中午12点触发

（8）0 15 10 ? * *    每天上午10:15触发

（9）0 15 10 * * ? 每天上午10:15触发

（10）0 15 10 * * ? 每天上午10:15触发

（11）0 15 10 * * ? 2005 2005年的每天上午10:15触发

（12）0 * 14 * * ? 在每天下午2点到下午2:59期间的每1分钟触发

（13）0 0/5 14 * * ? 在每天下午2点到下午2:55期间的每5分钟触发

（14）0 0/5 14,18 * * ? 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发

（15）0 0-5 14 * * ? 在每天下午2点到下午2:05期间的每1分钟触发

（16）0 10,44 14 ? 3 WED 每年三月的星期三的下午2:10和2:44触发

（17）0 15 10 ? * MON-FRI 周一至周五的上午10:15触发

（18）0 15 10 15 * ? 每月15日上午10:15触发

（19）0 15 10 L * ? 每月最后一日的上午10:15触发

（20）0 15 10 ? * 6L 每月的最后一个星期五上午10:15触发

（21）0 15 10 ? * 6L 2002-2005 2002年至2005年的每月的最后一个星期五上午10:15触发

（22）0 15 10 ? * 6#3 每月的第三个星期五上午10:15触发

[在线Cron表达式生成器](https://cron.qqe2.com/)

## Java时区

Java中常用的zoneId 有2种格式:

1.时区偏移量的形式：GMT+8

2.区域的形式：Asia/Shanghai（常用）

## ISO 8601日期格式与持续时间格式

### ISO 8601日期格式

ISO 8601日期格式如下：

```
YYYY-MM-DDThh:mm:ss[.mmm]TZD
```

```
YYYY表示四位数的年份
MM表示两位数的月份
DD表示两位数的天(day of the month)，从01到31
T是用来指示时间元素的开始字符
hh表示两位数的小时，从00到23，不包括AM/PM
mm表示两位数的分钟，从00到59
ss表示两位数的秒，从00到59
mmm表示三位数的毫秒数，从000到999
TZD表示时区指示符：Z或+hh:mm或-hh:mm，+或-表示时区距离UTC(世界标准时间)时区多远。例如：
CST(中国标准时间)：UTC +08:00，EST(东部标准时间)：UTC -05:00，CST(中部标准时间)：UTC -06:00。
```

例如：2012-03-29T10:05:45-06:00

表示：中部标准时间2012年3月29日10:05:45。

### ISO 8601持续时间格式

ISO 8601持续时间格式如下：

```
P(n)Y(n)M(n)DT(n)H(n)M(n)S
```

```
P is the duration designator (for period) placed at the start of the duration representation.
Y is the year designator that follows the value for the number of years.
M is the month designator that follows the value for the number of months.
W is the week designator that follows the value for the number of weeks.
D is the day designator that follows the value for the number of days.
T is the time designator that precedes the time components of the representation.
H is the hour designator that follows the value for the number of hours.
M is the minute designator that follows the value for the number of minutes.
S is the second designator that follows the value for the number of seconds. 
```

PT20S的值然后解析为：期间 时间 20 秒，所以，持续时间为 20 秒。

[https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-)

## 分布式定时任务锁SchedulerLock

ShedLock的实现原理是采用公共存储实现的锁机制，确保任务在同一时刻最多执行一次。如果一个任务正在一个节点上执行，则它将获得一个锁，该锁将阻止从另一个节点（或线程）执行同一任务。如果一个任务已经在一个节点上执行，则在其他节点上的执行不会等待，只需跳过它即可 。

[SpringBoot 集成 ShedLock (基于mysql)](https://blog.csdn.net/qq_43949280/article/details/122845553)

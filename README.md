# SSM
#SSM 框架整合及创建一个高并发的秒杀系统
遇到的坑：
1.spring-webmvc包（4.1.7.RELEASE版本）与某个包（不清楚是哪个）冲突，导致tomcat 启动不了。
    解决办法：将spring-webmvc包换成4.1.9.RELEASE版本（或其它不冲突的版本）
    
2.logback-classic包在启动tomcat时会打印出很多EORRO但不影响程序运行。（未解决，有强迫症，只能把logback-classic包在pom.xml里注掉，但这样就没了日志输出）

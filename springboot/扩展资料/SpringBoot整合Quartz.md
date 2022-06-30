# Spring Boot整合 Quartz 

## 1. Quartz (了解)

Quartz是一个开源的作业调度框架，它完全由Java写成，并设计用于J2SE和J2EE应用中。它提供了巨大的灵 活性而不牺牲简单性。你能够用它来为执行一个作业而创建简单的或复杂的调度。

Quartz  的使用思路

~~~
1）job - 任务 - 你要做什么事？
2）Trigger - 触发器 - 你什么时候去做？
3）Scheduler - 任务调度 - 你什么时候需要去做什么事？
~~~

## 2. Quartz的基本使用

### 2.1 引入依赖

~~~
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
</dependency>
~~~

### 2.2 创建Job任务类

~~~
package com.woniu.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class QuartzDemo implements Job {
    /**
     * 任务被触发时所执行的方法
     */
    public void execute(JobExecutionContext arg0) throws
            JobExecutionException {
        System.out.println("Execute...." + new Date());
    }
}
~~~

### 2.3测试

~~~
package com.woniu;
import com.woniu.schedule.QuartzDemo;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {
    public static void main(String[] args) throws Exception {
        // 1.创建 Job 对象：你要做什么事？
        JobDetail job = JobBuilder.newJob(QuartzDemo.class).build();
       // 2.创建 Trigger 对象：在什么时间做？
        Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build();
        // 3.创建 Scheduler 对象：在什么时间做什么事？
        Scheduler scheduler =  StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(job, trigger);
         //启动
        scheduler.start();
    }
}
~~~

## 3. springboot整合Quartz

### 3.1 修改pom.xml

~~~
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
 <dependency>
     <groupId>org.quartz-scheduler</groupId>
     <artifactId>quartz</artifactId>
     <version>2.2.1</version>
     <exclusions>
         <exclusion>
             <artifactId>slf4j-api</artifactId>
             <groupId>org.slf4j</groupId>
         </exclusion>
     </exclusions>
 </dependency>
 <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-context-support</artifactId>
 </dependency>
~~~

### 4.2 创建配置类QuartzConfig

~~~
package com.woniu.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {
    /**
     * 1.创建 Job 对象
     */
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        //关联我们自己的 Job 类
        factory.setJobClass(QuartzDemo.class);
        return factory;
    }

    /**
     * 2.创建 Trigger 对象
     * 简单的 Trigger
     */
    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean) {
        SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
         //关联 JobDetail 对象
        factory.setJobDetail(jobDetailFactoryBean.getObject());
        //该参数表示一个执行的毫秒数
        factory.setRepeatInterval(2000);
        //重复次数
        factory.setRepeatCount(5);
        return factory;
    }

    /**
     * 3.创建 Scheduler 对象
     */
    @Bean
    public SchedulerFactoryBean
    schedulerFactoryBean(SimpleTriggerFactoryBean simpleTriggerFactoryBean) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //关联 trigger
        factory.setTriggers(simpleTriggerFactoryBean.getObject());
        return factory;
    }
}
~~~

### 4.3 创建QuartzDemo

```
public class QuartzDemo implements Job {
    /**
     * 任务被触发时所执行的方法
     */
    public void execute(JobExecutionContext arg0) throws
            JobExecutionException {
        System.out.println("Execute...." + new Date());
    }
}
```

### 4.4 修改启动类

​    在启动类添加@EnableScheduling

~~~
package com.woniu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserApp {

    public static void main(String[] args) {
        SpringApplication.run(UserApp.class,args);
    }
}
~~~




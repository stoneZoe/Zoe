package com.woniu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class SpringbootRedisDemoApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void contextLoads() {
       /* BoundValueOperations<String, String> ops = redisTemplate.boundValueOps("hello");
        ops.set("world");*/

      /*  BoundHashOperations<String, String, String> opsHash = redisTemplate.boundHashOps("user");
        opsHash.put("name","张三丰");
        opsHash.put("age","20");
        opsHash.increment("age",20);*/


        HashOperations<String, Object, Object> ops2 = redisTemplate.opsForHash();
        ops2.put("user1","name","虎大");
        ops2.put("user1","age","20");
        //HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
    }

}

import com.keymao.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {
    @Test
    public  void testJedisCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.27.106",7001));
        nodes.add(new HostAndPort("192.168.27.106",7002));
        nodes.add(new HostAndPort("192.168.27.106",7003));
        nodes.add(new HostAndPort("192.168.27.106",7004));
        nodes.add(new HostAndPort("192.168.27.106",70015));
        nodes.add(new HostAndPort("192.168.27.106",7006));
        JedisCluster jedisCluster  = new JedisCluster(nodes);

        jedisCluster.set("hello","xiaojm");
        String str = jedisCluster.get("hello");
        System.out.println(str);

        jedisCluster.del("hello");
        jedisCluster.close();
    }

    @Test
    public void testJedisClient() throws Exception {
        //初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //从容器中获得JedisClient对象
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("first", "100");
        String result = jedisClient.get("first");
        System.out.println(result);


    }

}

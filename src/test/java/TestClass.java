import org.junit.Test;

import me.redis.RedisConnector;

public class TestClass {
	
	
	@Test
	public void connectionTest(){
		RedisConnector.start();
	}
	
}

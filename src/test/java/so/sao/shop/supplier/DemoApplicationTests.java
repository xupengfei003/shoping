package so.sao.shop.supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import so.sao.shop.supplier.config.Constant;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Test
	public void contextLoads() {
		redisTemplate.delete(Constant.REDIS_KEY_PREFIX + "LOGISTICS_AUTOMATIC_RECEIVE");
	}
	@Test
	public void compareDate() throws ParseException {
		String ss = "2017-10-20 17:07:25";
		Date first = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*System.out.println(System.currentTimeMillis());
		System.out.println("++++++++++++++++++++++++++++");
		System.out.println(new Date().getTime());
		System.out.println("++++++++++++++++++++++++++++");
		System.out.println(sf.parse(ss).getTime());
		System.out.println("++++++++++++++++++++++++++++");
		System.out.println(System.currentTimeMillis() == new Date().getTime());*/

		BigDecimal a = new BigDecimal(10.00);
		Integer b = 3;
		System.out.println(a.divide(new BigDecimal(b),2)+"======");
		System.out.println(BigDecimal.valueOf(0.01));

	}
}

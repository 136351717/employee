package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ����spring��junit����,��Junit����ʱ����springioc���� spring-test,junit
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillDaoTest {

	// ע��DAOʵ����
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testQueryById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
		/*
		 * 1000��ɱiphone7 Seckill [seckillId=1000, name=1000��ɱiphone7,
		 * number=100, startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov
		 * 02 00:00:00 CST 2015, createTime=Thu Jan 05 16:08:53 CST 2017]
		 */
	}

	@Test
	public void testQueryAll() throws Exception {
		List<Seckill> seckills = seckillDao.queryAll(0, 100);
		for (Seckill seckill : seckills) {
			System.out.println(seckill);
		}
		/**
		 * Seckill [seckillId=1000, name=1000��ɱiphone7, number=100,
		 * startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00
		 * CST 2015, createTime=Thu Jan 05 16:08:53 CST 2017] Seckill
		 * [seckillId=1001, name=500��ɱipad2, number=200, startTime=Sun Nov 01
		 * 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00 CST 2015,
		 * createTime=Thu Jan 05 16:08:53 CST 2017] Seckill [seckillId=1002,
		 * name=300��ɱС��4, number=300, startTime=Sun Nov 01 00:00:00 CST 2015,
		 * endTime=Mon Nov 02 00:00:00 CST 2015, createTime=Thu Jan 05 16:08:53
		 * CST 2017] Seckill [seckillId=1003, name=200��ɱ����note, number=400,
		 * startTime=Sun Nov 01 00:00:00 CST 2015, endTime=Mon Nov 02 00:00:00
		 * CST 2015, createTime=Thu Jan 05 16:08:53 CST 2017]
		 */
	}
	
	@Test
	public void testReduceNumber() throws Exception {
		Date killTime = new Date();
		int updateCount = seckillDao.reduceNumber(1000L, killTime);
		System.out.println("updateCount=" + updateCount);
	}
	/**
	 * update seckill set number=number-1 
	 * where seckill_id=? and start_time <= ? and end_time >= ? and number>0; 
	 */

}

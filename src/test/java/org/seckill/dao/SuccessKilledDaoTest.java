package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ����spring��junit����,��Junit����ʱ����springioc���� spring-test,junit
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SuccessKilledDaoTest {

	// ע��DAOʵ����
	@Autowired
	private SuccessKilledDao successKilledDao;

	@Test
	public void testInsertSuccessKilled() throws Exception {
		long id = 1001L;
		long phone = 1230123123L;
		int insertCount = successKilledDao.insertSuccessKilled(id, phone);
		System.out.println("insertCount:" + insertCount);
		/**
		 * ���еڶ���ʱ���ؽ����0 ��һ����1 ��ΪID��PHONE����Ϊ���������� ���Եڶ��������ݿ��в���һ����¼��ʱ�� �ᷢ�������ظ��Ĵ���
		 */
	}

	@Test
	public void testQueryByIdWithSeckill() throws Exception {
		long id = 1001L;
		long phone = 1230123123L;
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,
				phone);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
		/**
		 * SuccessKilled [
		 * seckillId=1000, 
		 * userPhone=1230123123, 
		 * state=-1,
		 * createTime=Thu Jan 05 23:02:46 CST 2017]
		 *  
		 * Seckill [
		 * seckillId=1000,
		 * name=1000��ɱiphone7, 
		 * number=100, 
		 * startTime=Sun Nov 01 00:00:00 CST 2015, 
		 * endTime=Mon Nov 02 00:00:00 CST 2015, 
		 * createTime=Thu Jan 05* 16:08:53 CST 2017]
		 */
	}

}

package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	/**
	 * ����һ���ɹ���ɱ�ļ�¼
	 * @param seckillId	��ɱ��ƷID
	 * @param userPhone	��ɱ�ɹ��û��绰
	 * @return 	���ص������ݿ�ĸ������� ����Ӧ�ø���һ����¼  ������Ϊĳ��ԭ�����������0,����������ɱ��¼ʧ��,���Ը����߼�ִ���ض��Ĳ���
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * ������ƷID��ѯһ���ɹ���ɱ����Ʒ�����Լ��û��ɹ���ɱ��¼
	 * @param seckillId ��ƷID
	 * @return	����һ����ɱ��¼	ͬʱSuccessKilled���滹����Ʒ���������,������ζ��ͬʱ�ܷ��سɹ���¼����Ʒ����
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}

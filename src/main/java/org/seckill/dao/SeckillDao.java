package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	
	/**
	 * �����ķ���
	 * @param seckillId ��ƷID
	 * @param killTime	��ɱʱ��
	 * @return	���ص������ݿ�ĸ������� ����Ӧ�ø���һ����¼  ������Ϊĳ��ԭ�����������0,�����������ʧ��,���Ը����߼�ִ���ض��Ĳ���
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	
	/**
	 * ͨ��ID��ѯ�����Ʒ����
	 * @param seckillId 
	 * @return ��Ʒ����
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * ����ƫ��������ȫ������Ʒ�б�
	 * @param offset	ƫ����
	 * @param limit	��ѯ������
	 * @return	��Ʒ�б�
	 */
	//������ʽ��������Ҫ����mybatis��ע��������DAOʵ������ʶ�����Ӧ����ʽ����
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	
	
	/**
	 * ʹ�ô洢����ִ����ɱ����
	 * @param paramMap
	 */
	void killByProcedure(Map<String,Object> paramMap);
	
	
}

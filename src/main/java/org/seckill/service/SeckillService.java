package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * ��Ʒ���service�ӿ�
 * @author SJW
 *
 */

public interface SeckillService {
	
	/**
	 * ��ѯ���п���ɱ��Ʒ����
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * ����ID��ѯ��������ɱ��Ʒ
	 * @return
	 */
	Seckill getById(long seckillId);
	
	/**
	 * ������ƷID��ѯ������Ʒ����ɱ����:�����ɱ��ʼ�����һ����ɱ��ַ
	 * �����ɱ��û�е�����ʱ��������ǰʱ�����ɱʱ��
	 * �������ͷ�װ��һ���洢��ɱ��Ϣ����
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * ִ����ɱ����
	 * @param seckillId
	 * @param userPhone
	 * @param md5 ��֤֮ǰ�õ���MD5��ִ����ɱ������MD5�Ƿ�һ��,���һֱ��������ִ����ɱ����
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5)
			throws SeckillException,RepeatKillException,SeckillCloseException;
	
	
	
	/**
	 * ִ����ɱ����  ͨ���洢���̵�����ɱ����
	 * @param seckillId
	 * @param userPhone
	 * @param md5 ��֤֮ǰ�õ���MD5��ִ����ɱ������MD5�Ƿ�һ��,���һֱ��������ִ����ɱ����
	 */
	SeckillExecution executeSeckillProcedure(long seckillId,long userPhone,String md5);
}

package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
@Service
public class SeckillServiceImpl implements SeckillService{
	//��ֵ,����MD5�õ�
	String slat = "sdfsf1@#RET#$%^$%GWEAWE";
	
	//ע��redisdao���ڻ���exportSeckillUrl����
	@Autowired
	private RedisDao redisDao;
	
	//��־
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//��Ҫ���õ�DAOע��
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	
	public Exposer exportSeckillUrl(long seckillId) {
		//�����û��ȵ���ɱ����ʱ����Ҫ�ȴ�����ӿڵı�¶,����ӿڵı�¶ֻ�ǲ�һ����ɱ��ַ,���Կ��Խ������뵽������,
		//����һ���û�ͨ�����ݿ�õ���¶��ַ��,��¶��ַ���뻺����,�����Ժ���û���Ҫ�õ������ַ����Ҫ�������ݿ�Ϳ����ڻ����еõ���
		
		//�Ż���1:�����Ż�
		//��һ��,����redis
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(seckill==null){
			//����ID��ѯ��ɱ��Ʒ�Ƿ����
			//�ڶ���,���redis��Ѱ�ҵ�seckill�ǿ�,ֱ��ȥ���ݿ��в��Ҷ���
			seckill = seckillDao.queryById(seckillId);
			//�ж���ɱ��Ʒ�Ƿ�Ϊ��,���Ϊ�ո���һ����¶����,��Ϊ�յĻ������ж��Ƿ��ڿ���ɱʱ�����
			if(seckill == null){
				return new Exposer(false,seckillId);
			}else{
				//���ݿ����ҵ��������ŵ�redis����
				redisDao.putSeckill(seckill);
			}
		}
		
		//�����Ʒ��Ϊ��,�жϵ�ǰϵͳʱ���Ƿ�����ɱ����ʱ�����
		Date now = new Date();
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		if(now.getTime()<startTime.getTime() || now.getTime()>endTime.getTime()){
			return new Exposer(false,seckillId,now.getTime(),startTime.getTime(),endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true,md5,seckillId);
	}
	
	//ת���ض��ַ�����һ������������㷨,������
	private String getMD5(long seckillId){
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	//ִ����ɱҵ������
	/**
	 * ʹ��ע��������񷽷����ŵ�:
	 * 1.�����Ŷӿ��Դ��һ�µ�Լ��,��ȷ��ע���񷽷��ı�̷��
	 * 2.��֤���񷽷���ִ��ʱ�価���ܵĶ�,��Ҫ����һЩ�������������,���һ����Ҫ�Ļ����뵽���񷽷�֮��
	 * 3.�������е�ҵ����Ҫ����,����ֻ��һ���޸Ĳ�����ҵ�����һЩ��ѯҵ��,��ע���񷽷����Ա����漰������Ҫ����ķ����ϵĶ�������
	 */
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeatKillException,
			SeckillCloseException {
		
		//�ж�MD5ֵ�Ƿ�����
		if(md5==null || !md5.equals(getMD5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		//���MD5ֵ��ȷ�Ļ���ִ�м�������
		Date nowTime = new Date();
		
		
		//Դ�����˳�����仯
		//ԭ�����ȼ�����ٲ��빺����ϸ
		//�����Ȳ��빺����ϸ,Ȼ���ټ����,��Ϊ������Ƕ�һ�����ݵľ���,����ʱ������м���,��һ���̲��ܲ���
		//�����빺����ϸ���Բ���ִ��,���ڼ����ǰ��ִ�е�Ŀ���Ǽ����м����ĳ���ʱ��,�����������û���ɱ�Ĳ���ִ��ʱ������
		//���е������ӳٺ�gc������ʱ�������һ��
		try {
			//������������ɹ�����ִ�в��빺����ϸ����
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			//�ж�Ӱ�������Ƿ�Ϊ0,���Ϊ0˵���û��ظ���ɱ����,ֱ���׳��쳣
			if(insertCount<=0){
				throw new RepeatKillException("repeat seckill");
			}else{
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				//�ж�Ӱ�������Ƿ�Ϊ0,���Ϊ0˵����ɱ�Ѿ��������߿�治��,ֱ���׳�һ����ɱ�����쳣
				if(updateCount<=0){
					throw new SeckillCloseException("seckill over");
				}else{
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
				}
			}
		} catch (SeckillCloseException e1){
			throw e1;
		} catch (RepeatKillException e2){
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//���б���ʱ�쳣ת��Ϊ����ʱ�쳣,��Ϊspringʶ�������ʱ�쳣ʱ����ִ��rollback����
			throw new SeckillException("seckill inner error" + e.getMessage());
		}
		
	}
	
	
	
	//ͨ���洢���̵���ִ����ɱ����
	//֮ǰ���������Ҫ�׳�����쳣,Ŀ���Ǹ���spring������ʽ����ʲô�߼���rooback,ʲô�߼���commit,
	//��Ϊspringmvc��ͨ������ʱ�쳣�����������commit��rollback��,���ڵķ����ǵ��ô洢����ִ����ɱʵ��,�Ѿ�����Ҫ
	//�ڱ���ͨ��springmvc������������,������Щ�쳣���Բ������׳���
	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		//�ж�MD5
		if(md5==null || !md5.equals(getMD5(seckillId))){
			return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
		}
		//��ȡһ��ϵͳʱ��
		Date killTime = new Date();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		//��map���ݲ�����Ŀ���Ǵ���result,���ô洢���̵õ������,�����ٴ�map��ȡ��result,��ʱ��result�Ѿ�����ֵ�ɹ�
		try {
			seckillDao.killByProcedure(map);
			//��ȡresult
			int result = MapUtils.getInteger(map, "result", -2);
			if(result==1){
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
			}else{
				return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
		}
	}
	/*
	org.springframework.dao.TransientDataAccessResourceException: 
		### Error querying database.  Cause: java.sql.SQLException: Parameter number 4 is not an OUT parameter
		### The error may exist in file [F:\Java2EEWorkSpace\seckill\target\classes\mapper\SeckillDao.xml]
		### The error may involve org.seckill.dao.SeckillDao.killByProcedure
		### The error occurred while executing a query
		### SQL: call execute_seckill(    ?,    ?,    ?,    ?   )
		### Cause: java.sql.SQLException: Parameter number 4 is not an OUT parameter
		; SQL []; Parameter number 4 is not an OUT parameter; nested exception is java.sql.SQLException: Parameter number 4 is not an OUT parameter
*/
}

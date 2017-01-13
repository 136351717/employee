--��ɱִ�еĴ洢����
DELIMITER $$ -- console ; ת��Ϊ $$
--����洢����
--������ in���������out�������
--row_count():������һ���޸����ͣ�delete,insert,update)��Ӱ������
--row_count:  0����ʾδ�޸����ݣ�>0����ʾ�޸ĵ�������<0:sql����/δִ���޸�sql
CREATE PROCEDURE `seckill`.`execute_seckill`
	(in v_seckill_id bigint,in v_phone bigint,
	 in v_kill_time timestamp,out r_result int)
	 BEGIN
		 DECLARE insert_count int DEFAULT 0;
		 START TRANSACTION;
		 insert ignore into success_killed
		  (seckill_id,user_phone,create_time)
		 value (v_seckill_id,v_phone,v_kill_time); 
		 select row_count() into insert_count;
		 IF (insert_count=0) THEN
		   ROLLBACK;
		   set r_result = -1;
		 ELSEIF(insert_count<0) THEN
		   ROLLBACK;
		   set r_result = -2;
		 ELSE
		   update seckill
		   set number = number-1
		   where seckill_id=v_seckill_id
		   and v_kill_time > start_time
		   and v_kill_time < end_time
		   and number > 0;
		   select row_count() into insert_count;
		   IF (insert_count = 0 ) THEN
		     ROLLBACK;
		     set r_result = 0;
		   ELSEIF (insert_count <0) THEN
		     ROLLBACK;
		     set r_result = -2;
		   ELSE
		     COMMIT;
		     set r_result = 1;
		   END IF;
	    END IF;
	 END;
$$
--�洢���̶������

DELIMITER;

set @r_result=-3
--ִ�д洢����
call execute_seckill(1001,15764210366,now(),@r_result);

select @r_result;

--�洢����
--1.�洢�����Ż��������м������е�ʱ��
--2.��Ҫ���������洢����
--3.�򵥵��߼�����Ӧ�ô洢����
--4.QPS һ����ɱ��6000/QPS
--QPS:ÿ���ѯ��QPS�Ƕ�һ���ض��Ĳ�ѯ�������ڹ涨ʱ�����������������ٵĺ�����׼��
	 
	 
	 
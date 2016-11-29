--执行秒杀存储过程
DELIMITER $$ --console ; 转换为 $$
--定义存储过程
--参数：in：输入参数；out：输出参数（可以赋值）
-- row_count():返回上一条修改类型的（delete，update，insert）sql的影响行数
CREATE PROCEDURE 'seckill'.'execute_seckill2'
	(in v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp,out r_result int )
   BEGIN
	   DECLARE insert_count int DEFAULT 0;
	   START TRANSACTION;
	   insert ignore into success_killed
	   			(seckill_id,user_phone,create_time)
	   values(v_seckill_id,v_phone,v_kill_time)		
	   select row_count() into insert_count;
	   IF (insert_count = 0) THEN　
	   ROLLBACK;
	   SET　r_result　= -1;
	   ELSEIF (insert_count < 0) THEN
	   ROLLBACK;
	   SET r_result = -2;
	   
	   ELSE
	    UPDATE seckill
	    SET number=number-1;
	    WHERE seckill_id=v_seckill_id
	          and end_time > v_kill_time
	          and start_time < v_kill_time
	          and number > 0;
	   select row_count() into insert_count;
	   IF (insert_count = 0) THEN　
	   ROLLBACK;
	   SET　r_result　= 0;
	   ELSEIF (insert_count < 0) THEN
	   ROLLBACK;
	   SET r_result = -2;
	   ELSE
	   COMMIT ;
	   SET r_result = 1;
	   END IF;
	  END IF;
   END;
   
$$

--存储过程定义结束
DELIMITER $$;
SET  @r_result=-3;
call execute_seckill(1,18766676677,now(),@r_result)

--获取结果
select @r_result;

--1.存储过程优化：事务行级锁持有时间
--2.不要过度依赖存储过程
--3.简单的逻辑可以应用存储过程
--4.QPS一个秒杀单6000/qps

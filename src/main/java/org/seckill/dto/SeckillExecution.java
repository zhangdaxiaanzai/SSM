package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

/**
 * ��װִ����ɱ�󷵻صĽ��
 * @author admin
 *
 */
public class SeckillExecution {
	
	private long seckillId;
	
	//��ɱִ�н��״̬
	private int state;
	
	//״̬��ʾ
	private String stateInfo;
	
	//��ɱ�ɹ�����
	private SuccessKilled successKilled;
	
	
	

	public SeckillExecution(long seckillId,SeckillStatEnum stetEnum , SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.state = stetEnum.getState();
		this.stateInfo = stetEnum.getStateInfo();
		this.successKilled = successKilled;
	}
	
	

	public SeckillExecution(long seckillId, SeckillStatEnum stetEnum) {
		super();
		this.seckillId = seckillId;
		this.state = stetEnum.getState();
		this.stateInfo = stetEnum.getStateInfo();
	}



	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state + ", stateInfo=" + stateInfo
				+ ", successKilled=" + successKilled + "]";
	}



	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	
	
	
}

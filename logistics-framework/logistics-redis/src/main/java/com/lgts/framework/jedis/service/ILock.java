package com.lgts.framework.jedis.service;

/**
 * 定义锁
 * @author hry
 *
 */
public interface ILock {
	/**
	 * 获取锁
	 * @param lock 锁名称
	 */
	boolean lock(String lock);
	
	/**
	 * 释放锁
	 * @param lock 锁名称
	 */
	boolean unlock(String lock);

	/**
	 * 尝试加锁
	 * @param lock
	 * @return
	 */
	boolean tryLock(String lock);


}

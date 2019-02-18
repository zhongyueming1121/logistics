package com.lgts.framework.jedis.lock;

import com.lgts.framework.jedis.service.ILock;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 通过redis实现分布锁
 * @author ymz
 * @date 2018/9/15 01:21
 *
 */
public class LuaLock implements ILock {
	private Jedis jedis;
	/**
	 * 做为锁key的前缀
	 */
	private static String lockPrex;
	/**
	 * 单位s，一个线程持有锁的最大时间
	 */
	private static int lockMaxExistTime;

	private static final String LOCK_SCRIPT =
					"local key     = KEYS[1];\n" +
					"local content = KEYS[2];\n" +
					"local ttl     = ARGV[1];\n" +
					"local lockSet = redis.call('setnx', key, content);\n" +
					"if lockSet == 1 then\n" +
					"  redis.call('pexpire', key, ttl) \n" +
					"else \n" +
					"  local value = redis.call('get', key) \n" +
					"  if(value == content) then\n" +
					"    lockSet = 1;\n" +
					"    redis.call('pexpire', key, ttl)\n" +
					"  end\n" +
					"end\n" +
					"return lockSet";

	private static final String UNLOCK_SCRIPT =
			"local key     = KEYS[1];\n" +
			"local content = KEYS[2];\n" +
			"local value = redis.call('get', key);\n" +
			"if value == content then\n" +
			"  return redis.call('del', key);\n" +
			"end\n" +
			"return 0";

	/**
	 * 线程变量
	 */
	private static ThreadLocal<String> threadKeyId = ThreadLocal.withInitial(()->UUID.randomUUID().toString());

	LuaLock(Jedis jedis, String lockPrex, int lockMaxExistTime){
		this.jedis = jedis;
		LuaLock.lockPrex = lockPrex;
		LuaLock.lockMaxExistTime = lockMaxExistTime;
	}

	/**
	 * 加锁
	 * @param lock 锁名称，会转换成小写
	 */
	@Override
	public boolean lock(String lock){
		Random random = new Random();
		Assert.notNull(lock, "lock can't be null!");
		String lockKey = getLockKey(lock);
		while(true){
			List<String> keys = new ArrayList<String>();
			List<String> avgs = new ArrayList<>();
			String k2 = threadKeyId.get();
			String av1 = String.valueOf(lockMaxExistTime * 1000);
			keys.add(lockKey);
			keys.add(k2);
			avgs.add(av1);
			// 1成功 0失败
			Long result = (Long) jedis.eval(LOCK_SCRIPT,keys,avgs);
			if(result!=null&&result> 0){
				return true;
			}else{
				try {
					// 短暂休眠，nano避免出现活锁 
					Thread.sleep(10, (random.nextInt(100)*500));
				} catch (InterruptedException e) {
					return false;
				}
			}
		}
	}

	/**
	 * 尝试获得锁
	 * @param lock
	 * @return
	 */
	@Override
	public boolean tryLock(String lock) {
		Assert.notNull(lock, "lock can't be null!");
		String lockKey = getLockKey(lock);
		List<String> keys = new ArrayList<String>();
		keys.add(lockKey);
		List<String> avgs = new ArrayList<>();
		String k2 = threadKeyId.get();
		String av1 = String.valueOf(lockMaxExistTime * 1000);
		keys.add(k2);
		avgs.add(av1);
		// 1成功 0失败
		Long result = (Long) jedis.eval(LOCK_SCRIPT,keys,avgs);

		if(result!=null&&result> 0){
			return true;
		}
		return false;
	}
	

	/**
	 * 释放锁，同时要考虑当前锁是否为自己所有，以下情况会导致当前线程失去锁：线程执行的时间超过超时的时间，导致此锁被其它线程拿走; 此时用户不可以执行删除
	 * @param lock 锁名称，会转换成小写
	 */
	@Override
	public boolean unlock(String lock) {
		Assert.notNull(lock, "lock can't be null!");
		String lockKey = getLockKey(lock);
		List<String> keys = new ArrayList<String>();
		String k2 = threadKeyId.get();
		keys.add(lockKey);
		keys.add(k2);
		List<String> avgs = new ArrayList<>();
		try {
			// 1成功 0失败
			Long result = (Long) jedis.eval(UNLOCK_SCRIPT,keys,avgs);
			if(result!=null&&result>0) {
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally {
			try {
				threadKeyId.remove();
			}catch (Exception e) {

			}
		}
		return false;
	}

	private String getLockKey(String lock){
		return lockPrex.toUpperCase()+"_"+lock;
	}

	private String luaStr(String key) {
		return "'"+key+"'";
	}
	
}

package com.gz.medicine.ftpUtil;

import com.gz.medicine.common.util.PropertyUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by dlf on 2017/8/21 0021.
 * 创建ftp连接池
 */
public class FTPClientPool implements ObjectPool<Ftp> {
    public static final Logger LOGGER = Logger.getLogger(FTPClientPool.class);

    private static final int DEFAULT_POOL_SIZE = Integer.parseInt(PropertyUtil.getPropery("DEFAULT_POOL_SIZE"));
    private final BlockingQueue<Ftp> pool;
    private final FtpClientFactory factory;

    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param factory
     * @throws Exception
     */
    public FTPClientPool(FtpClientFactory factory) throws Exception {
        this(DEFAULT_POOL_SIZE, factory);
    }

    /**
     * @param poolSize
     * @param factory
     * @throws Exception
     */
    public FTPClientPool(int poolSize, FtpClientFactory factory) throws Exception {
        this.factory = factory;
        pool = new ArrayBlockingQueue<Ftp>(poolSize * 2);
        initPool(poolSize);
    }

    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param maxPoolSize
     * @throws Exception
     */
    private void initPool(int maxPoolSize) throws Exception {
        for (int i = 0; i < maxPoolSize; i++) {
            //往池中添加对象
            addObject();
        }

    }

    /**
     * FTP获取
     * @return
     * @throws Exception
     * @throws NoSuchElementException
     * @throws IllegalStateException
     */
    public Ftp borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        Ftp ftp = pool.take();
        if (ftp == null) {
            ftp = (Ftp) factory.makeObject();
            addObject();
        } else if (!factory.validateObject(ftp)) {//验证不通过
            //使对象在池中失效
            invalidateObject(ftp);
            //制造并添加新对象到池中
            ftp = (Ftp) factory.makeObject();
            addObject();
        }
        return ftp;

    }


    /**
     * 返回FTP
     * @param f
     * @throws Exception
     */
    public void returnObject(Ftp f) throws Exception {
        if ((f != null) && !pool.offer(f, 3, TimeUnit.SECONDS)) {
            try {
                factory.destroyObject(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void invalidateObject(Ftp f) throws Exception {
        //移除无效的客户端
        pool.remove(f);
    }

    /**
     * 加入到队列
     * @throws Exception
     * @throws IllegalStateException
     * @throws UnsupportedOperationException
     */
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
        //插入对象到队列
        pool.offer((Ftp) factory.makeObject(), 3, TimeUnit.SECONDS);
    }

    @Override
    public int getNumIdle() {
        return 0;
    }

    @Override
    public int getNumActive() {
        return 0;
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {

    }


    /**
     * 关闭
     */
    public void close() {
        while (pool.iterator().hasNext()) {
            Ftp client = null;
            try {
                client = pool.take();
                factory.destroyObject(client);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
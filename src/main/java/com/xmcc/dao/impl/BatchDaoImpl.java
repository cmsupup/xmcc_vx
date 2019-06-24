package com.xmcc.dao.impl;

import com.xmcc.dao.BatchDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    public void batchInsert(List<T> list) {
        long length = list.size();
        for (int i = 0; i <length; i++) {
            entityManager.persist(list.get(i));
            if (i % 100 == 0||i==length-1) {//每100条执行一次写入数据库操作
                entityManager.flush();
                entityManager.clear();
            }
        }

    }
}

package com.zhuyahui.util;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

/**
 * 事务参数配置类
 *
 * @author : Zhu Yahui
 * @version : 1.0.6
 * @date : 2023/1/13
 */
public class TransactionConfig {
    private String transactionManager = "";
    private Propagation propagation = Propagation.REQUIRED;
    private Isolation isolation = Isolation.READ_COMMITTED;
    private int timeout = -1;
    private String name;
    private boolean readOnly = false;

    public TransactionConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public TransactionConfig(Propagation propagation, Isolation isolation, int timeout, String transactionManager) {
        this.transactionManager = transactionManager;
        this.propagation = propagation;
        this.isolation = isolation;
        this.timeout = timeout;
    }

    public TransactionConfig(Propagation propagation, Isolation isolation) {
        this.propagation = propagation;
        this.isolation = isolation;
    }

    public TransactionConfig(Isolation isolation,boolean readOnly){
        this.isolation=isolation;
        this.readOnly=readOnly;
    }

    public String getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(String transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public Isolation getIsolation() {
        return isolation;
    }

    public void setIsolation(Isolation isolation) {
        this.isolation = isolation;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

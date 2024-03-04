package com.my.myapicommon.service;


/**
 * nonce随机数储存到redis,判断是否为请求重放
 */
public interface InnerNonceService {

    /**
     * 判断传入的nonce是否有效
     * @param nonce
     * @return
     */
    Boolean nonceJudge(String nonce);
}

package com.weixin.pay.service;

/**
 * Created by luc on 17/1/10.
 */
public interface AdminService {
    /**
     * CYJY返回今日订单
     * @param openid
     * @return
     */
    String queryTodayOrders(String openid, String createTime);

    /**
     * 绑定openid和手机号码
     * @param openid
     * @param mobile
     * @param code
     * @return
     */
    String linkOpenidWithMobile(String openid, String mobile, String code);

    /**
     * BDYZ返回当前openid绑定油站信息
     * @param openid
     * @return
     */
    String queryBDStation(String openid);

    /**
     * 通过openid获取用户的相关信息
     * 太耗时,暂不使用
     * @param openid
     * @return
     */
    String fetchNickNameByOpenid(String openid);
}

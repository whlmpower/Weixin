package com.weixin.pay.dao;

import com.weixin.pay.domain.StationInfo;
import com.weixin.pay.domain.StationList;
import com.weixin.pay.domain.generateOrder.OrderData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by luc on 16/8/3.
 */
@Repository
public class TransationDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("ucouponnamedjdbc")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplateUcoupon;

    @Autowired
    @Qualifier("ucouponjdbc")
    private JdbcTemplate jdbcTemplateUcoupon;

    private static String sql = "insert into weixin_db.pay_tb (trade, amount, fee, umoney, coupon, duration, mobile, license, station_id, transation_date)"
            + " values (:t,:a,:f,:u,:c,:dr,:m,:l,:s,:d)";

    private static final String STATUS_SQL = "INSERT INTO weixin_db.pay_status (trade_no, status) VALUES (:t, :s) ";

    private static final String QUERY_STATION = " SELECT station_name FROM weixin_db.station_info WHERE station_id = ? ";

    private static final String QUERY_STATION_UCOUPON = " SELECT station_name FROM ucoupon.station_info WHERE station_id = ? ";

    private static final String NOTIFY_COUPON_URL = " INSERT INTO wx_front_end.trasation_url_tb (out_trade_no, mobile, url_log) VALUE (?,?,?)";

    private static String INSERT_TRASATION_COUPON = " insert into wx_front_end.trasation_tb (trade, amount, fee, umoney, coupon, duration, mobile, license, station_id, transation_date)"
            + " values (:t,:a,:f,:u,:c,:dr,:m,:l,:s,:d)";

    private static String RECORD_ERROR_ORDER = "INSERT INTO wx_front_end.order_error (out_trade_no, amount, mobile) VALUES (:o, :a, :m)";

    private static String RECORD_LOCATE_SQL = "INSERT INTO wx_front_end.locate_log (openid, lng, lat, min, station_id, station_name) VALUES (:o, :lng, :lat, :m, :id, :name)";

    public int recordTransation(String out_trade_no, OrderData orderData, String time_end) {
        try {
            MapSqlParameterSource source = new MapSqlParameterSource()
                    .addValue("t", out_trade_no)
                    .addValue("a", orderData.getAmount())
                    .addValue("f", String.valueOf(Double.valueOf(orderData.getFee()) / 100))
                    .addValue("u", orderData.getUmoney_discount())
                    .addValue("c", orderData.getCoupon_discount())
                    .addValue("dr", String.valueOf(orderData.getDuration()))
                    .addValue("m", orderData.getTel())
                    .addValue("l", orderData.getLicense())
                    .addValue("s", orderData.getStation_id())
                    .addValue("d", time_end);
            return namedParameterJdbcTemplate.update(sql, source);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int recordTransation2Ucoupon(String out_trade_no, OrderData orderData, String time_end) {
        try {
            MapSqlParameterSource source = new MapSqlParameterSource()
                    .addValue("t", out_trade_no)
                    .addValue("a", orderData.getAmount())
                    .addValue("f", String.valueOf(Double.valueOf(orderData.getFee()) / 100))
                    .addValue("u", orderData.getUmoney_discount())
                    .addValue("c", orderData.getCoupon_discount())
                    .addValue("dr", String.valueOf(orderData.getDuration()))
                    .addValue("m", orderData.getTel())
                    .addValue("l", orderData.getLicense())
                    .addValue("s", orderData.getStation_id())
                    .addValue("d", time_end);
            return namedParameterJdbcTemplateUcoupon.update(INSERT_TRASATION_COUPON, source);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int recordUcouponStatus(String out_trade_no, int status){
        try {
            MapSqlParameterSource source = new MapSqlParameterSource()
                    .addValue("t", out_trade_no)
                    .addValue("s", status);
            return namedParameterJdbcTemplate.update(STATUS_SQL, source);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 记录出票失败的订单
     * 包括ucoupon后台异常
     * @param out_trade_no
     * @param amount
     * @param mobile
     * @return
     */
    public int recordErrorOrder(String out_trade_no, double amount, String mobile){
        try {
            MapSqlParameterSource source = new MapSqlParameterSource().addValue("o", out_trade_no)
                    .addValue("a", amount)
                    .addValue("m", mobile);
            return namedParameterJdbcTemplateUcoupon.update(RECORD_ERROR_ORDER, source);
        }catch (Exception e){
            return 0;
        }

    }

    public String queryForStationNameById(String stationId){
        try {
            return jdbcTemplate.queryForObject(QUERY_STATION, new Object[]{stationId}, String.class);
        }catch (Exception e){
            return "NA";
        }
    }

    public String queryForStationNameByIdFromUcoupon(String stationId){
        try {
            return jdbcTemplateUcoupon.queryForObject(QUERY_STATION_UCOUPON, new Object[]{stationId}, String.class);
        }catch (Exception e){
            return "NA";
        }
    }

    /**
     * 记录支付成功后 通知ucoupon的url
     * @param out_trade_no
     * @param mobile
     * @param url
     * @return
     */
    public int recordUcouponNotify(String out_trade_no, String mobile, String url){
        try {
            return jdbcTemplateUcoupon.update(NOTIFY_COUPON_URL, out_trade_no, mobile, url);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 记录每次定位结果
     * @param lng
     * @param lat
     * @param openid
     * @param stationList
     * @return
     */
    public int recordLocateLog(String lng, String lat, String openid, StationList stationList){
        MapSqlParameterSource source = new MapSqlParameterSource();
        List<StationInfo> list = stationList.getList();
        if(list != null)
           source.addValue("o", openid)
                   .addValue("lng", lng)
                   .addValue("lat", lat)
                   .addValue("m", list.get(0).getMin())
                   .addValue("id", list.get(0).getStation_id())
                   .addValue("name", list.get(0).getStation_name());
        else
            source.addValue("o", openid)
                    .addValue("lng", lng)
                    .addValue("lat", lat)
                    .addValue("m", -1)
                    .addValue("id", "NA")
                    .addValue("name", "NA");
        try {
            return namedParameterJdbcTemplateUcoupon.update(RECORD_LOCATE_SQL, source);
        }catch (Exception e){
            return 0;
        }
    }
}

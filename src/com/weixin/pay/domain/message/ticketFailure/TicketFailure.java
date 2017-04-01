package com.weixin.pay.domain.message.ticketFailure;

import com.weixin.pay.domain.message.DetailMsg;

/**
 * Created by luc on 16/8/6.
 */
public class TicketFailure {
    private DetailMsg first;

    private DetailMsg keyword1;

    private DetailMsg keyword2;

    private DetailMsg keyword3;

    private DetailMsg remark;

    public DetailMsg getFirst() {
        return first;
    }

    public void setFirst(DetailMsg first) {
        this.first = first;
    }

    public DetailMsg getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(DetailMsg keyword1) {
        this.keyword1 = keyword1;
    }

    public DetailMsg getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(DetailMsg keyword2) {
        this.keyword2 = keyword2;
    }

    public DetailMsg getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(DetailMsg keyword3) {
        this.keyword3 = keyword3;
    }

    public DetailMsg getRemark() {
        return remark;
    }

    public void setRemark(DetailMsg remark) {
        this.remark = remark;
    }

    public TicketFailure(DetailMsg first, DetailMsg keyword1, DetailMsg keyword2, DetailMsg keyword3, DetailMsg remark) {
        this.first = first;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.remark = remark;
    }
}

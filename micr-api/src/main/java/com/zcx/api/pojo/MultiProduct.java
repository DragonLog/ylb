package com.zcx.api.pojo;

import com.zcx.api.model.ProductInfo;

import java.io.Serializable;
import java.util.List;

public class MultiProduct implements Serializable {

    private List<ProductInfo> xinShouBao;
    private List<ProductInfo> youXuan;
    private List<ProductInfo> sanBiao;

    public MultiProduct() {
    }

    public MultiProduct(List<ProductInfo> xinShouBao, List<ProductInfo> youXuan, List<ProductInfo> sanBiao) {
        this.xinShouBao = xinShouBao;
        this.youXuan = youXuan;
        this.sanBiao = sanBiao;
    }

    public List<ProductInfo> getXinShouBao() {
        return xinShouBao;
    }

    public void setXinShouBao(List<ProductInfo> xinShouBao) {
        this.xinShouBao = xinShouBao;
    }

    public List<ProductInfo> getYouXuan() {
        return youXuan;
    }

    public void setYouXuan(List<ProductInfo> youXuan) {
        this.youXuan = youXuan;
    }

    public List<ProductInfo> getSanBiao() {
        return sanBiao;
    }

    public void setSanBiao(List<ProductInfo> sanBiao) {
        this.sanBiao = sanBiao;
    }

    @Override
    public String toString() {
        return "MultiProduct{" +
                "xinShouBao=" + xinShouBao +
                ", youXuan=" + youXuan +
                ", sanBiao=" + sanBiao +
                '}';
    }
}

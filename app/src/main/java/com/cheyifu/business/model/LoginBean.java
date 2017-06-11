package com.cheyifu.business.model;

import com.cheyifu.business.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wsl  on 2017/6/711:32
 */
public class LoginBean extends BaseBean {
    private List<ItemsBean> items;

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public class ItemsBean implements Serializable{
        private String merchantId;
        private String parkingId;
        private String parkingName;

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getParkingId() {
            return parkingId;
        }

        public void setParkingId(String parkingId) {
            this.parkingId = parkingId;
        }

        public String getParkingName() {
            return parkingName;
        }

        public void setParkingName(String parkingName) {
            this.parkingName = parkingName;
        }
    }
}

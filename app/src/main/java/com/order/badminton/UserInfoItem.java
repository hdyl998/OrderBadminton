package com.order.badminton;

/**
 * Created by Administrator on 2018/4/14.
 */

public class UserInfoItem {
    private PubAccount pubAccount;

    private boolean roleTeacher;

    private boolean sign;

    private boolean viewFee;


    @Override
    public String toString() {
        return getPubAccount().getRealName();

    }

    public void setPubAccount(PubAccount pubAccount) {
        this.pubAccount = pubAccount;
    }

    public PubAccount getPubAccount() {
        return this.pubAccount;
    }

    public void setRoleTeacher(boolean roleTeacher) {
        this.roleTeacher = roleTeacher;
    }

    public boolean getRoleTeacher() {
        return this.roleTeacher;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean getSign() {
        return this.sign;
    }

    public void setViewFee(boolean viewFee) {
        this.viewFee = viewFee;
    }

    public boolean getViewFee() {
        return this.viewFee;
    }

    public static class PubAccount {
        private int accountFee;

        private int amount;

        private String avatar;

        private int companyId;

        private String mobile;

        private int pubAccountId;

        private int pubUserId;

        private String realName;

        private int srvId;

        private String wechatId;

        public void setAccountFee(int accountFee) {
            this.accountFee = accountFee;
        }

        public int getAccountFee() {
            return this.accountFee;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatar() {
            return this.avatar;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public int getCompanyId() {
            return this.companyId;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return this.mobile;
        }

        public void setPubAccountId(int pubAccountId) {
            this.pubAccountId = pubAccountId;
        }

        public int getPubAccountId() {
            return this.pubAccountId;
        }

        public void setPubUserId(int pubUserId) {
            this.pubUserId = pubUserId;
        }

        public int getPubUserId() {
            return this.pubUserId;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getRealName() {
            return this.realName;
        }

        public void setSrvId(int srvId) {
            this.srvId = srvId;
        }

        public int getSrvId() {
            return this.srvId;
        }

        public void setWechatId(String wechatId) {
            this.wechatId = wechatId;
        }

        public String getWechatId() {
            return this.wechatId;
        }
    }


}

package com.modules.login.model.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-06-15
 */
@Data
public class NeedOtherVo {

    // 是否展示绑定界面
    private boolean show;

    // 是否已绑定微信
    private boolean wechat;

    // 是否已绑定支付宝
    private boolean alipay;

}

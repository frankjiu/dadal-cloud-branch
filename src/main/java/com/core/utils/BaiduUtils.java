package com.core.utils;

import com.baidu.aip.http.AipRequest;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;

public class BaiduUtils extends AipImageClassify {

    public BaiduUtils(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }

    /**
     * 添加百度识别图片
     *
     * @param image
     * @param brief
     * @return
     */
    public JSONObject addDefinedPic(byte[] image, String brief) {
        AipRequest request = new AipRequest();
        this.preOperation(request);
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        request.addBody("brief", brief);
        request.setUri("https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/dish/add");
        this.postOperation(request);
        return this.requestServer(request);
    }

    /**
     * 删除百度识别图
     *
     * @param contSign
     * @return
     */
    public JSONObject deleteDefinedPic(String contSign) {
        AipRequest request = new AipRequest();
        this.preOperation(request);
        request.addBody("cont_sign", contSign);
        request.setUri("https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/dish/delete");
        this.postOperation(request);
        return this.requestServer(request);
    }

}

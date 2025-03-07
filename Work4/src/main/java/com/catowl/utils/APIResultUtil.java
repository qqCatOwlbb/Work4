package com.catowl.utils;

import com.catowl.entity.APIResult;

public class APIResultUtil {
    public static APIResult apiResult(Boolean success,Object data, String message) {
        APIResult apiResult = new APIResult();
        apiResult.setSuccess(success);
        apiResult.setMessage(message);
        apiResult.setData(data);
        return apiResult;
    }
}

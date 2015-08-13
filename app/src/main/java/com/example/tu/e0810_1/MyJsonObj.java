package com.example.tu.e0810_1;

import com.google.gson.annotations.SerializedName;
/**
 * Created by tu on 2015/8/11.
 */
public class MyJsonObj {
    @SerializedName("title")
    private String titleStr;

    @SerializedName("createDatetime")
    private String createDatetimeStr;

    @SerializedName("pmName")
    private String pmName;

    @SerializedName("imgSrc")
    private String imgSrc;

    @SerializedName("conHtml")
    private String conHtml;

    public String getSna() {
        return titleStr;
    }

    public String getLat() {
        return createDatetimeStr;
    }

    public String getLng() {
        return pmName;
    }

    public String getSbi() {
        return imgSrc;
    }
    public String getBemp() {
        return conHtml;
    }
}

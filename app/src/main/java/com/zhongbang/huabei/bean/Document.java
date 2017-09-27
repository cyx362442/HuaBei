package com.zhongbang.huabei.bean;

/**
 * Created by Administrator on 2017-09-27.
 */

public class Document {

    /**
     * content : 3张主界面轮播图片网址，文档忘了提供，希望你们能看到@.@：
     http://chinaqmf.cn:8088/ihuabei/attached/rotation/1.jpg
     http://chinaqmf.cn:8088/ihuabei/attached/rotation/2.jpg
     http://chinaqmf.cn:8088/ihuabei/attached/rotation/3.jpg
     * createTime : 2017-08-18 09:23:36
     * id : 114
     * images :
     * type : marketing
     */

    private String content;
    private String createTime;
    private String id;
    private String images;
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.leopard4.alcoholrecipe.model;

public class Emotion {

    //    {
//        "result": {
//        "alcoholId": 1306,
//                "name": "그래, 그 날",
//                "imgUrl": "",
//                "emotionId": 8,
//                "emotion": "SAD",
//                "content": "혹시 지금 그 날을 떠올리고 있나요? 그래, 그 날"
//    }
//    }

    private  int alcoholId;
    private  String name;
    private  String imgurl;
    private int emotionId;
    private  String emotion;
    private  String content;

    public int getAlcoholId() {
        return alcoholId;
    }

    public void setAlcoholId(int alcoholId) {
        this.alcoholId = alcoholId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.leopard4.alcoholrecipe.model.cheers;

import com.leopard4.alcoholrecipe.model.cheers.CheersMent;

import java.io.Serializable;

public class CheersMentRes implements Serializable {
//    {
//        "result": "success",
//            "item": {
//                "title": "지화자",
//                "first": "지금부터 화끈한 자리를",
//                "last": "위하여!"
//    }
//    }
    private String result;
    // item이라는 키값으로 오는 reponse가 CheersMent 클래스로 매핑됩니다.
    // 만약 아이템이 여러개라면,
    // List<CheersMent> item; 이렇게 하면 List로 받을 수 있습니다.
    // 여기선 1개만 받을것이기 때문에 CheersMent로 받습니다.
    private CheersMent item;



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public CheersMent getItem() {
        return item;
    }

    public void setItem(CheersMent item) {
        this.item = item;
    }
}

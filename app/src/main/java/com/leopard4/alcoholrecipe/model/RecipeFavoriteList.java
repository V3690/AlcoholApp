package com.leopard4.alcoholrecipe.model;

import java.util.List;

public class RecipeFavoriteList {
//    {
//        "result": [
//        {
//            "id": 2,
//                "title": "블루레몬에이드밀키스주",
//                "percent": 1
//        },
//        {
//            "id": 5,
//                "title": "탄산이온주",
//                "percent": 4
//        }
//    ],
//        "count": 2
//    }
    private List<RecipeFavorite> result;
    private int count;

    public List<RecipeFavorite> getResult() {
        return result;
    }

    public void setResult(List<RecipeFavorite> result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.leopard4.alcoholrecipe.model.recipe;

import com.leopard4.alcoholrecipe.model.dogam.Dogam;

import java.io.Serializable;
import java.util.List;

public class RecipeLabList  implements Serializable {

    //"result": "success",
//        "items": [
//    {
//             "id": 17,
//            "title": "홍초주",
//            "percent": 1,
//            "cnt": 2
//      ],
//    "count": 25
//}


    private String result;
    private List<RecipeLab> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<RecipeLab> getItems() {
        return items;
    }

    public void setItems(List<RecipeLab> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;
import java.util.List;

public class RecipeOne implements Serializable {
//    {
//        "recipeOne": {
//                "title": "탱크젤리주",
//                "likeCnt": 2,
//                "percent": 2,
//                "alcoholType": null,
//                "userId": 1,
//                "engTitle": null,
//                "intro": "칵테일을 만들어보세요",
//                "content": "소주, 탄산수, 봉봉를 1:1:1 비율로 섞어서 만들어요",
//                "imgUrl": "https://i.imgur.com/1ZQ3Z9M.png",
//                "ingredient": "설레임, 토닉워터",
//                "createdAt": "2023-03-06T07:19:23",
//                "updatedAt": "2023-03-08T17:23:33"
//    }
//    }
    private List<Recipe> recipeOne;

    public List<Recipe> getRecipeOne() {
        return recipeOne;
    }

    public void setRecipeOne(List<Recipe> recipeOne) {
        this.recipeOne = recipeOne;
    }
}

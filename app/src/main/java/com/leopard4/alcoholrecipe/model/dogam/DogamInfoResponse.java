package com.leopard4.alcoholrecipe.model.dogam;

import com.google.gson.annotations.SerializedName;
import com.leopard4.alcoholrecipe.model.RecipeOne;

import java.util.List;

public class DogamInfoResponse {

    private String result;

    @SerializedName("alcohol")
    private DogamInfo dogamInfo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DogamInfo getDogamInfo() {
        return dogamInfo;
    }

    public void setDogamInfo(DogamInfo dogamInfo) {
        this.dogamInfo = dogamInfo;
    }
}

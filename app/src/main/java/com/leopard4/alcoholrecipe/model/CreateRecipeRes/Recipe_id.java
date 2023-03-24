package com.leopard4.alcoholrecipe.model.CreateRecipeRes;


public class Recipe_id
{
    private String id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    @Override
    public String toString() // toString() 메소드를 오버라이딩 해준다. // 이 메소드는 객체를 문자열로 표현할 때 사용된다.
    {
        return "ClassPojo [id = "+id+"]"; // 리턴값을 설정해준다.
    }
}



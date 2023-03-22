package com.leopard4.alcoholrecipe.model.CreateRecipeRes;

public class RecipeRes {
    private String result;

    private Recipe_id[] recipe_id;

    public String getResult ()
    {
        return result;
    }

    public void setResult (String result)
    {
        this.result = result;
    }

    public Recipe_id[] getRecipe_id ()
    {
        return recipe_id;
    }

    public void setRecipe_id (Recipe_id[] recipe_id)
    {
        this.recipe_id = recipe_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", recipe_id = "+recipe_id+"]";
    }

}



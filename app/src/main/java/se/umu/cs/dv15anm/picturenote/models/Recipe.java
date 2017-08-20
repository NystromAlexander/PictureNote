package se.umu.cs.dv15anm.picturenote.models;

import java.util.Date;

import se.umu.cs.dv15anm.picturenote.helpers.NoteType;

/**
 * Class to represent a recipe, have som fields in common with the note
 */

public class Recipe extends Note {

    private String mIngredients;
    private String mIngredientsImg;
    private String mImage;

    /**
     * Create a new recipe
     */
    public Recipe() {
        super(NoteType.RECIPE);
    }

    /**
     * Create a new recipe
     * @param title The title of the recipe.
     * @param text The instructions
     * @param date The date it was last modified
     * @param ingredients The ingredients.
     */
    public Recipe(String title, String text, Date date, String ingredients) {
        super(title,text,date, NoteType.RECIPE);
        mIngredients = ingredients;
    }

    /**
     *
     * @return Path to the image that generated the ingredients
     */
    public String getIngredientsImg() {
        return mIngredientsImg;
    }

    /**
     * Set the path to the image that generate the instructions
     * @param recipeImg Path to the image.
     */
    public void setRecipeImg(String recipeImg) {
        super.setNoteImage(recipeImg);
    }

    /**
     *
     * @return Path to the image that generated the instructions
     */
    public String getRecipeImg() {
        return super.getNoteImage();
    }

    /**
     * Set the path to the image that generated the ingredients.
     * @param ingredientsImg The image path
     */
    public void setIngredientsImg(String ingredientsImg) {
        mIngredientsImg = ingredientsImg;
    }

    /**
     *
     * @return The ingredients text
     */
    public String getIngredients() {
        return mIngredients;
    }

    /**
     * Set the text for the ingredients
     * @param ingredients The ingredients.
     */
    public void setIngredients(String ingredients) {
        mIngredients = ingredients;
    }

    /**
     *
     * @return Path to image representing the result of the recipe
     */
    public String getFoodImage() {
        return mImage;
    }

    /**
     * Set a image of the result from the recipe
     * @param image The path to the image.
     */
    public void setFoodImage(String image) {
        mImage = image;
    }
}

package se.umu.cs.dv15anm.picturenote.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import se.umu.cs.dv15anm.picturenote.models.Note;
import se.umu.cs.dv15anm.picturenote.models.Recipe;
import se.umu.cs.dv15anm.picturenote.database.NoteDbSchema.RecipeTable;
import se.umu.cs.dv15anm.picturenote.helpers.NoteType;
import se.umu.cs.dv15anm.picturenote.database.NoteDbSchema.NoteTable;

/**
 * Cursor wrapper to ease the use of the database.
 */

public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Retrieve and create a note.
     * @return The complete note
     */
    public Note getNote() {
        int id = getInt(getColumnIndex(NoteTable.Cols.ID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        String text = getString(getColumnIndex(NoteTable.Cols.TEXT));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        String noteImage = getString(getColumnIndex(NoteTable.Cols.NOTE_IMAGE));

        Note note = new Note(title, text,new Date(date), NoteType.STANDARD);
        note.setId(id);
        note.setNoteImage(noteImage);
        return note;
    }

    /**
     * Retrieve and create a recipe
     * @return The complete recipe
     */
    public Recipe getRecipe() {
        int id = getInt((getColumnIndex(RecipeTable.Cols.ID)));
        String title = getString(getColumnIndex(RecipeTable.Cols.TITLE));
        String recipeText = getString(getColumnIndex(RecipeTable.Cols.RECIPE));
        String ingredientsText = getString(getColumnIndex(RecipeTable.Cols.INGREDIENTS));
        String recipeImage = getString(getColumnIndex(RecipeTable.Cols.RECIPE_IMAGE));
        String ingredientsImage = getString(getColumnIndex(RecipeTable.Cols.INGREDIENTS_IMAGE));
        String foodImage = getString(getColumnIndex(RecipeTable.Cols.FOOD_IMAGE));
        long date = getLong(getColumnIndex(RecipeTable.Cols.DATE));

        Recipe recipe = new Recipe(title,recipeText,new Date(date),ingredientsText);
        recipe.setId(id);
        recipe.setRecipeImg(recipeImage);
        recipe.setIngredientsImg(ingredientsImage);
        recipe.setFoodImage(foodImage);
        return recipe;
    }
}

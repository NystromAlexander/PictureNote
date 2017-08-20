package se.umu.cs.dv15anm.picturenote.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.umu.cs.dv15anm.picturenote.models.Note;
import se.umu.cs.dv15anm.picturenote.NoteListActivity;
import se.umu.cs.dv15anm.picturenote.models.Recipe;
import se.umu.cs.dv15anm.picturenote.database.NoteDbSchema.NoteTable;
import se.umu.cs.dv15anm.picturenote.database.NoteDbSchema.RecipeTable;
import se.umu.cs.dv15anm.picturenote.helpers.NoteType;

/**
 * Class used to communicate with the database and give easy access.
 */

public class NoteBoard {

    private static final String TAG = "NoteBoard";
    private static NoteBoard sNoteBoard;

    private SQLiteDatabase mDatabase;

    /**
     * The NoteBoard is a singleton and will only have one existing object reference, if there is
     * none one will be created.
     * @param context The context of the calling activity
     * @return The NotBoard.
     */
    public static NoteBoard getNoteBoard(Context context) {
        if (sNoteBoard == null) {
            sNoteBoard = new NoteBoard(context);
        }
        return sNoteBoard;
    }

    /**
     * Retrieve a list with all notes and recipes
     * @return The finished list
     */
    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();

        try (NoteCursorWrapper cursor = queryNotes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        }

        try (NoteCursorWrapper cursor = quesryRecipes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getRecipe());
                cursor.moveToNext();
            }
        }

        Collections.sort(notes,new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return o2.getDate().compareTo(o1.getDate()) ;
            }
        });

        return notes;
    }

    /**
     * Retrieve a specific note by searching for it's id in the database.
     * @param id The id of the note.
     * @return The note if found else null.
     */
    public Note getNote(Long id) {

        try (NoteCursorWrapper cursor = queryNotes(NoteTable.Cols.ID + " = ?",
                new String[]{id.toString()})) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNote();
        }
    }

    /**
     * Retrieve a specific recipe by searching for it's id, in the database.
     * @param id The id of the recipe.
     * @return The recipe if found else null.
     */
    public Recipe getRecipe(Long id) {
        try (NoteCursorWrapper cursor = quesryRecipes(RecipeTable.Cols.ID + " = ?",
                new String[]{id.toString()})) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getRecipe();
        }
    }

    /**
     * Add a note to the database.
     * @param note The note to be added
     * @return The note but with it's database id added.
     */
    public Note addNote(Note note) {
        ContentValues values = getContentValues(note);
        long id = mDatabase.insert(NoteTable.NAME, null, values);
        note.setId(id);
        return note;
    }

    /**
     * Add a recipe to the database.
     * @param recipe The recipe to be added.
     * @return The recipe but with it's database id added.
     */
    public Recipe addRecipe(Recipe recipe) {
        ContentValues values = getRecipeContentValues(recipe);
        long id = mDatabase.insert(RecipeTable.NAME, null, values);
        recipe.setId(id);
        return recipe;
    }

    /**
     * Update a pre-existing note in the database,
     * @param note The note to be updated.
     */
    public void updateNote(Note note) {
        Long id = note.getId();
        ContentValues values = getContentValues(note);

        mDatabase.update(NoteTable.NAME, values, NoteTable.Cols.ID + " = ?",
                new String[] {id.toString()});
    }

    /**
     * Update a pre-existing recipe in the database.
     * @param recipe The recipe to be updated.
     */
    public void updateRecipe(Recipe recipe) {
        Long id = recipe.getId();
        ContentValues values = getRecipeContentValues(recipe);

        mDatabase.update(RecipeTable.NAME, values, RecipeTable.Cols.ID + " = ?",
                new String[] {id.toString()});
    }

    /**
     * Remove a note from the database if the note is a recipe removeRecipe will be called to
     * handle the removal.
     * @param note The note to be removed.
     */
    public void removeNote(Note note) {
        if (note.getType() == NoteType.RECIPE) {
            Recipe recipe = (Recipe) note;
            removeRecipe(recipe);
        }
        Long id = note.getId();
        mDatabase.delete(NoteTable.NAME,NoteTable.Cols.ID + "  = ?",new String[]{id.toString()});
        if (removeNoteImage(note.getNoteImage())) {
            Log.d(TAG, "Removed image at path: "+note.getNoteImage());
        }
    }

    /**
     * Remove a recipe from the database.
     * @param recipe The recipe to be removed.
     */
    private void removeRecipe(Recipe recipe) {
        Long id = recipe.getId();
        mDatabase.delete(RecipeTable.NAME, RecipeTable.Cols.ID + " = ?",
                new String[] {id.toString()});
        removeNoteImage(recipe.getRecipeImg());
        removeNoteImage(recipe.getIngredientsImg());
        removeNoteImage(recipe.getFoodImage());
    }

    /**
     * Remove a image that is linked to a note or recipe, if that image was taken from the gallery
     * and hence not have the app's name in the file path, that image will be removed.
     * @param imagePath The path to the image to be removed.
     * @return true if it got removed else false.
     */
    private boolean removeNoteImage(String imagePath) {
        if (imagePath.contains(NoteListActivity.APP_NAME)) {
            File imageFile = new File(imagePath);
            return imageFile.delete();
        }

        return false;
    }

    /**
     * Query after notes, and return the cursor, with the notes sorted in descending order sorted
     * after their dates
     * @param whereClause The where sql statement
     * @param whereArgs The arguments for the where statement
     * @return The cursor to the query.
     */
    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(NoteTable.NAME, null, whereClause, whereArgs, null, null,
                NoteTable.Cols.DATE + " DESC");

        return new NoteCursorWrapper(cursor);
    }

    /**
     * Query after recipes, and return the cursor, with the recipes sorted in descending order
     * sorted after their dates
     * @param whereClause The where sql statement
     * @param whereArgs The arguments for the where statement
     * @return The cursor to the query.
     */
    private NoteCursorWrapper quesryRecipes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(RecipeTable.NAME, null, whereClause, whereArgs, null, null,
                RecipeTable.Cols.DATE + " DESC");

        return new NoteCursorWrapper(cursor);
    }

    /**
     * Create content values from a note object
     * @param note The note to have the data extracted to a content value object.
     * @return The finished content value
     */
    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.TEXT, note.getText());
        values.put(NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteTable.Cols.NOTE_IMAGE, note.getNoteImage());

        return values;
    }

    /**
     * Create content values from a recipe object
     * @param recipe The recipe to have the data extracted to a content value object.
     * @return The finished content value
     */
    private ContentValues getRecipeContentValues(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeTable.Cols.TITLE, recipe.getTitle());
        values.put(RecipeTable.Cols.RECIPE, recipe.getText());
        values.put(RecipeTable.Cols.INGREDIENTS, recipe.getIngredients());
        values.put(RecipeTable.Cols.RECIPE_IMAGE, recipe.getRecipeImg());
        values.put(RecipeTable.Cols.INGREDIENTS_IMAGE, recipe.getRecipeImg());
        values.put(RecipeTable.Cols.FOOD_IMAGE, recipe.getFoodImage());
        values.put(RecipeTable.Cols.DATE, recipe.getDate().getTime());
        return values;
    }

    /**
     * Create a new NoteBoard.
     * @param context the context of the caller.
     */
    private NoteBoard(Context context) {
        mDatabase = new NoteDbHelper(context.getApplicationContext()).getWritableDatabase();
    }
}

package se.umu.cs.dv15anm.picturenote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import se.umu.cs.dv15anm.picturenote.database.NoteDbSchema.NoteTable;
import se.umu.cs.dv15anm.picturenote.database.NoteDbSchema.RecipeTable;

/**
 * Class that will handle the database
 */

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "NoteBoard.db";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * When the database gets created, create the required tables
     * @param db The database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME +
                        "(" + NoteTable.Cols.ID + " integer primary key, " +
                NoteTable.Cols.TITLE + ", " +
                NoteTable.Cols.TEXT + ", " +
                NoteTable.Cols.DATE + ", " +
                NoteTable.Cols.NOTE_IMAGE + ")" );

        db.execSQL("create table " + RecipeTable.NAME + "(" +
                RecipeTable.Cols.ID + " integer primary key, " +
                RecipeTable.Cols.TITLE + ", " +
                RecipeTable.Cols.RECIPE + ", " +
                RecipeTable.Cols.INGREDIENTS + ", " +
                RecipeTable.Cols.RECIPE_IMAGE + ", " +
                RecipeTable.Cols.INGREDIENTS_IMAGE + ", " +
                RecipeTable.Cols.FOOD_IMAGE + ", " +
                RecipeTable.Cols.DATE + ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

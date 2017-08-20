package se.umu.cs.dv15anm.picturenote.database;

/**
 * Class used to make the database calls more clear and easy to understand
 */

public class NoteDbSchema{

    /**
     * Class representing the table fort the notes and the columns
     */
    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String TEXT = "text";
            public static final String DATE = "date";
            public static final String NOTE_IMAGE = "note_image";
        }
    }

    /**
     * Class representing the recipe table and the columns
     */
    public static final class RecipeTable {
        public static final String NAME = "recipes";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String RECIPE = "recipe";
            public static final String INGREDIENTS = "ingredients";
            public static final String DATE = "date";
            public static final String RECIPE_IMAGE = "recipe_image";
            public static final String FOOD_IMAGE = "food_image";
            public static final String INGREDIENTS_IMAGE = "ingredients_image";
        }
    }
}

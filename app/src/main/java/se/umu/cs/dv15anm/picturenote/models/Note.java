package se.umu.cs.dv15anm.picturenote.models;

import java.util.Date;

import se.umu.cs.dv15anm.picturenote.helpers.NoteType;

/**
 * Class to represent a Note
 */

public class Note {
    private String mTitle;
    private String mText;
    private Date mDate;
    private String mNoteImage;
    private NoteType mType;
    private long mId;

    /**
     * Create a new note.
     * @param type The type of the note.
     */
    public Note(NoteType type){
        mType = type;
        mTitle = "";
        mId = 0;
    }

    /**
     * Create a new note
     * @param title The title of the note.
     * @param text The content text of the note.
     * @param date The date the note was last modified.
     * @param type The notes type.
     */
    public Note(String title, String text, Date date, NoteType type) {
        mTitle = title;
        mText = text;
        mDate = date;
        mType = type;
        mId = 0;
    }

    /**
     *
     * @return The title of the note
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Change the title of the note.
     * @param mTitle The title of the note
     */
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     *
     * @return The content text of the note.
     */
    public String getText() {
        return mText;
    }

    /**
     * Change the content text of the note.
     * @param mText The content text to set to the note.
     */
    public void setText(String mText) {
        this.mText = mText;
    }

    /**
     *
     * @return The date the note was last modified
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * Change the date the note was last modified.
     * @param mDate The date the note was modified.
     */
    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    /**
     *
     * @return The database id of the note.
     */
    public long getId() {
        return mId;
    }

    /**
     * Set the database id of the note.
     * @param mId The database id of the note
     */
    public void setId(long mId) {
        this.mId = mId;
    }

    /**
     *
     * @return Path to the image that generated the content text.
     */
    public String getNoteImage() {
        return mNoteImage;
    }

    /**
     * Set path to the image that generated the content text
     * @param noteImage The image to be associated with the note.
     */
    public void setNoteImage(String noteImage) {
        mNoteImage = noteImage;
    }

    /**
     *
     * @return The type of note.
     */
    public NoteType getType() {
        return mType;
    }
}

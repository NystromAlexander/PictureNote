package se.umu.cs.dv15anm.picturenote;

/**
 * Created by Roguz on 11/08/2017.
 */

public class Note {
    private String mTitle;
    private String mText;
    private String mDate;
    private int mId;

    public Note(){}

    public Note(String title, String text, String date) {
        mTitle = title;
        mText = text;
        mDate = date;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}

package beans;

public class Note {

    private int note_id;
    private String title;
    private String note;
    private int color;

    public Note(int note_id, String title, String note, int color) {
        this.note_id=note_id;
        this.title=title;
        this.note=note;
        this.color=color;
    }

    public int getId() {
        return note_id;
    }

    public void setId(int id){ this.note_id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}


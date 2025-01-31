package snipster;

public class Snippet {
    private int id;
    private String title;
    private String code;
    private String tags;

    public Snippet(int id, String title, String code, String tags) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return id + " - " + title;
    }
}

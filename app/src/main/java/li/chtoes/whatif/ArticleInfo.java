package li.chtoes.whatif;

import android.util.JsonReader;

import java.io.IOException;

//Basic info about article
public class ArticleInfo {
    public static ArticleInfo EMPTY = new ArticleInfo("", "");


    private String id;         // Id of article, e.g. "microwaves"
    private String title;      // Title of article, e.g. "Микроволны"

    public ArticleInfo() { id = ""; title = ""; }

    public ArticleInfo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String mId) {
        this.id = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public static ArticleInfo fromJsonReader(JsonReader jsonReader) throws IOException {
        String id = "";
        String title = "";

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if (name.equals("id")) {
                id = jsonReader.nextString();
            } else if (name.equals("title")) {
                title = jsonReader.nextString();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return new ArticleInfo(id, title);
    }

    public String toString() {
        return String.format("Title: %1$s ID: %2$s", getTitle(), getId());
    }
}

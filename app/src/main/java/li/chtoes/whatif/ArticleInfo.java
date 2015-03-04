package li.chtoes.whatif;

import android.util.JsonReader;

import java.io.IOException;

//Basic info about article
public class ArticleInfo {
    private String mId;         // Id of article, e.g. "microwaves"
    private String mTitle;      // Title of article, e.g. "Микроволны"

    public ArticleInfo() { mId = ""; mTitle = ""; }

    public ArticleInfo(String id, String title) {
        this.mId = id;
        this.mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
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

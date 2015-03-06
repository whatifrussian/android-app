package li.chtoes.whatif;

/**
 * Class, that holds all info about article
 */
public class Article {
    private String id;
    private String title;
    private String content;

    public Article(ArticleInfo info) {
        this.id = info.getId();
        this.title = info.getTitle();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package li.chtoes.whatif;

import android.os.AsyncTask;
import android.util.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class for static objects as mainActivity or app tag
 */
public class App {
    public static final String TAG = "whatif";
    public static ArticleActivity mainActivity;

    /**
     * Class for chtoes.li API
     */
    public static class API {
        public static final String MAIN_SITE_URL = "https://chtoes.li/";
        public static final String ARTICLES_URL = MAIN_SITE_URL + "articles.json";
        public static final String ARTICLE_TEMPLATE = MAIN_SITE_URL + "%1$s/";

        private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

        /**
         * Gets info about available {@link Article}s.
         *
         * @return available articles infos
         * @throws IOException
         * @see li.chtoes.whatif.Article
         * @see li.chtoes.whatif.ArticleInfo
         */
        public static List<ArticleInfo> getArticleInfos() throws IOException {
            // Converting string to stream and creating JsonReader
            String jsonResponse = getArticleInfosJSON();
            byte[] bytes = jsonResponse.getBytes();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            InputStreamReader reader = new InputStreamReader(inputStream, DEFAULT_CHARSET);
            JsonReader jsonReader = new JsonReader(reader);

            //Starting reading
            List<ArticleInfo> infos = new ArrayList<ArticleInfo>();

            try {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    ArticleInfo articleInfo = ArticleInfo.fromJsonReader(jsonReader);
                    articleInfo.setIndex(infos.size());

                    infos.add(articleInfo);
                }
                jsonReader.endArray();
            } finally {
                jsonReader.close();
            }

            return infos;
        }

        /**
         * Get json of articles
         * @return json string that contains article infos
         */
        private static String getArticleInfosJSON() {
            return  "[ " +
                        "{ \"title\": \"Микроволны\", \"id\": \"microwaves\" }," +
                        "{ \"title\": \"Уборка снега\", \"id\": \"snow-removal\" }," +
                        "{ \"title\": \"Черная дыра вместо Луны\", \"id\": \"black-hole-moon\" }," +
                        "{ \"title\": \"Zippo’фон\", \"id\": \"zippo-phone\" }," +
                        "{ \"title\": \"Перетягивание каната\", \"id\": \"tug-of-war\" }," +
                        "{ \"title\": \"Лестница\", \"id\": \"stairs\" }" +
                    "]";

            //return GetHTML.get(ARTICLES_URL);
        }

        /**
         * @return articles count or zero, if IOException occurred
         */
        public static int getArticlesCount() {
            try {
                return getArticleInfos().size();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        /**
         * @return info about last article or {@link li.chtoes.whatif.ArticleInfo#EMPTY} if
         * IOException occurred
         */
        public static ArticleInfo getLastArticleInfo() {
            return getArticleInfoByIndex(getLastIndex());
        }


        private static int getLastIndex() {
            //I will save last index to android settings soon

            return getArticlesCount() - 1;
        }

        /**
         * @return info about first article or {@link li.chtoes.whatif.ArticleInfo#EMPTY} if
         * IOException occurred
         */
        public static ArticleInfo getFirstArticleInfo() {
            return getArticleInfoByIndex(0);
        }

        public static ArticleInfo getArticleInfoByIndex(int index) {
            try {
                List<ArticleInfo> infos = getArticleInfos();
                for (ArticleInfo a : infos) {
                    if (a.getIndex() == index) {
                        return a;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ArticleInfo.EMPTY;
        }

        /**
         * Method for getting cutted html for app WebView
         *
         * @param info contains basic info about article
         * @param articleFragment fragment for callbacks
         */
        public static void getArticle(final ArticleInfo info, final ArticleFragment articleFragment) {
             new AsyncTask<String, Void, String>() {
                 @Override
                 protected String doInBackground(String... params) {
                     String id = params[0];

                     // Not really cool. Lazy.
                     String html = GetHTML.get(String.format(ARTICLE_TEMPLATE, id), false);
                     Document doc = Jsoup.parse(html);

                     Elements forRemove = doc.select("header, footer, " +
                                                     "ul.menu-page, " +
                                                     "div[class^=border], " +
                                                     "div[class^=social]");
                     forRemove.remove();

                     return doc.html();
                 }

                 @Override
                 protected void onPreExecute() {
                     super.onPreExecute();
                     articleFragment.onPreExecute();
                 }

                 @Override
                 protected void onPostExecute(String s) {
                     super.onPostExecute(s);

                     Article a = new Article(info);
                     a.setContent(s);

                     articleFragment.onGetArticle(a);
                 }
             }.execute(info.getId());
        }

        /**
         * This class used for synced getting html in android
         * In android you can't freeze main thread with GET requests
         * So I сreated this cheat
         */
        private static class GetHTML extends AsyncTask<String, Void, String> {
            public String response;

            /**
             * Simple method for getting html in one
             * string.
             *
             * @param url absolute url of page
             * @return html of the page
             */
            public static String get(String url, boolean useTask) {
                if (useTask) {
                    GetHTML g = new GetHTML();
                    try {
                        return g.execute(new String[]{url}).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(url);
                    HttpResponse response;

                    String html = "";
                    try {
                        response = client.execute(request);

                        InputStream in = response.getEntity().getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder str = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null)
                        {
                            str.append(line);
                        }
                        in.close();
                        html = str.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return html;
                }

                return "";
            }

            @Override
            protected String doInBackground(String... strings) {
                return get(strings[0], false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                response = s;
            }
        }
    }
}

package li.chtoes.whatif;

import android.os.AsyncTask;
import android.util.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//Class for static objects as
public class App {
    public static final String TAG = "whatif";
    public static ArticleActivity mainActivity;

    //Class for chtoes.li API
    //It doesn't exists
    public static class API {
        public static final String ARTICLES_URL = "https://chtoes.li/articles.json";
        private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

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
                    infos.add(ArticleInfo.fromJsonReader(jsonReader));
                }
                jsonReader.endArray();
            } finally {
                jsonReader.close();
            }

            return infos;
        }

        //Get json of articles
        private static String getArticleInfosJSON() {
            return  "[ " +
                        "{ \"title\": \"Микроволны\", \"id\": \"microwaves\" }," +
                        "{ \"title\": \"Уборка снега\", \"id\": \"snow-removal\" }" +
                    "]";

            //return GetHTML.get(ARTICLES_URL);
        }

        

        //This class used for synced getting html in android
        //In android you can't freeze main thread with GET requests
        //So I сreated this cheat
        private static class GetHTML extends AsyncTask<String, Void, String> {
            public String response;         // Result
            public boolean isSuccess;       // Was operation successful or not

            public static String get(String url) {
                GetHTML g =  new GetHTML();
                try {
                    return g.execute(new String[] {url}).get();
                } catch (InterruptedException ignored) {
                } catch (ExecutionException ignored) { }

                return "";
            }

            @Override
            protected String doInBackground(String... strings) {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(strings[0]);
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

                } catch (IOException ignored) {
                    isSuccess = false;
                } finally {
                    isSuccess = true;
                }

                return "";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                response = s;
            }
        }
    }
}

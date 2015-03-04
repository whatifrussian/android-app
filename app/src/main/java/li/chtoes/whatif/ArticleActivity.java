package li.chtoes.whatif;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;


public class ArticleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        App.mainActivity = this;

        Log.d(App.TAG, "Reading articles.");
        try {
            for (ArticleInfo info : App.API.getArticleInfos()) {
                Log.d(App.TAG, info.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(App.TAG, "Done reading articles.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

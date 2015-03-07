package li.chtoes.whatif;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.List;


public class ArticleActivity extends ActionBarActivity {
    private Button firstButton;
    private Button prevButton;
    private Button articlesListButton;
    private Button nextButton;
    private Button lastButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.ic_actionbar_icon);
        }

        App.mainActivity = this;

        try {
            List<ArticleInfo> i = App.API.getArticleInfos();
            openArticle(i.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initButtons();
    }

    private void initButtons() {
        firstButton = (Button) findViewById(R.id.first_article_button);
        prevButton = (Button) findViewById(R.id.prev_article_button);
        articlesListButton = (Button) findViewById(R.id.list_articles_button);
        nextButton = (Button) findViewById(R.id.next_article_button);
        lastButton = (Button) findViewById(R.id.last_article_button);


        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openArticle(App.API.getFirstArticleInfo());
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        } );
        articlesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        } );
        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openArticle(App.API.getLastArticleInfo());
            }
        });
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

    public void openArticle(ArticleInfo info) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.container, ArticleFragment.newInstance(info));
        transaction.addToBackStack(null);

        transaction.commitAllowingStateLoss();
    }
}

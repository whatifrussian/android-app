package li.chtoes.whatif;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ArticleActivity extends ActionBarActivity {
    private static final int REQUEST_CODE_GET_NUMBER = 1;
    private static int ACTION_BAR_HEIGHT = 0;

    private boolean isActionBarHided = false;

    private Button firstButton;
    private Button prevButton;
    private Button articlesListButton;
    private Button nextButton;
    private Button lastButton;

    private static ArticleInfo currentArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.ic_actionbar_icon);
        }

        App.mainActivity = this;

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        ACTION_BAR_HEIGHT = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        initButtons();

        openArticle(App.API.getArticleInfoByIndex(0));
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
                int index = currentArticle.getIndex() - 1;
                ArticleInfo info = App.API.getArticleInfoByIndex(index);

                openArticle(info);
            }
        } );
        articlesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ArticleActivity.this, ArticlesListActivity.class);
                startActivityForResult(myIntent, REQUEST_CODE_GET_NUMBER);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = currentArticle.getIndex() + 1;
                ArticleInfo info = App.API.getArticleInfoByIndex(index);

                openArticle(info);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GET_NUMBER) {
                int index = data.getIntExtra("number", currentArticle.getIndex());
                openArticle(App.API.getArticleInfoByIndex(index));
            }
        }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        super.onBackPressed();
    }

    public void openArticle(ArticleInfo info) {
        currentArticle = info;

        boolean isLast = App.API.getLastArticleInfo().getId().equals(info.getId());
        lastButton.setClickable(!isLast);
        lastButton.setTextColor(isLast ? Color.GRAY : Color.BLACK);
        nextButton.setClickable(!isLast);
        nextButton.setTextColor(isLast ? Color.GRAY : Color.BLACK);

        boolean isFirst = App.API.getFirstArticleInfo().getId().equals(info.getId());
        firstButton.setClickable(!isFirst);
        prevButton.setClickable(!isFirst);
        firstButton.setTextColor(isFirst ? Color.GRAY : Color.BLACK);
        prevButton.setTextColor(isFirst ? Color.GRAY : Color.BLACK);

        articlesListButton.setText("#" + (info.getIndex() + 1));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.container, ArticleFragment.newInstance(info));
        transaction.addToBackStack(null);

        transaction.commitAllowingStateLoss();
    }

    public void onScroll(int delta, int currentY) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) { return; }

        if (currentY < ACTION_BAR_HEIGHT && currentY - delta => ACTION_BAR_HEIGHT) {
            actionBar.show();
        }

        if (currentY > ACTION_BAR_HEIGHT * 3 && currentY - delta <= ACTION_BAR_HEIGHT * 3) {
            actionBar.hide();
        }
    }
}

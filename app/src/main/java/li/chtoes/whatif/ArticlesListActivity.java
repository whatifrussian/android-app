package li.chtoes.whatif;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class ArticlesListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> articles = App.API.getArticlesTitles();
        String[] articlesArray = new String[articles.size()];
        articlesArray = articles.toArray(articlesArray);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, articlesArray);

        setListAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String s = (String) getListAdapter().getItem(position);
        Intent intent = new Intent();
        intent.putExtra("number", Integer.parseInt(s.replaceAll("[\\D]", "")) - 1);
        setResult(RESULT_OK, intent);
        finish();
    }
}

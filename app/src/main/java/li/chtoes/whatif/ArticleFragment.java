package li.chtoes.whatif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * An article {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {
    private static final String ID_ARG_PARAM = "id";
    private static final String TITLE_ARG_PARAM = "title";

    private ProgressDialog progressDialog;

    private ObservableWebView content;
    //private TextView title;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param info contains base info about fragment
     * @return A new instance of fragment ArticleFragment.
     */
    public static ArticleFragment newInstance(ArticleInfo info) {
        ArticleFragment fragment = new ArticleFragment();

        Bundle args = new Bundle();
        args.putString(ID_ARG_PARAM, info.getId());
        args.putString(TITLE_ARG_PARAM, info.getTitle());
        fragment.setArguments(args);

        return fragment;
    }

    public ArticleFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String id = getArguments().getString(ID_ARG_PARAM);
            String title = getArguments().getString(TITLE_ARG_PARAM);

            ArticleInfo info = new ArticleInfo(id, title);
            App.API.getArticle(info, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        //title = (TextView)v.findViewById(R.id.article_title);
        content = (ObservableWebView)v.findViewById(R.id.article_content);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onPreExecute() {
        Resources r = App.mainActivity.getResources();

        progressDialog = ProgressDialog.show(App.mainActivity,
                r.getString(R.string.reading_article),
                r.getString(R.string.please_wait),
                true);

        progressDialog.setCancelable(false);
    }

    public void onGetArticle(Article article) {
        //title.setText(article.getTitle());

        content.getSettings().setJavaScriptEnabled(true);
        content.loadDataWithBaseURL(App.API.MAIN_SITE_URL,
                                    article.getContent(),
                                    "text/html",
                                    "UTF-8", "");

        progressDialog.dismiss();
    }
}

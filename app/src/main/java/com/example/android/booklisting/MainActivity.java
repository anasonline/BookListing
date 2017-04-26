package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    /**
     * URL for Google Books API
     */
    public String defaultRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    Button button;
    EditText searchQueryEditText;
    String keyword;
    View loadingIndicator;

    LoaderManager loaderManager;
    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        searchQueryEditText = (EditText) findViewById(R.id.search_query);

        button = (Button) findViewById(R.id.search_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                keyword = searchQueryEditText.getText().toString();

                // Find a reference to the {@link ListView} in the layout
                ListView bookListView = (ListView) findViewById(R.id.list);

                // Create a new adapter that takes an empty list of books as input
                mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface
                bookListView.setAdapter(mAdapter);

                // Get a reference to the LoaderManager, in order to interact with loaders.
                loaderManager = getLoaderManager();

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

                if (loaderManager.getLoader(BOOK_LOADER_ID).isStarted()) {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    //restart it if there's one
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }
            }
        });
    }


    public String createQuery(String defaultRequestUrl) {
        String searchQuery = defaultRequestUrl + keyword + "&maxResults=10";
        return searchQuery;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, createQuery(defaultRequestUrl));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        loadingIndicator.setVisibility(View.GONE);
        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
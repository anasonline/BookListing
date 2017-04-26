package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * URL for Google Books API
     */
    public String defaultRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=";

    EditText searchQueryEditText;
    String searchQuery;
    String keyword;
    ListView bookListView;
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


        // Find a reference to the {@link ListView} in the layout
        bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        mEmptyStateTextView.setText(R.string.no_results);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        bookListView.setAdapter(mAdapter);

        // Get the loadingIndicator and hide it
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Initialize loader
        loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        Button button = (Button) findViewById(R.id.search_button);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // Show loading indicator when button is clicked
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);
                // Get a reference to the EditText
                searchQueryEditText = (EditText) findViewById(R.id.search_query);
                // Get the entered search keyword
                keyword = searchQueryEditText.getText().toString();
                // Build the search query URL
                searchQuery = defaultRequestUrl + keyword + "&maxResults=10";

                // Get a reference to the LoaderManager
                loaderManager = getLoaderManager();

                // Initialize the loader
                loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

                search();
            }
        });
    }

    public void search() {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Restart Loader
            loaderManager.restartLoader(BOOK_LOADER_ID, null, this);

        } else {
            // display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given search query
        return new BookLoader(this, searchQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
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
package idv.haojun.objectboxdemo;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private RecyclerView rv;

    private AuthorDao authorDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_main_add_author).setOnClickListener(this);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new AuthorRVAdapter());

        getAuthors();
    }

    public void getAuthors() {
        if (authorDao == null) authorDao = new AuthorDao();
        ((AuthorRVAdapter) rv.getAdapter()).setAuthors(authorDao.getAuthors());
    }

    @Override
    public void onClick(View v) {
        Author author = new Author();
        author.name = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        long id = authorDao.putAuthor(author);
        if (id > 0) {
            getAuthors();
        } else {
            Toast.makeText(this, "Add author fail", Toast.LENGTH_SHORT).show();
        }
    }

    class AuthorRVAdapter extends RecyclerView.Adapter<AuthorRVAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvName;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_item_rv_authors_name);

                itemView.setOnClickListener(this);
            }

            public void setName(String name) {
                tvName.setText(name);
            }

            private void showAuthorInfo(final Author author) {
                final EditText et = new EditText(MainActivity.this);
                et.setText(author.name);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("#" + author.id)
                        .setView(et)
                        .setNegativeButton("CANCEL", null)
                        .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                authorDao.removeAuthor(author.id);
                                getAuthors();
                            }
                        })
                        .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = et.getText().toString();
                                if (TextUtils.isEmpty(name)) {
                                    Toast.makeText(MainActivity.this, "Can't empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                author.name = name;
                                authorDao.putAuthor(author);
                                getAuthors();
                            }
                        })
                        .show();
            }

            @Override
            public void onClick(View v) {
                showAuthorInfo(authors.get(getAdapterPosition()));
            }
        }

        private List<Author> authors;

        public AuthorRVAdapter() {
            authors = new ArrayList<>();
        }

        public void setAuthors(List<Author> authors) {
            Log.d(TAG, "size:" + authors.size());
            this.authors = authors;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_authors, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Author author = authors.get(position);
            holder.setName(String.format(Locale.getDefault(), "#%d %s", author.id, author.name));
        }

        @Override
        public int getItemCount() {
            return authors.size();
        }
    }
}

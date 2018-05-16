package idv.haojun.cacheimagesample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private List<MyImage> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_add).setOnClickListener(this);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ImagesRVAdapter());

        getImages();

        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    private void getImages() {
        images = PrefHelper.getImages(this);
        rv.getAdapter().notifyDataSetChanged();
    }

    private void saveImages() {
        PrefHelper.setImages(this, images);
    }

    class ImagesRVAdapter extends RecyclerView.Adapter<ImagesRVAdapter.ViewHolder> {
        ImageView image;

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.iv_item_rv);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ImageLoader.load(MainActivity.this, images.get(position).getUrl(), image);
        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }

    private void showInputUrlDialog() {
        final EditText editText = new EditText(this);

        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = editText.getText().toString();
                        if (url.isEmpty()) return;

                        MyImage image = new MyImage();
                        image.setUrl(url);
                        images.add(image);
                        rv.getAdapter().notifyDataSetChanged();

                        saveImages();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        showInputUrlDialog();
    }
}

package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codesgood.views.JustifiedTextView;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.categoryControl.CategoryController;

public class ReadBlogActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    JustifiedTextView desc, readBlogTitle;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_blog);

        Toolbar toolbar = findViewById(R.id.readBlogToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        readBlogTitle = findViewById(R.id.readBlogTitle);
        desc = findViewById(R.id.readBlogDesc);
        imageView = findViewById(R.id.readBlogImage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String title = bundle.getString("title");
            String description = bundle.getString("description");
            String imagePath = bundle.getString("image");
            String category = bundle.getString("category");
            setTitle(category);

            readBlogTitle.setText(title);
            desc.setText(description);
            Picasso.get().load(imagePath).fit().into(imageView);
        }




    }
}
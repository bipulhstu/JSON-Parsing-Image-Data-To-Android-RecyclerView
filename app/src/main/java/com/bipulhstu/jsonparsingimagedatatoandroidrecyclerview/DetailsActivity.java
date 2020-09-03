package com.bipulhstu.jsonparsingimagedatatoandroidrecyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    TextView name, description;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String Name, Image, Description;

        name = findViewById(R.id.titileTextView);
        image = findViewById(R.id.imgView);
        description = findViewById(R.id.description);


        //get data from HeroAdapter using getStringExtra
        Image = getIntent().getStringExtra("img");
        Name = getIntent().getStringExtra("name");
        Description = getIntent().getStringExtra("description");


        //set data to detailsActivity
        Picasso.get().load(Image).into(image);
        name.setText(Name);
        description.setText(Description);

    }
}
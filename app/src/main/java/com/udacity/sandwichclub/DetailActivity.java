package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView image;
    private TextView origin;
    private TextView alsoKnownAs;
    private TextView ingredients;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        image = findViewById(R.id.image_iv);
        origin = findViewById(R.id.origin_tv);
        alsoKnownAs = findViewById(R.id.also_known_as_tv);
        ingredients = findViewById(R.id.ingredients_tv);
        description = findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);


        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(image);

        origin.setText(getDisplayValue(sandwich.getPlaceOfOrigin()));
        alsoKnownAs.setText(getDisplayValue(sandwich.getAlsoKnownAs()));
        ingredients.setText(getDisplayValue(sandwich.getIngredients()));
        description.setText(getDisplayValue(sandwich.getDescription()));
    }

    private String getDisplayValue(Object value) {
        if (value instanceof String) {
            return (TextUtils.isEmpty(String.valueOf(value))) ?
                    getString(R.string.detail_not_available) : String.valueOf(value);
        }

        if (value instanceof List) {
            return (CollectionUtils.isEmpty((List) value)) ?
                    getString(R.string.detail_not_available) : TextUtils.join(", ", (List) value);
        }

        return getString(R.string.detail_not_available);
    }
}

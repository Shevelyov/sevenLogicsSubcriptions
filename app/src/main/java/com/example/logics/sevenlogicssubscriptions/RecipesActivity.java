package com.example.logics.sevenlogicssubscriptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RecipesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_recipes);
    }

    public void onClickAddRecipe(View view){
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }
}

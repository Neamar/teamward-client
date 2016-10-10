package fr.neamar.lolgamedata;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import fr.neamar.lolgamedata.pojo.Player;

public class ChampionDetailActivity extends AppCompatActivity {
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        player = (Player) getIntent().getSerializableExtra("player");

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setTitle(player.champion.name);
        final ImageView splashArtImage = (ImageView) findViewById(R.id.splashArt);
        ImageLoader.getInstance().loadImage(player.champion.splashUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                splashArtImage.setImageBitmap(loadedImage);
                Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                        float[] hsv = new float[3];
                        Color.colorToHSV(vibrantColor, hsv);
                        hsv[2] *= 0.8f; // value component
                        int vibrantColorDark = Color.HSVToColor(hsv);

                        collapsingToolbarLayout.setContentScrimColor(vibrantColor);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(vibrantColorDark);
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_gg) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(player.champion.ggUrl));
            startActivity(browserIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champion_detail, menu);

        return true;
    }
}

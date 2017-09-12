package dejssa.justmoney;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindInterface();


        startUpdate();
    }

    private void bindInterface(){
        TextView[] titles = {(TextView) findViewById(R.id.names), (TextView) findViewById(R.id.nbrb), (TextView) findViewById(R.id.nbp)};
        for(TextView view : titles)
            setFont(view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.update);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdate();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.result_screen);
    }

    private void startUpdate(){
        String[] currency_codes = {"USD", "EUR", "RUB", "PLN"};

        LoadCurrency loadCurrency = new LoadCurrency(this, currency_codes);
        Thread s = new Thread(loadCurrency);
        s.start();

    }

    public RelativeLayout getMainScreen(){
        return (RelativeLayout) findViewById(R.id.main_screen);
    }

    public void setFont(TextView textView){
        AssetManager manager = this.getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(manager, "fonts/akrobat_semi_bold.otf");
        textView.setTypeface(typeface);
    }




}


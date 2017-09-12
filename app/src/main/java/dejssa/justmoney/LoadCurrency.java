package dejssa.justmoney;

import android.support.design.widget.Snackbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Алексей on 11.09.2017.
 */

public class LoadCurrency implements Runnable{
    private MainActivity activity;

    private ArrayList<Currency> currencies_nbrb = new ArrayList<>();
    private ArrayList<Currency> currencies_pln= new ArrayList<>();

    private String[] currency_codes;

    public LoadCurrency(MainActivity activity, String[] currency_codes) {
        this.activity = activity;
        this.currency_codes = currency_codes;
    }

    @Override
    public void run() {
        boolean good[] = {true,true};

        for(int i = 0; i < currency_codes.length; i++){
            try {
                connectToPage("http://www.nbrb.by/API/ExRates/Rates/" + currency_codes[i] + "?ParamMode=2", currencies_nbrb);
            } catch (IOException | JSONException e) {
                good[0] = false;
                e.printStackTrace();
            }
        }
        //NO PLN HERE
        for(int i = 0; i < currency_codes.length-1; i++){
            try {
                connectToPage("http://api.nbp.pl/api/exchangerates/rates/a/" + currency_codes[i] + "?format=json", currencies_pln);
            } catch (IOException | JSONException e) {
                good[1] = false;
                e.printStackTrace();
            }
        }

        update(good);

    }

    private URL prepareURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        return url;
    }

    private void connectToPage(String urlString, ArrayList<Currency> currencies) throws IOException, JSONException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(   prepareURL (urlString).openStream()    ));

        StringBuilder report = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            report.append(line + "\n");
        }

        bufferedReader.close();

        String jsonString = report.toString();
        System.out.println("JSON: " + jsonString);
        parse(new JSONObject(jsonString), currencies);
    }

    private void parse(JSONObject object, ArrayList<Currency> currencies) throws JSONException{
        if(currencies.equals(currencies_nbrb)){
            parseBy(object, currencies);
        }
        else{
            parsePl(object, currencies);
        }
    }

    private void parseBy(JSONObject container, ArrayList<Currency> currencies) throws JSONException{

        String value = container.getString("Cur_OfficialRate");
        String name = container.getString("Cur_Abbreviation");

        currencies.add(new Currency(name, value));

        Log.v("Parsed value : BY" ,  name + " " + value);
    }

    private void parsePl(JSONObject container, ArrayList<Currency> currencies)  throws JSONException{
        Log.v("Parsed value : PL" ,  container.toString());

        JSONArray array = container.getJSONArray("rates");
        JSONObject obj = array.getJSONObject(0);

        String value = obj.getString("mid");
        String name = container.getString("code");

        currencies.add(new Currency(name, value));

        Log.v("Parsed value : PL" ,  name + " " + value);
    }

    private void update(boolean done[]){
        if(done[0]) {
            System.out.println(currencies_nbrb.toString());
            runUiToast("Updated");

        }
        else{
            runUiToast("Belarus bank is not available");
        }
        if(done[1]) {
            System.out.println(currencies_nbrb.toString());
            runUiToast("Updated");

        }
        else{
            runUiToast("Belarus bank is not available");
        }
    }

    private void runUiToast(final String txt){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(activity.getMainScreen(), txt,Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

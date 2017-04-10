package com.devmicheledonato.airto.utils;

import android.net.Uri;
import android.util.Log;

import com.devmicheledonato.airto.BuildConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Michele on 09/04/2017.
 */

public class AirToNetworkUtils {

    private static final String TAG = AirToNetworkUtils.class.getSimpleName();

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast";

    private static final String CITY_PARAM = "id";
    private static final String TURIN_ID = "3165524";

    // API_KEY
    private static final String API_PARAM = "appid";

    private static final String UNITS_PARAM = "units";
    private static final String METRIC = "metric";
    private static final String IMPERIAL = "imperial";

    private static final String IPQA_URL =
            "http://www.cittametropolitana.torino.it/cms/ambiente/qualita-aria/dati-qualita-aria/ipqa";

    /**
     * Builds the URL used to talk to the weather server.
     *
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl() {
        Uri weatherQueryUri = Uri.parse(WEATHER_URL).buildUpon()
                .appendQueryParameter(CITY_PARAM, TURIN_ID)
                .appendQueryParameter(API_PARAM, BuildConfig.OW_API_KEY)
                .appendQueryParameter(UNITS_PARAM, METRIC)
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String[] getIPQAData() throws IOException {
        String[] ipqa = new String[4];

        Document doc = Jsoup.connect(IPQA_URL).timeout(0).get();
        Element table = doc.select("table[class=dati] > tbody").first();
        Iterator<Element> iteratorTR = table.select("tr").iterator();
        Iterator<Element> iteratorTH = table.select("th").iterator();

        ipqa[0] = iteratorTH.next().text();
        ipqa[1] = iteratorTH.next().text();

        iteratorTR.next();
        Iterator<Element> iteratorTD = table.select("td").iterator();

        ipqa[2] = iteratorTD.next().text().split(" ")[1];
        ipqa[3] = iteratorTD.next().text().split(" ")[1];

        return ipqa;
    }

}

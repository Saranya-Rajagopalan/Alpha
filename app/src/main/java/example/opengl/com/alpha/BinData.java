package example.opengl.com.alpha;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by srajago on 17-11-2016.
 */

public class BinData {
    public int id;
    public String binName;
    private int capacity;
    private double Latitude;
    private double Longitude;
    public int filledPercentage;
    public String time;
    public LatLng location;


    BinData(JSONObject object) {
        try {
            this.id = object.getInt("ID");
            this.binName = object.getString("BinName");
            this.capacity = object.getInt("Capacity");
            this.Longitude = object.getDouble("LatLocation");
            this.Latitude = object.getDouble("LongLocation");
            this.filledPercentage = object.getInt("FilledPercentage");
            this.time = object.getString("LastCollectedTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static float percentageToColor(double percentage) {
        if (percentage > 80) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if (percentage < 80 && percentage > 40) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }


    void MarkOnMap(GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(this.Latitude, this.Longitude)).title(this.binName)).setIcon(BitmapDescriptorFactory.defaultMarker(percentageToColor(this.filledPercentage)));
    }
}
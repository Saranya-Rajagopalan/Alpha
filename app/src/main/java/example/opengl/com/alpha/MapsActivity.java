package example.opengl.com.alpha;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, NetworkCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button button= (Button)findViewById(R.id.button2);
        button.setOnClickListener(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        NetworkCalls.get(this, "http://rrajend-in-le01/GarbageWebAPI/api/BinInfo");
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void success(String body) {
        Handler handler = new Handler(getMainLooper());
        final String jsonString = body;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                JSONArray array;
                if (mMap == null) {
                    return;
                }
                try {
                    array = new JSONArray(jsonString);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        BinData bin = new BinData(object);
                        bin.MarkOnMap(mMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    public void failure() {


    }

    @Override
    public void onClick(View view) {
        NetworkCalls.get(this, "http://rrajend-in-le01/GarbageWebAPI/api/BinInfo");
    }
}

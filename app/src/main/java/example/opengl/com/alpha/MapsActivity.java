package example.opengl.com.alpha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, NetworkCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button refresh_button= (Button)findViewById(R.id.button2);
        refresh_button.setOnClickListener(this);
        Button details_button= (Button)findViewById(R.id.button3);
        details_button.setOnClickListener(this);
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),"Marker clicked"+marker.getTitle(),Toast.LENGTH_SHORT).show();
                NetworkCalls.get(new NetworkCallback() {
                    @Override
                    public void success(final String body) {
                        Handler handler = new Handler(getMainLooper());
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final BinData binData = new BinData(new JSONObject(body));
                                    ((TextView) findViewById(R.id.id_entry)).setText(String.valueOf(binData.id));
                                    ((TextView) findViewById(R.id.percentage_entry)).setText(String.valueOf(binData.filledPercentage));
                                    ((TextView) findViewById(R.id.capacity_entry)).setText(String.valueOf(binData.capacity));
                                    ((TextView) findViewById(R.id.last_collected_entry)).setText(binData.time);
                                    ((TextView) findViewById(R.id.type_entry)).setText(binData.binName);
                                } catch (
                                        JSONException e
                                        ) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        handler.post(runnable);

                    }

                    @Override
                    public void failure() {

                    }
                }, "http://rrajend-in-le01/GarbageWebAPI/api/BinInfo/"+marker.getTitle());
                return true;
            }
        });
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
        switch (view.getId()){
            case R.id.button2: NetworkCalls.get(this, "http://rrajend-in-le01/GarbageWebAPI/api/BinInfo");
                break;
            case R.id.button3:
                Intent intent = new Intent(this, DetailsActivity.class);
                startActivity(intent);
        }

    }
}

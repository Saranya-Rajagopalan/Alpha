package example.opengl.com.alpha;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class DetailsActivity extends AppCompatActivity implements NetworkCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        NetworkCalls.get(this, "http://rrajend-in-le01/GarbageWebAPI/api/BinInfo");
    }

    public void success(final String body) {
        Handler handler = new Handler(getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                JSONArray array;
                try {
                    array = new JSONArray(body);
                    Adapter ad = new Adapter(array, getApplicationContext());
                    ListView list = (ListView)findViewById(R.id.ListData);
                    list.setAdapter(ad);

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
private class Adapter extends BaseAdapter{
    private JSONArray array;
    private LayoutInflater inflater;

    Adapter(JSONArray jsonarray, Context context){
        this.array = jsonarray;
        this.inflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return array.getJSONObject(i);
        } catch (JSONException e) {

        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.bin_data_layout, null);
        final BinData binData;
        try {
            binData = new BinData(array.getJSONObject(i));
            ((TextView) view.findViewById(R.id.id_entry)).setText(String.valueOf(binData.id));
            ((TextView) view.findViewById(R.id.capacity_entry)).setText(String.valueOf(binData.capacity));
            ((TextView) view.findViewById(R.id.last_collected_entry)).setText(binData.time);
            ((TextView) view.findViewById(R.id.type_entry)).setText(binData.binName);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar.setProgress(binData.filledPercentage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
}


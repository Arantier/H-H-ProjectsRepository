package ru.android_school.h_h.sevenapp.MainActivity;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;

public class BridgeJSONAdapter extends TypeAdapter {
    
    public static  final String TAG = "JSON_Adapter";

    public void skipUnwantedValues(JsonReader in) throws IOException {
        in.skipValue();
        in.beginObject();
        while (in.hasNext()) {
            in.skipValue();
        }
        in.endObject();
    }

    public void parseIntervals(Bridge bridge, JsonReader in) throws IOException {
        in.beginArray();
        Log.i(TAG,"Parsing intervals");
        while (in.hasNext()) {
            Log.i(TAG,"Got interval");
            String start="", end="";
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                Log.i(TAG,"\tParsing field "+name);
                switch (name) {
                    case "end":
                        end = in.nextString();
                        break;
                    case "start":
                        start = in.nextString();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            Log.i(TAG,"\tReceived values:"+start+"\n"+end);
            bridge.addInterval(start.substring(0,5),end.substring(0,5));
            Log.i(TAG,"Last received value:"+ bridge.getIntervals().get(bridge.getIntervals().size()-1));
        }
        in.endArray();
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {

    }

    @Override
    public Object read(JsonReader in) throws IOException {
        List<Bridge> bridgeList = new ArrayList<>();
        Log.i(TAG, "Reader start working");
        in.beginObject();
        skipUnwantedValues(in);
        in.skipValue();
        in.beginArray();
        while (in.hasNext()) {
            Bridge newBridge = new Bridge();
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                Log.i(TAG,"Parsed field "+name);
                switch (name) {
                    case "id":
                        newBridge.setId(in.nextInt());
                        Log.i(TAG,"parsing...");
                        break;
                    case "name":
                        newBridge.setName(in.nextString());
                        Log.i(TAG,"parsing...");
                        break;
                    case "description":
                        newBridge.setDescription(in.nextString());
                        Log.i(TAG,"parsing...");
                        break;
                    case "lat":
                        newBridge.setLatitude(in.nextDouble());
                        Log.i(TAG,"parsing...");
                        break;
                    case "lng":
                        newBridge.setLongitude(in.nextDouble());
                        Log.i(TAG,"parsing...");
                        break;
                    case "photo_open":
                        newBridge.setPhotoBridgeClosedURL("http://gdemost.handh.ru/"+in.nextString());
                        Log.i(TAG,"parsing...");
                        break;
                    case "divorces":
                        parseIntervals(newBridge, in);
                        Log.i(TAG,"parsing...");
                        break;
                    case "photo_close":
                        newBridge.setPhotoBridgeOpenURL("http://gdemost.handh.ru/"+in.nextString());
                        Log.i(TAG,"parsing...");
                        break;
                    default:
                        in.skipValue();
                        Log.i(TAG,"parsing...");
                        break;
                }
            }
            in.endObject();
            Log.i(TAG,"Bridge has been parsed:\n"+newBridge);
            bridgeList.add(newBridge);
        }
        in.endArray();
        in.endObject();
        Log.i(TAG, "Reader stop working");
        return bridgeList;
    }

}

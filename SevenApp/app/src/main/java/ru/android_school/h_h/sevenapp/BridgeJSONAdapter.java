package ru.android_school.h_h.sevenapp;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.android_school.h_h.sevenapp.support_classes.Bridge;

public class BridgeJSONAdapter extends TypeAdapter {
    @Override
    public void write(JsonWriter out, Object value) throws IOException {

    }

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
        Log.i("retrofitTestIntervals","Parsing intervals");
        while (in.hasNext()) {
            Log.i("retrofitTestIntervals","Got interval");
            String start="", end="";
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                Log.i("retrofitTestIntervals","\tParsing field "+name);
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
            Log.i("retrofitTestIntervals","\tReceived values:"+start+"\n"+end);
            bridge.addInterval(start.substring(0,5),end.substring(0,5));
            Log.i("retrofitTestIntervals","Last received value:"+bridge.bridgeIntervals.get(bridge.bridgeIntervals.size()-1));
        }
        in.endArray();
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        List<Bridge> bridgeList = new ArrayList<>();
        Log.i("JsonReader", "Reader start working");
        in.beginObject();
        skipUnwantedValues(in);
        in.skipValue();
        in.beginArray();
        while (in.hasNext()) {
            Bridge newBridge = new Bridge();
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                Log.i("retrofitTest","Parsed field "+name);
                switch (name) {
                    case "id":
                        newBridge.setId(in.nextInt());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "name":
                        newBridge.setName(in.nextString());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "name_eng":
                        newBridge.setNameEng(in.nextString());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "description":
                        newBridge.setDescription(in.nextString());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "description_eng":
                        newBridge.setDescriptionEng(in.nextString());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "lat":
                        newBridge.setLatitude(in.nextDouble());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "lng":
                        newBridge.setLongtitude(in.nextDouble());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "photo_open":
                        newBridge.setPhotoBridgeClosedURL("http://gdemost.handh.ru/"+in.nextString());
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "divorces":
                        parseIntervals(newBridge, in);
                        Log.i("retrofitTest","parsing...");
                        break;
                    case "photo_close":
                        newBridge.setPhotoBridgeOpenURL("http://gdemost.handh.ru/"+in.nextString());
                        Log.i("retrofitTest","parsing...");
                        break;
                    default:
                        in.skipValue();
                        Log.i("retrofitTest","parsing...");
                        break;
                }
            }
            in.endObject();
            Log.i("retrofitTestBridge","Bridge has been parsed:\n"+newBridge);
            bridgeList.add(newBridge);
        }
        in.endArray();
        in.endObject();
        Log.i("JsonReader", "Reader stop working");
        return bridgeList;
    }

}

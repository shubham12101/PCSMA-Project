package com.pcsma.ifhtt.mainApp.Methods;

import android.util.Log;
import com.pcsma.ifhtt.mainApp.Objects.LocationObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Shubham on 15 Apr 15.
 */
public class JSONParser {

    private static String message;
    private static String TAG;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public static List<LocationObject> locationObjectList = new CopyOnWriteArrayList<>();

    public JSONParser(String message)
    {
        TAG=this.getClass().getSimpleName();
        this.message=message;
    }
    public List<LocationObject> parse()
    {
        try {
            jsonObject = new JSONObject(message);
            jsonArray = jsonObject.getJSONArray("occupancy_information");
            for(int i=0; i<jsonArray.length(); ++i)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Log.d(TAG,jsonObject1.toString());
                String building = jsonObject1.getString("building");
                String wing = jsonObject1.getString("wing");
                String floor = jsonObject1.getString("floor");
                String room = jsonObject1.getString("room");
                if(!(building.isEmpty() || floor.isEmpty()))
                {
                    LocationObject loc = new LocationObject();
                    loc.setBuilding(building + " Building");
                    loc.setFloor("Floor " + floor);
                    loc.setRoom("Room " + room);
                    loc.setWing("Wing " + wing);
                    if(!(locationObjectList.contains(loc)))
                        locationObjectList.add(loc);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationObjectList;
    }
}

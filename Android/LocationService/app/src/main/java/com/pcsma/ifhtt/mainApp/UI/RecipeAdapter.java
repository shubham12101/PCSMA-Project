package com.pcsma.ifhtt.mainApp.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pcsma.ifhtt.R;
import com.pcsma.ifhtt.mainApp.ActionObject;

import java.util.ArrayList;

/**
 * Created by vedantdasswain on 15/04/15.
 */
public class RecipeAdapter extends ArrayAdapter<ActionObject> {

    ArrayList<ActionObject> recipeList=new ArrayList<ActionObject>();
    Context context;

    public RecipeAdapter(Context context, ArrayList<ActionObject> recipeList) {
        super(context, R.layout.list_item_recipe, recipeList);
        this.context=context;
        this.recipeList=recipeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_recipe, parent, false);
        TextView tv=(TextView) rowView.findViewById(R.id.textLocation);
        tv.setText(recipeList.get(position).getLocation());
        tv=(TextView) rowView.findViewById(R.id.textStartTime);
        tv.setText(recipeList.get(position).getStartTime());
        tv=(TextView) rowView.findViewById(R.id.textEndTime);
        tv.setText(recipeList.get(position).getEndTime());
        tv=(TextView) rowView.findViewById(R.id.textAction);
        tv.setText(recipeList.get(position).getAction());
        tv=(TextView) rowView.findViewById(R.id.textOption);
        tv.setText(recipeList.get(position).getOption1());
        return rowView;
    }
}

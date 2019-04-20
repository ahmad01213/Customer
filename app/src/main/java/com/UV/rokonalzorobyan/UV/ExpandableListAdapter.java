package com.UV.rokonalzorobyan.UV;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private ArrayList<String> groupList;
    static String itemIdOrder,itemCost,itemImage,itemtitle,itemDetail;
    public ExpandableListAdapter(Activity context,ArrayList<String> groupList) {
        this.context = context;
        this.groupList=groupList;
    }
    public Object getChild(int groupPosition, int childPosition) {
        try {
            return FoodListActivity.wholeData.getData().get(groupPosition).getItems().get(0).get(childPosition).getItemName();
        }catch (Exception e){
            return "";
        }
    }
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
          itemtitle =(String) getChild(groupPosition, childPosition);
          itemImage =FoodListActivity.wholeData.getData().get(groupPosition).getItems().get(0).get(childPosition).getItemImage();
         itemCost =FoodListActivity.wholeData.getData().get(groupPosition).getItems().get(0).get(childPosition).getItemPrice();
         itemDetail =FoodListActivity.wholeData.getData().get(groupPosition).getItems().get(0).get(childPosition).getItemDetails();
         itemIdOrder =FoodListActivity.wholeData.getData().get(groupPosition).getItems().get(0).get(childPosition).getItemKey()+"";
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }
        TextView itemTitle = convertView.findViewById(R.id.item_title);
        TextView itemPrice = convertView.findViewById(R.id.item_cost);
        TextView mDesc = convertView.findViewById(R.id.item_desc);
        TextView mImgurl = convertView.findViewById(R.id.imgUrl);
        TextView mId = convertView.findViewById(R.id.id_order);
        ImageView mItemImag= convertView.findViewById(R.id.item_img);
        Glide.with(context)
                .load(itemImage)
                .into(mItemImag);
        itemPrice.setText(itemCost);
        itemTitle.setText(itemtitle);
        mDesc.setText(itemDetail);
        mId.setText(itemIdOrder);
        mImgurl.setText(itemImage);
        return convertView;
    }
    public int getChildrenCount(int groupPosition) {
        return FoodListActivity.wholeData.getData().get(groupPosition).getItems().get(0).size();
    }
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }
    public int getGroupCount() {
        return groupList.size();
    }
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupString = groupList.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.group_item_text);
        item.setText(groupString);
        ImageView arrow=convertView.findViewById(R.id.group_item_arraw);
        if (isExpanded) {
            arrow.setImageResource(R.drawable.arrow);
        } else {
          arrow.setImageResource(R.drawable.back);
        }
        return convertView;
    }
    public boolean hasStableIds() {
        return true;
    }
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
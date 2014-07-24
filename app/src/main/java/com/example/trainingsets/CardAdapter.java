package com.example.trainingsets;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Card> cardList;
	
	public CardAdapter(Context context, ArrayList<Card> cardList){
		
		this.context = context;
		this.cardList = cardList;
	}

	@Override
	public int getCount() {
		
		return cardList.size();
	}

	@Override
	public Object getItem(int index) {
		
		return cardList.get(index);
	}

	@Override
	public long getItemId(int index) {
		
		return index;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		
		LayoutInflater layoutInflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = layoutInflater.inflate(R.layout.list_adapter_card, null);
		//view.setBackgroundColor(Color.parseColor("#D1D1D5"));
		
		TextView name = (TextView) view.findViewById(R.id.adapterExerciseName);
		TextView training = (TextView) view.findViewById(R.id.adapterTraining);
		TextView startDate = (TextView ) view.findViewById(R.id.adapterStartDate);
		TextView endDate = (TextView ) view.findViewById(R.id.adapterEndDate);
		
		name.setText(cardList.get(index).getName());
		training.setText(Integer.toString((cardList.get(index).getTraining()))); 
		startDate.setText(cardList.get(index).getStartDate());
		endDate.setText(cardList.get(index).getEndDate());
		
		return view;
	}
}


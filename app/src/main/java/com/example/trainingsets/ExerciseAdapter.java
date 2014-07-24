package com.example.trainingsets;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExerciseAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Exercise> exerciseList;
	private int callingCard;
	private int callingTraining;

	public ExerciseAdapter(Context context, int callingCard,
			int callingTraining, ArrayList<Exercise> exerciseList) {

		this.callingCard = callingCard;
		this.callingTraining = callingTraining;
		this.context = context;
		this.exerciseList = exerciseList;
	}

	@Override
	public int getCount() {

		return exerciseList.size();
	}

	@Override
	public Object getItem(int index) {

		return exerciseList.get(index);
	}

	@Override
	public long getItemId(int index) {

		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if ((callingCard == TrainingActivity.fichaExecutando)
				&& (callingTraining == TrainingActivity.treinoExecutando)
				&& (index == TrainingActivity.exercicioExecutando)) {

			view = layoutInflater.inflate(
					R.layout.list_adapter_exercise_playing, null);

		} else {

			view = layoutInflater.inflate(R.layout.list_adapter_exercise, null);
		}

		if (MainActivity.db.isChecked(exerciseList.get(index).getId(),
				exerciseList.get(index).getTreino(), exerciseList.get(index)
						.getName()) == 1) {

			view.setBackgroundResource(R.color.LightGreen);
		} else {

			if (index % 2 == 0) {

				view.setBackgroundResource(R.color.LightGrey);
			}
		}

		TextView seriesXrepeats = (TextView) view.findViewById(R.id.textSeriesXRepeats);
		TextView name = (TextView) view.findViewById(R.id.textAdapterExerciseName);
		TextView weight = (TextView) view.findViewById(R.id.textPeso);

		name.setText(exerciseList.get(index).getName());
		seriesXrepeats.setText(Integer.toString(exerciseList.get(index).getQntSeries())+
				"x"+Integer.toString(exerciseList.get(index)
						.getQntRepeats()));
		weight.setText(Integer.toString(exerciseList.get(index).getWeight())+" Kg");

		return view;
	}
}

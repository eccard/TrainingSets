package com.example.trainingsets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainingsets.R.id;

public class CurrentExercise extends ActionBarActivity implements
		android.view.View.OnClickListener, OnClickListener {

	public static int contSerieAtual = 0;

	private Exercise exercise;
	private int callingCard;
	private int callingTraining;
	private int callingExercise;
	private TextView name;
	private TextView peso;
	private TextView repeats;
	private TextView series;
	// private TextView nextExercise;
	private Intent newActivity;
	private ImageView imagemExercicio;
	private static Chronometer crono;
	private long time = 0;
	private int proximoExercicio;

	private static long timeWhenStopped = 0;
	private static long nowtime = 0;
	private static boolean stateCron;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_current_exercise);

		// Recebimento dos dados passados pela Activity anteriror
		callingCard = getIntent().getIntExtra("callingCard", 0);
		callingTraining = getIntent().getIntExtra("callingTraining", 0);
		callingExercise = getIntent().getIntExtra("callingExercise", 0);

		exercise = MainActivity.cardList.get(callingCard).getTrainingList()
				.get(callingTraining).getExerciseList().get(callingExercise);

		proximoExercicio = nextExercise(callingCard, callingTraining,
				callingExercise);

		actionBar = getSupportActionBar();
		actionBar.setTitle(MainActivity.cardList.get(callingCard)
				.getTrainingList().get(callingTraining).getName());
		actionBar.setSubtitle(MainActivity.cardList.get(callingCard).getName());
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Faz a identificação dos elementos presentes no layout
		name = (TextView) findViewById(R.id.textExerciseName);
		peso = (TextView) findViewById(R.id.textViewPesoValue);
		repeats = (TextView) findViewById(R.id.textViewRepeatsValue);
		series = (TextView) findViewById(R.id.textViewSeriesValue);
		imagemExercicio = (ImageView) findViewById(R.id.imageView_exercicio);

		name.setText(exercise.getName().toString());
		peso.setText(Integer.toString(exercise.getWeight()).toString());
		repeats.setText(Integer.toString((exercise.getQntRepeats())).toString());
		series.setText(Integer.toString(contSerieAtual) + "/"
				+ Integer.toString(exercise.getQntSeries()).toString());

		try {

			InputStream imagem = getAssets().open("supino_reto.jpg");
			Drawable d = Drawable.createFromStream(imagem, null);
			imagemExercicio.setImageDrawable(d);

		} catch (IOException e) {

		}

		// nextExercise = (TextView) findViewById(R.id.text_proximo_exercicio);

		// nextExercise.setText(nextExercise(callingCard, callingTraining,
		// callingExercise));

		crono = (Chronometer) findViewById(R.id.chronometer);
		// crono.setTextColor(Color.CYAN);
		crono.setOnClickListener(this);

		crono.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (stateCron == true) {
					timeWhenStopped = crono.getBase()
							- SystemClock.elapsedRealtime();
					CurrentExercise.nowtime = timeWhenStopped;
					crono.stop();
					stateCron = false;

				} else {

					// crono.setBase(SystemClock.elapsedRealtime()+time);
					crono.setBase(SystemClock.elapsedRealtime()
							+ timeWhenStopped);
					crono.start();
					stateCron = true;

				}
			}
		});

	}

	
	// Mostra uma mensagem avisando que todas as séries de um exercício foram
	// executadas
	public void mostrarMenssagemMaxSerie() {

		// Todos os contadores com informações do exercício atual são resetados
		TrainingActivity.fichaExecutando = -1;
		TrainingActivity.treinoExecutando = -1;
		TrainingActivity.exercicioExecutando = -1;
		CurrentExercise.contSerieAtual = 0;

		exercise.setDone(1);

		MainActivity.db.AtualizarChecked(callingCard + 1, callingTraining + 1,
				exercise.getName().toString(), 1);

		showAlertDialog();
	}

	// Conta a quantidade de séries realizadas em um exercício
	public void contagemSeries() {

		// Verifica se a quantidade de serie executadas é igual a quantidade
		// máxima de séries de um exercício
		if (CurrentExercise.contSerieAtual == exercise.getQntSeries() - 1) {

			CurrentExercise.contSerieAtual++;

			series.setText(Integer.toString(CurrentExercise.contSerieAtual)
					+ "/"
					+ Integer.toString(exercise.getQntSeries()).toString());

			mostrarMenssagemMaxSerie();

			crono.stop();
			crono.setVisibility(View.INVISIBLE);
			stateCron = false;

			// Se a quantidade for menor o contador é incrementado
		} else {

			crono.setBase(SystemClock.elapsedRealtime() + time);
			crono.start();
			stateCron = true;

			CurrentExercise.contSerieAtual++;

			series.setText(Integer.toString(CurrentExercise.contSerieAtual)
					+ "/"
					+ Integer.toString(exercise.getQntSeries()).toString());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			CurrentExercise.timeWhenStopped = crono.getBase();

			newActivity = new Intent(getApplicationContext(),
					TrainingActivity.class);
			newActivity.putExtra("callingCard", callingCard);
			newActivity.putExtra("insertedTab", callingTraining);

			finish();
			startActivity(newActivity);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater menuinflanter = getMenuInflater();
		menuinflanter.inflate(R.menu.action_bar_current_exercise, menu);

		return super.onCreateOptionsMenu(menu);

	}

	public boolean onCreateActionMode(ActionMode mode, Menu menu) {

		mode.setTitle("Options");
		mode.getMenuInflater().inflate(R.menu.action_bar_context, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case id.menu_action_count_series:

			TrainingActivity.fichaExecutando = callingCard;
			TrainingActivity.treinoExecutando = callingTraining;
			TrainingActivity.exercicioExecutando = callingExercise;

			contagemSeries();

			crono.setBase(SystemClock.elapsedRealtime() + time);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {

		super.onResume();

		if (CurrentExercise.timeWhenStopped != 0 && stateCron != false) {

			Log.i("CurrentExercise", "-------------onResume state true");
			// crono.setBase(SystemClock.elapsedRealtime()-CurrentExercise.nowtime);
			// crono.setBase(CurrentExercise.nowtime);
			crono.setBase(CurrentExercise.timeWhenStopped);
			crono.start();
		}
		if (CurrentExercise.timeWhenStopped != 0 && stateCron == false) {
			Log.i("CurrentExercise", "-------------onResume state false");

			crono.setBase(SystemClock.elapsedRealtime() + nowtime); // quase
																	// funcionando
			timeWhenStopped = crono.getBase() - SystemClock.elapsedRealtime();

			// crono.getText();

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void showAlertDialog() {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(
				CurrentExercise.this);

		if (proximoExercicio != -1) {

			String nomeProximoExercicio = MainActivity.cardList
					.get(callingCard).getTrainingList().get(callingTraining)
					.getExerciseList().get(proximoExercicio).getName();

			mensagem.setTitle("Exercício Concluído");
			mensagem.setMessage("Próximo exercício:" + nomeProximoExercicio);
			mensagem.setPositiveButton("Ir para o próximo", this);
			mensagem.setNegativeButton("Voltar", this);

		} else {
			mensagem.setTitle("Parabéns!");
			mensagem.setMessage("Todos os exercícios foram concluídos!");
			mensagem.setNegativeButton("OK", this);
		}
		mensagem.create();
		mensagem.show();
	}

	public void showToast(String message) {

		Context context = getApplicationContext();
		CharSequence text = message;
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}

	@Override
	public void onClick(View view) {

		int id = view.getId();

		switch (id) {

		case R.id.chronometer:

			if (stateCron == true) {
				timeWhenStopped = crono.getBase()
						- SystemClock.elapsedRealtime();
				CurrentExercise.nowtime = timeWhenStopped;
				crono.stop();
				stateCron = false;

			} else {

				// crono.setBase(SystemClock.elapsedRealtime()+time);
				crono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
				crono.start();
				stateCron = true;

			}

			break;

		default:
			break;
		}

	}

	private int nextExercise(int callingCard, int callingTraining,
			int callingExercise) {

		ArrayList<Exercise> exerciseList = MainActivity.cardList
				.get(callingCard).getTrainingList().get(callingTraining)
				.getExerciseList();
		int i;

		if (callingExercise < exerciseList.size() - 1) {

			for (i = callingExercise; i < exerciseList.size() - 1; i++) {

				if (exerciseList.get(i + 1).getDone() == 0) {

					return i + 1;
				}
			}

			for (i = callingExercise; i > 0; i--) {

				if (exerciseList.get(i - 1).getDone() == 0) {

					return i - 1;
				}
			}

		} else {

			for (i = exerciseList.size() - 1; i > 0; i--) {

				if (exerciseList.get(i - 1).getDone() == 0) {

					return i - 1;
				}

			}
		}
		return -1;

	}

	@Override
	public void onClick(DialogInterface dialog, int id) {

		switch (id) {

		case Dialog.BUTTON_NEGATIVE:

			int qntExercicios = MainActivity.cardList.get(callingCard)
					.getTrainingList().get(callingTraining).getExerciseList()
					.size();

			int qntBanco = MainActivity.db.EndTreino(callingTraining + 1);

			if ((qntBanco == qntExercicios) && (qntBanco != 0)
					&& (qntExercicios != 0)) {

				showToast("Treino Concluído");
				toVibrate();
			}

			newActivity = new Intent(getApplicationContext(),
					TrainingActivity.class);
			finish();
			startActivity(newActivity);

			break;

		case Dialog.BUTTON_POSITIVE:

			newActivity = new Intent(getApplicationContext(),
					CurrentExercise.class);
			newActivity.putExtra("callingCard", callingCard);
			newActivity.putExtra("callingTraining", callingTraining);
			newActivity.putExtra("callingExercise", proximoExercicio);
			finish();
			startActivity(newActivity);

		}
	}

	private void toVibrate() {
		Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long milliseconds = 400;
		rr.vibrate(milliseconds);
	}
}
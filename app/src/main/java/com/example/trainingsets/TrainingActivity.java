package com.example.trainingsets;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class TrainingActivity extends ActionBarActivity implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener {

	private ExerciseAdapter exerciseAdapter;
	private ListView listView;
	private ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();
	private Intent newActivity;
	private int selectedItem;
	private boolean listViewClickable = true;
	public static int callingCard;
	public static int callingTraining;
	public static int fichaExecutando = -1;
	public static int treinoExecutando = -1;
	public static int exercicioExecutando = -1;
	public static ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise_list);

		actionBar = getSupportActionBar();
		actionBar.setTitle("Treino "
				+ Character.toString((char) (65 + callingTraining)));
		actionBar.setSubtitle(" "
				+ MainActivity.cardList.get(callingCard).getName());
		actionBar.setDisplayHomeAsUpEnabled(true);

		showExerciseList();
		Log.i("trainigactivity","testandooo 1 ");
	}

	public void showExerciseList() {

		exerciseList = MainActivity.cardList.get(callingCard).getTrainingList()
				.get(callingTraining).getExerciseList();
		exerciseAdapter = new ExerciseAdapter(this, callingTraining,
				callingTraining, exerciseList);
		listView = (ListView) findViewById(R.id.listViewExerciseList);
	
		listView.setAdapter(exerciseAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_training_activity, menu);

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		int trainingListSize = MainActivity.cardList.get(callingCard)
				.getTrainingList().size() - 1;

		MenuItem item;

		if (callingTraining == 0) {

			item = menu.findItem(R.id.menu_action_previous_training);
			item.setIcon(R.drawable.ic_action_previous_item_light);
			item.setEnabled(false);

		} else if (callingTraining == trainingListSize) {

			item = menu.findItem(R.id.menu_action_next_training);
			item.setIcon(R.drawable.ic_action_next_item_light);
			item.setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_action_new_exercise:

			newActivity = new Intent(getApplicationContext(),
					ExerciseActivity.class);

			newActivity.putExtra("callingCard", callingCard);
			newActivity.putExtra("callingTraining", callingTraining);
			newActivity.putExtra("flag", 'n');

			finish();
			startActivity(newActivity);

			break;

		case R.id.menu_action_next_training:

			callingTraining = callingTraining + 1;

			finish();
			startActivity(getIntent());
			break;

		case R.id.menu_action_previous_training:

			callingTraining = callingTraining - 1;

			finish();
			startActivity(getIntent());
			break;

		case R.id.menu_action_restore_training:
			Log.i("training","testes se é o botão de voltar !!");
			MainActivity.db
					.UnlockTraining(callingCard + 1, callingTraining + 1);

			for (int i = 0; i < exerciseList.size(); i++) {
				exerciseList.get(i).setDone(0);
			}

			exerciseAdapter.notifyDataSetChanged();
			break;

		/*case R.id.home:
			Log.i("teste", "testsssss");
			
//			finish();
//			NavUtils.navigateUpFromSameTask(this);
//			onBackPressed();
			return true;
		 */

		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {

		selectedItem = position;
		startSupportActionMode(modeCallBack);
		view.setSelected(true);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {

		if (listViewClickable == true) {
			int result = MainActivity.db.isChecked(callingCard + 1,
					callingTraining + 1, exerciseList.get(position).getName());

			if (result == 0) {

				// Verifica se o exercício selecionado já está sendo
				// executado ou se nenhum exercício foi iniciado ainda

				if ((checarExercicioEmExecucao(callingCard, callingTraining,
						position) == true)
						|| (TrainingActivity.fichaExecutando == -1
								&& TrainingActivity.treinoExecutando == -1 && TrainingActivity.exercicioExecutando == -1)) {

					newActivity = new Intent(getApplicationContext(),
							CurrentExercise.class);

					newActivity.putExtra("callingCard", callingCard);
					newActivity.putExtra("callingTraining", callingTraining);
					newActivity.putExtra("callingExercise", position);

//					finish(); não pode matar senão a actionbar não sabe de onde veio...
					startActivity(newActivity);

					// Verifica se a contagem de séries do exercício
					// selecionado já
					// foi iniciado pelo usuário)

				} else if (CurrentExercise.contSerieAtual >= 1) {

					String exerciseExecutingName = MainActivity.cardList
							.get(fichaExecutando).getTrainingList()
							.get(treinoExecutando).getExerciseList()
							.get(exercicioExecutando).getName();

					showNewSerieDialog(exerciseExecutingName);
				}

			} else {

				showAlertDialog("Alerta Exercício", exerciseList.get(position)
						.getName()
						+ " já foi feito. "
						+ "Utilize a opção HABILITAR EXERCÍCIOS no menu");

			}
		}
	}

	public boolean checarExercicioEmExecucao(int cartaoChamado,
			int treinoChamado, int exercicioChamado) {

		if (TrainingActivity.fichaExecutando == cartaoChamado
				&& TrainingActivity.treinoExecutando == treinoChamado
				&& TrainingActivity.exercicioExecutando == exercicioChamado) {

			return true;

		} else {

			return false;
		}
	}

	public void showAlertDialog(String titulo, String texto) {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(
				TrainingActivity.this);

		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("OK", this);
		mensagem.create();
		mensagem.show();
	}

	public void showNewSerieDialog(String executingExerciseName) {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(
				TrainingActivity.this);

		mensagem.setTitle("Atenção!");
		mensagem.setMessage("Você ja está executando: " + executingExerciseName);
		mensagem.setPositiveButton("Voltar para este exercício", this);
		mensagem.setNeutralButton("Ok", null);
		mensagem.setCancelable(false);
		mensagem.create();
		mensagem.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		switch (which) {

		case DialogInterface.BUTTON_POSITIVE:

			newActivity = new Intent(getApplicationContext(),
					CurrentExercise.class);

			newActivity.putExtra("callingCard", fichaExecutando);
			newActivity.putExtra("callingTraining", treinoExecutando);
			newActivity.putExtra("callingExercise", exercicioExecutando);

			finish();
			startActivity(newActivity);
			break;

		default:
			break;
		}
	}

	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			listViewClickable = false;
			return false;
		}

		public void onDestroyActionMode(ActionMode mode) {
			listViewClickable = true;
			mode = null;
		}

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			mode.setTitle("Opções");
			mode.getMenuInflater().inflate(R.menu.action_bar_context, menu);

			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {

			case R.id.action_bar_context_edit:

				newActivity = new Intent(getApplicationContext(),
						ExerciseActivity.class);
				newActivity.putExtra("callingCard", callingCard);
				newActivity.putExtra("callingTraining", callingTraining);
				newActivity.putExtra("callingExercise", selectedItem);
				newActivity.putExtra("flag", 'e');

				finish();
				startActivity(newActivity);

				break;

			case R.id.action_bar_context_delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TrainingActivity.this);
				builder.setTitle("Confirmação da Operação");
				builder.setMessage("Deseja mesmo deletar esse exercício?");
				builder.setPositiveButton("Não", null);
				builder.setNegativeButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Item selecionado é retirado do banco de dados
								MainActivity.db.DeletarExercise(exerciseList
										.get(selectedItem).getId(),
										exerciseList.get(selectedItem).getName(),
										exerciseList.get(selectedItem).getTreino());
								exerciseList.remove(selectedItem);
								exerciseAdapter.notifyDataSetChanged();
							}
						});
				builder.create();
				builder.setCancelable(false);
				builder.show();
				break;
			}
			return true;
		}
	};

	@Override
	protected void onStop() {
		
		finish();
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("traniningactivity", "presionado o botaõ return");
		
		switch(keyCode)
		{
		
		case KeyEvent.KEYCODE_BACK:
		
			newActivity = new Intent(getApplicationContext(),
					MainActivity.class);

			finish();
			startActivity(newActivity);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
/*
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		newActivity = new Intent(getApplicationContext(),
				MainActivity.class);

		finish();
		startActivity(newActivity);

	}*/
	
	
}

package com.example.trainingsets;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class ExerciseActivity extends ActionBarActivity implements OnItemClickListener{

	private int callingCard;
	private int callingTraining;
	private int callingExercise;
	private char flag;
	private Intent newActivity;
	// private EditText name;
	private AutoCompleteTextView name;
	private EditText muscularGroup;
	private EditText series;
	private EditText repeats;
	private EditText weight;
	public static String oldname;
	private ActionBar actionBar;
	private ArrayAdapter<String> adapterListaExercicios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exercise);

		actionBar = getSupportActionBar();
		actionBar.setTitle("Novo Exercício");

		adapterListaExercicios = new ArrayAdapter<String>(this,
				R.layout.list_item, MainActivity.autocompleteExercises);

		callingCard = getIntent().getIntExtra("callingCard", 0);
		callingTraining = getIntent().getIntExtra("callingTraining", 0);
		callingExercise = getIntent().getIntExtra("callingExercise", 0);
		flag = getIntent().getCharExtra("flag", 'n');

		// name = (EditText) findViewById(R.id.editTextExerciseName);
		muscularGroup = (EditText) findViewById(R.id.editTextMuscularGroup);
		series = (EditText) findViewById(R.id.editTextSeries);
		repeats = (EditText) findViewById(R.id.editTextRepeats);
		weight = (EditText) findViewById(R.id.editTextPeso);
		
		name = (AutoCompleteTextView) findViewById(R.id.editTextExerciseName);
		name.setAdapter(adapterListaExercicios);
		name.setOnItemClickListener(this);

		if (flag == 'e') {

			preencheDadosEditText();

		}
	}

	public void preencheDadosEditText() {
		
		actionBar.setTitle("Editar Exercício");
		
		String n = MainActivity.cardList.get(callingCard).getTrainingList()

		.get(callingTraining).getExerciseList().get(callingExercise).getName()
				.toString();
		name.setText(n);

		// Salva o nome atual
		oldname = n;

		String mg = MainActivity.cardList.get(callingCard).getTrainingList()
				.get(callingTraining).getExerciseList().get(callingExercise)
				.getMuscularGroup().toString();

		muscularGroup.setText(mg);

		String s = Integer.toString(
				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise).getQntSeries()).toString();

		series.setText(s);

		String rp = Integer.toString(
				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise).getQntRepeats()).toString();

		repeats.setText(rp);

		String peso = Integer.toString(
				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise).getWeight()).toString();

		weight.setText(peso);
	}

	public void newExercise() {

		try {

			// Validação do nome e criação do objeto Exercicio
			if (VerificaNome(name.getText().toString())
					|| MainActivity.cardList.get(callingCard).getTrainingList()
							.get(callingTraining).getExerciseList().isEmpty()) {

				int cardId = MainActivity.cardList.get(callingCard).getId();

				int treino = callingTraining + 1;

				Exercise newExercise = new Exercise(cardId, name.getText()
						.toString(), muscularGroup.getText().toString(),
						Integer.parseInt(series.getText().toString()),
						Integer.parseInt(repeats.getText().toString()),
						Integer.parseInt(weight.getText().toString()), treino);

				// Método de adição de exercicio no banco de dados
				MainActivity.db.InserirExercise(newExercise);

				// Método para adição do exercicio na array
				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.add(newExercise);

				newActivity = new Intent(getApplicationContext(),
						TrainingActivity.class);
				newActivity.putExtra("callingCard", callingCard);
				newActivity.putExtra("callingTraining", callingTraining);

				finish();
				startActivity(newActivity);
			}

			else {

				showAlertDialog("Nome Inválido", "Exercicio já existe");
			}

		} catch (NumberFormatException e) {

			showAlertDialog("Atenção", "Não deixe nenhum campo vazio.");
		}
	}

	// Método para editar um exercício
	public void editExercise() {

		try {

			String nome = name.getText().toString();
			String mg = muscularGroup.getText().toString();
			String serie = series.getText().toString();
			String rp = repeats.getText().toString();
			String peso = weight.getText().toString();

			// Validação do nome e atualizacao dos valores
			if (VerificaNome(name.getText().toString())
					|| name.getText().toString().equals(oldname)) {

				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise).setName(nome);

				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise).setMuscularGroup(mg);

				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise)
						.setQntSeries(Integer.parseInt(serie));

				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise)
						.setQntRepeats(Integer.parseInt(rp));

				MainActivity.cardList.get(callingCard).getTrainingList()
						.get(callingTraining).getExerciseList()
						.get(callingExercise).setWeight(Integer.parseInt(peso));

				// Atribuindo Numero da Ficha
				int id = MainActivity.cardList.get(callingCard)
						.getTrainingList().get(callingTraining)
						.getExerciseList().get(callingExercise).getId();

				// Atribuindo Numero do Treino
				int treino = MainActivity.cardList.get(callingCard)
						.getTrainingList().get(callingTraining)
						.getExerciseList().get(callingExercise).getTreino();

				// Criacao do objeto com seus valores para atualização
				// no banco de dados
				Exercise newExercise = new Exercise(id, nome, mg,
						Integer.parseInt(serie), Integer.parseInt(rp),
						Integer.parseInt(peso), treino);

				// Atualizacao no banco passando o novo objeto como
				// referencia
				MainActivity.db.AtualizarExercise(newExercise, oldname);

				newActivity = new Intent(getApplicationContext(),
						TrainingActivity.class);
				startActivity(newActivity);

			} else {

				showAlertDialog("Nome Inválido", "Exercicio já existe");
			}

		} catch (NumberFormatException e) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					ExerciseActivity.this);

			builder.setTitle("Atenção!");
			builder.setMessage("Não deixe nenhum campo vazio.");

			builder.setNeutralButton("Ok", null);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_exercise_activity, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_action_confirm_new_exercise:

			if (flag == 'n') {

				newExercise();

			} else {

				editExercise();
			}
			break;

		case R.id.menu_action_cancel_new_exercise:

			newActivity = new Intent(getApplicationContext(),
					TrainingActivity.class);
			newActivity.putExtra("callingCard", callingCard);
			newActivity.putExtra("callingTraining", callingTraining);

			finish();
			startActivity(newActivity);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean VerificaNome(String name) {

		// Percorre o ArrayList da aplicação procurando por nomes
		// iguais
		if (!MainActivity.cardList.get(callingCard).getTrainingList()
				.get(callingTraining).getExerciseList().isEmpty()) {

			for (int i = 0; i < MainActivity.cardList.get(callingCard)
					.getTrainingList().get(callingTraining).getExerciseList()
					.size(); i++) {

				if (name.equals(MainActivity.cardList.get(callingCard)
						.getTrainingList().get(callingTraining)
						.getExerciseList().get(i).getName())) {

					return false;
				}
			}
		}
		return true;
	}

	private void setGrupoMuscular(String exercicio){
		
		String grupoMuscular;
		grupoMuscular = MainActivity.db.getGrupoMuscular(exercicio);
		muscularGroup.setText(grupoMuscular);
	}
	
	private void showAlertDialog(String titulo, String texto) {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(
				ExerciseActivity.this);

		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("OK", null);
		mensagem.create();
		mensagem.show();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		
		String exercicio = (String) adapter.getItemAtPosition(position);
		setGrupoMuscular(exercicio);
	}
}
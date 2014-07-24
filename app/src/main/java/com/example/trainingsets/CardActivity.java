package com.example.trainingsets;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CardActivity extends ActionBarActivity {

	// Criação das variáveis que irão receber os valores passados para esta
	// activity através de 'Intents' com 'putExtras'
	int callingCard;
	char flag;
	public static String oldname;
	public static String oldtreino;
	static final int DATE_DIALOG_ID = 0;
	private EditText startDate;
	private EditText endDate;
	private String dateDPStart = null;
	private String dateDPEnd = null;
	private int flagDP;
	private Intent newActivity;
	private TextView train;
	private EditText name;
	private EditText training;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_card);
		getSupportActionBar().setTitle("Nova Ficha");

		flag = getIntent().getCharExtra("flag", 'n');
		callingCard = getIntent().getIntExtra("callingCard", 0);

		train = (TextView) findViewById(R.id.textTreinos);
		name = (EditText) findViewById(R.id.editTextName);
		training = (EditText) findViewById(R.id.editTextTraining);
		startDate = (EditText) findViewById(R.id.editTextStartDate);
		endDate = (EditText) findViewById(R.id.editTextEndDate);

		startDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				

				flagDP = 1;
				showDialog(DATE_DIALOG_ID);
				
			}
		});

		endDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				

				flagDP = 2;
				showDialog(DATE_DIALOG_ID);
				
			}
		});

		if (flag == 'e') {

			getSupportActionBar().setTitle("Editar Ficha");

			training.setFocusable(false);

			// training.setBackgroundResource(R.color.LightGrey);
			train.setTextColor(-3355444);
			training.setTextColor(-3355444);

			// Cada elemento da tela é preenchido com informações referentes a
			// ficha
			// selecionada para edição
			oldname = MainActivity.cardList.get(callingCard).getName()
					.toString();

			oldtreino = Integer.toString(MainActivity.cardList.get(callingCard)
					.getTraining());
			name.setText(oldname);

			training.setText(oldtreino);

			startDate.setText(MainActivity.cardList.get(callingCard)
					.getStartDate().toString());

			endDate.setText(MainActivity.cardList.get(callingCard).getEndDate()
					.toString());
		}
	}

	// Método para a criação de uma nova ficha
	public void newCard() throws IllegalArgumentException,
			NumberFormatException {

		Card fichaNova;

		if (VerificaNome(name.getText().toString())
				|| MainActivity.cardList.isEmpty()) {

			try {
				VerificaDataValida(startDate.getText().toString(), endDate
						.getText().toString());
				if (MainActivity.cardList.size() == 0) {
					fichaNova = new Card(1, name.getText().toString(),
							Integer.parseInt(training.getText().toString()),
							startDate.getText().toString(), endDate.getText()
									.toString());

				} else {

					int id = MainActivity.cardList.get(
							MainActivity.cardList.size() - 1).getId() + 1;

					fichaNova = new Card(id, name.getText().toString(),
							Integer.parseInt(training.getText().toString()),
							startDate.getText().toString(), endDate.getText()
									.toString());

				}

				MainActivity.cardList.add(fichaNova);
				MainActivity.db.InserirFicha(fichaNova);

				newActivity = new Intent(getApplicationContext(),
						TrainingActivity.class);

				newActivity.putExtra("callingCard",
						MainActivity.cardList.size() - 1);
				newActivity.putExtra("callingTraining", 0);

				finish();
				startActivity(newActivity);
			}

			catch (NumberFormatException e) {

				showAlertDialog("Atenção", "Nao deixe nenhum campo vazio");

			} catch (IllegalArgumentException e) {

				showAlertDialog("Atenção", "Datas Inválidas");
			}

		} else {

			showAlertDialog("Atenção",
					"Nome inválido, já existe uma ficha com este nome");
		}
	}

	// Método para a edição de uma ficha
	public void editCard() {

		try {

			// Identificação dos elementos da tela
			EditText name = (EditText) findViewById(R.id.editTextName);
			EditText training = (EditText) findViewById(R.id.editTextTraining);
			startDate = (EditText) findViewById(R.id.editTextStartDate);
			endDate = (EditText) findViewById(R.id.editTextEndDate);

			// Modificação dos dados da ficha que está sendo editada
			String nome = name.getText().toString();
			String t = training.getText().toString();
			String di = startDate.getText().toString();
			String df = endDate.getText().toString();
			VerificaDataValida(startDate.getText().toString(), endDate
					.getText().toString());
			
			// Validacao do nome e atualizacao
			if (VerificaNome(name.getText().toString())
					|| name.getText().toString().equals(oldname)) {
				
				MainActivity.cardList.get(callingCard).setName(nome);
				MainActivity.cardList.get(callingCard).setTraining(
						Integer.parseInt((t)));
				MainActivity.cardList.get(callingCard).setStartDate(di);
				MainActivity.cardList.get(callingCard).setEndDate(df);

				// Atualizacao no banco

				Card newCard = new Card(MainActivity.cardList.get(callingCard)
						.getId(), nome, Integer.parseInt(oldtreino), di, df);
				MainActivity.db.AtualizarFicha(newCard, oldname);

				finish();
				startActivity(new Intent(getApplicationContext(),
						MainActivity.class));
			} else {

				showAlertDialog("Atenção",
						"Nome inválido, já existe uma ficha com este nome.");
			}

		} catch (NumberFormatException e1) {

			showAlertDialog("Atenção", "Não deixe nenhum campo vazio.");

		} catch (IllegalArgumentException e) {

			showAlertDialog("Atenção",
					"A data de fim deve ser maior que a data de início.");
		}

		startDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				

				flagDP = 1;
				showDialog(DATE_DIALOG_ID);
				
			}
		});
		
		endDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				

				flagDP = 2;
				showDialog(DATE_DIALOG_ID);
				
			}
		});
	}

	// Funcao para validação do nome
	public boolean VerificaNome(String name) {
		if (!MainActivity.cardList.isEmpty()) {
			for (int i = 0; i < MainActivity.cardList.size(); i++) {
				if (name.equals(MainActivity.cardList.get(i).name)) {
					return false;
				}
			}
		}
		return true;
	}

	public void VerificaDataValida(String di, String df)
			throws IllegalArgumentException {
		String datai[];
		String dataf[];
		datai = di.split("\\ /");
		dataf = df.split("\\ /");
		GregorianCalendar gi = new GregorianCalendar(
				Integer.parseInt(datai[2]), Integer.parseInt(datai[1]),
				Integer.parseInt(datai[0]));
		GregorianCalendar gf = new GregorianCalendar(
				Integer.parseInt(dataf[2]), Integer.parseInt(dataf[1]),
				Integer.parseInt(dataf[0]));
		if ((gi.compareTo(gf) == 0) || (gi.after(gf))) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar calendario = Calendar.getInstance();

		int ano = calendario.get(Calendar.YEAR);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String data = String.valueOf(dayOfMonth) + " /"
					+ String.valueOf(monthOfYear + 1) + " /"
					+ String.valueOf(year);
			if (flagDP == 1) {
				dateDPStart = data;
				startDate = (EditText) findViewById(R.id.editTextStartDate);
				startDate.setText(dateDPStart);
			} else {
				dateDPEnd = data;
				endDate = (EditText) findViewById(R.id.editTextEndDate);
				endDate.setText(dateDPEnd);
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_card_activity, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_action_confirm_new_card:

			if (flag == 'n') {
				newCard();

			} else {

				editCard();
			}

			break;

		case R.id.menu_action_cancel_new_card:

			finish();
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));

		}
		return super.onOptionsItemSelected(item);
	}

	public void showAlertDialog(String titulo, String texto) {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(
				CardActivity.this);

		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("OK", null);
		mensagem.create();
		mensagem.show();
	}
}
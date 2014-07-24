package com.example.trainingsets;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.con.model.Fichax;
import com.example.con.webservice.FichaREST;

public class MainActivity extends ActionBarActivity implements
OnItemLongClickListener, OnItemClickListener {

	public static ArrayList<Card> cardList = new ArrayList<Card>();
	public static ArrayList<Exercise> bdexercise = new ArrayList<Exercise>();
	public ArrayList<String> expficha = new ArrayList<String>();
	public static ArrayList<String> autocompleteExercises = new ArrayList<String>();
	private Intent newActivity;
	private ListView listCard;
	private boolean listViewClickable = true;
	private CardAdapter listCardAdapter;
	private int selectedItem;

	// Classe de banco de dados;
	public static DataBaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ( autocompleteExercises.isEmpty()) {
			db = new DataBaseHelper(getApplicationContext());

			try {

				db.CriarDataBase();

			} catch (IOException e) {

				e.printStackTrace();
			}

			db.abrirDataBase();

			autocompleteExercises = db.getAllExercices();
			db.close();
		}
		if (cardList.isEmpty()) {

			db = new DataBaseHelper(getApplicationContext());

			try {

				db.CriarDataBase();

			} catch (IOException e) {

				e.printStackTrace();
			}

			db.abrirDataBase();

			MainActivity.cardList = db.carregaFichas();

			MainActivity.bdexercise = db.carregaExercicios();

			// Aloca os Exercicios do ArrayList auxiliar em suas respectivas
			// fichas
			for (int i = 0; i < MainActivity.bdexercise.size(); i++) {

				MainActivity.cardList
				.get(db.rowNumber(MainActivity.bdexercise.get(i)
						.getId()) - 1).getTrainingList()
						.get(MainActivity.bdexercise.get(i).getTreino() - 1)
						.getExerciseList().add(MainActivity.bdexercise.get(i));
			}
		}

		setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle("Minhas Fichas");

		checkExpiredCards();
		showCardList();

	}

	public void showCardList() {

		listCard = (ListView) findViewById(R.id.listViewCard);
		listCardAdapter = new CardAdapter(getApplicationContext(), cardList);

		listCard.setAdapter(listCardAdapter);

		listCard.setOnItemLongClickListener(this);
		listCard.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_main, menu);
		inflater.inflate(R.menu.webservice_main_activity, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_action_new_card:

			newActivity = new Intent(getApplicationContext(),
					CardActivity.class);
			newActivity.putExtra("flag", 'n');

			startActivity(newActivity);

			return true;
		case R.id.menu_sync:
			// caso selecione para buscar fichas do servidor realizar essa operação...
//			fetchJSON();
			connectWs();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	 public void connectWs() {
		 Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	        	 FichaREST fichaREST = new FichaREST();
	             try {
	            	 ArrayList<Fichax> listaFicha = (ArrayList<Fichax>) fichaREST.getListaFicha();
	                 for (int i=0;i< listaFicha.size();i++){
	                	 syncfichasws(listaFicha.get(i));
//	                	 Log.i("ws",listaFicha.get(i).getNome());
	                 }
	            	 
	             } catch (Exception e) {
	                 e.printStackTrace();
//	                 gerarToast(e.getMessage());
	             }
	         }
	      });

	       thread.start(); 	
	    }
	 public void syncfichasws(Fichax fichax){
		 Card fichaNova;
		 if (MainActivity.cardList.size() == 0) {
				fichaNova = new Card(1, fichax.getNome(),
						fichax.getQntTreinos(),
						fichax.getDataInicio(), fichax.getDataFim());

			} else {

				int id = MainActivity.cardList.get(
						MainActivity.cardList.size() - 1).getId() + 1;
				fichaNova = new Card(id, fichax.getNome(),
						fichax.getQntTreinos(),
						fichax.getDataInicio(), fichax.getDataFim());
				
			}

			MainActivity.cardList.add(fichaNova);
			MainActivity.db.InserirFicha(fichaNova);

			newActivity = new Intent(getApplicationContext(),
					MainActivity.class);

//			newActivity.putExtra("callingCard",
//					MainActivity.cardList.size() - 1);
//			newActivity.putExtra("callingTraining", 0);

			finish();
			startActivity(newActivity);

	 }



	@Override
	protected void onResume() {

		setContentView(R.layout.activity_main);
		showCardList();

		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long arg3) {

		if (listViewClickable == true) {

			newActivity = new Intent(getApplicationContext(),
					TrainingActivity.class);

			TrainingActivity.callingCard = position;
			TrainingActivity.callingTraining = 0;

			//	finish(); Não pode matar a Main porque se não não tem como voltar com actionbar
			startActivity(newActivity);
		}
	}

	public void showAlertDialog(String titulo, String texto) {

		AlertDialog.Builder mensagem = new AlertDialog.Builder(
				MainActivity.this);
		mensagem.setTitle(titulo);
		mensagem.setMessage(texto);
		mensagem.setNeutralButton("OK", null);
		mensagem.show();
	}

	public void AlertExpFicha(String s) {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Expiração de Fichas");
		builder.setMessage(s);
		builder.setNegativeButton("Ok", null);
		builder.setPositiveButton("Não lembrar novamente",

				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Item selecionado é retirado do banco de dados
				db.AtualizarCheckedExpFicha(1);
			}
		});
		builder.create();
		builder.setCancelable(false);
		builder.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {

		selectedItem = position;
		startSupportActionMode(modeCallBack);
		view.setSelected(true);
		return true;
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

			mode.setTitle("Options");
			mode.getMenuInflater().inflate(R.menu.action_bar_context, menu);

			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			switch (item.getItemId()) {

			case R.id.action_bar_context_edit:

				newActivity = new Intent(getApplicationContext(),
						CardActivity.class);
				newActivity.putExtra("callingCard", selectedItem);
				newActivity.putExtra("flag", 'e');

				finish();
				startActivity(newActivity);

				break;

			case R.id.action_bar_context_delete:
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("Confirmação da Operação");
				builder.setMessage("Deseja mesmo deletar essa ficha?");
				builder.setPositiveButton("Não", null);
				builder.setNegativeButton("Sim",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Item selecionado é retirado do banco de dados
						MainActivity.db.DeletarFicha(selectedItem);
						MainActivity.cardList.remove(selectedItem);
						listCardAdapter.notifyDataSetChanged();
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

	public void checkExpiredCards() {

		expficha = db.FichaExpirada();

		if (!(expficha.isEmpty())) {

			if (db.CheckedExpFicha() == 0) {

				db.AtualizarQntExpFicha(expficha.size());
				StringBuilder s = new StringBuilder();

				for (int i = 0; i < expficha.size(); i++) {

					s.append("| " + expficha.get(i) + " |   ");
				}

				showNotification(s.toString());
				db.AtualizarCheckedExpFicha(1);

			} else if (db.QntExpFicha() < expficha.size()) {

				db.AtualizarCheckedExpFicha(0);
				db.AtualizarQntExpFicha(expficha.size());
			}
		} else {

			db.AtualizarQntExpFicha(0);
			db.AtualizarCheckedExpFicha(0);
		}
	}

	private void showNotification(String s) {

		String DESDE_NOTIFICACION = "desdeNotification";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent notificacionIntent = new Intent(getApplicationContext(),
				MainActivity.class);

		notificacionIntent.putExtra(DESDE_NOTIFICACION, true);

		PendingIntent notificacionPendingIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, notificacionIntent, 0);

		android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this);

		builder.setContentTitle("Ficha(s) Expiradas")
		.setTicker("Notificação TrainingSets")
		.setSmallIcon(R.drawable.ic_launcher_training_sets)
		.setLargeIcon(
				BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher_training_sets))
						.setContentText("Fichas expiradas")
						.setContentIntent(notificacionPendingIntent);
		Notification notificacion = null;

		notificacion = new NotificationCompat.InboxStyle(builder).addLine(s)
				.build();

		notificacion.flags |= Notification.FLAG_AUTO_CANCEL;
		notificacion.defaults |= Notification.DEFAULT_SOUND;
		notificacion.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notificacion);
	}
	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
	@Override
	protected void onRestart() {
		checkExpiredCards();
		super.onRestart();
	}
	 public void fetchJSON(){
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	            URL url = new URL("http://10.0.0.103:8080/TSsystem2/ws/ficha/1");
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(10000 /* milliseconds */);
	            conn.setConnectTimeout(15000 /* milliseconds */);
	            conn.setRequestMethod("GET");
	            conn.setDoInput(true);
	            // Starts the query
	            conn.connect();
	         InputStream stream = conn.getInputStream();

	      String data = convertStreamToString(stream);

	      Log.i("ws",data);
//	      readAndParseJSON(data);
	         stream.close();

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 		
	   }
	   static String convertStreamToString(java.io.InputStream is) {
	      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	      return s.hasNext() ? s.next() : "";
	   }
	
}
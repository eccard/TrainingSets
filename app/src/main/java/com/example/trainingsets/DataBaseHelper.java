package com.example.trainingsets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	// Declaração do diretório e nome do arquivo do banco de dados.
	private static String DB_PATH = "/data/data/com.example.trainingsets/databases/";
	// private static String DB_NAME = "TrainingSets2.db";
	private static String DB_NAME = "TrainingSets2.1.db";
	public SQLiteDatabase dbQuery;
	private final Context dbContexto;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.dbContexto = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// Método para inserir ficha, passando os valores
	public void InserirFicha(Card a) throws NumberFormatException {

		ContentValues values = new ContentValues();
		values.put("id", a.id);
		values.put(Card.NOME, a.name);
		values.put(Card.QNTTREINO, a.training);
		values.put(Card.DATAINIC, a.startDate);
		values.put(Card.DATAFIM, a.endDate);
		InserirFicha(values);
	}

	// Inserir Ficha recebendo os valores e usando comando sql
	public void InserirFicha(ContentValues valores) {

		dbQuery.insert("ficha", null, valores);
	}

	public int getNewId(String name) {

		String[] nome = { name };
		int id;

		Cursor query = dbQuery.rawQuery("select id from ficha where nome = ?",
				nome);
		id = query.getInt(0);

		return id;
	}

	// Método para inserir exercicio, passando os valores
	public void InserirExercise(Exercise a) {

		ContentValues values = new ContentValues();
		values.put("id", a.getId());
		values.put(Exercise.NOME, a.getName());
		values.put(Exercise.GRUPOMUSCULAR, a.getMuscularGroup());
		values.put(Exercise.SERIE, a.getQntSeries());
		values.put(Exercise.REPETICAO, a.getQntRepeats());
		values.put(Exercise.PESO, a.getWeight());
		values.put("treino", a.getTreino());
		values.put("concluido", a.getDone());
		InserirExercise(values);
	}

	// Inserir Exercicio recebendo os valore e usando comando sql
	public void InserirExercise(ContentValues valores) {
		dbQuery.insert("exercicio", null, valores);
	}

	// Método para atualizar exercicio
	public void AtualizarExercise(Exercise a, String nome) {

		ContentValues values = new ContentValues();

		values.put(Exercise.NOME, a.getName());
		values.put(Exercise.GRUPOMUSCULAR, a.getMuscularGroup());
		values.put(Exercise.SERIE, a.getQntSeries());
		values.put(Exercise.REPETICAO, a.getQntRepeats());
		values.put(Exercise.PESO, a.getWeight());

		String where = "exercicio.id=? and nome=? and treino=?";

		String[] whereArgs = new String[] { String.valueOf(a.getId()), nome,
				String.valueOf(a.getTreino()) };

		AtualizarExercise(values, where, whereArgs);
	}

	// Método para atualizar exercicio , recebendo valores condiçoes e usando
	// sql
	public void AtualizarExercise(ContentValues valores, String where,
			String[] whereArgs) {
		dbQuery.update("exercicio", valores, where, whereArgs);
	}

	// Método para atualizar exercicio
	public void AtualizarFicha(Card a, String nome) {
		ContentValues values = new ContentValues();
		values.put(Card.NOME, a.name);
		values.put(Card.QNTTREINO, a.training);
		values.put(Card.DATAINIC, a.startDate);
		values.put(Card.DATAFIM, a.endDate);
		String where = "nome=?";
		String[] whereArgs = new String[] { nome };
		AtualizarFicha(values, where, whereArgs);
	}

	// Método para atualizar ficha , recebendo valores condiçoes e usando sql
	public void AtualizarFicha(ContentValues valores, String where,
			String[] whereArgs) {
		dbQuery.update("ficha", valores, where, whereArgs);
	}

	// Metodo para deletar ficha
	public void DeletarFicha(int i) {

		String[] nome = { MainActivity.cardList.get(i).getName() };
		dbQuery.delete("ficha", "nome = ?", nome);
	}

	// Metodo para deletar exercicio recebendo os valores
	public void DeletarExercise(int id, String nome, int treino) {

		String[] whereArgs = new String[] { String.valueOf(id), nome,
				String.valueOf(treino) };
		dbQuery.delete("exercicio", "id = ? and nome = ? and treino = ?",
				whereArgs);
	}

	// Criação do banco
	public void CriarDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (!dbExist) {
			this.getReadableDatabase();

			try {
				this.copiarDataBase();
			} catch (IOException e) {
				throw new Error("Erro ao copiar o Banco de Dados!");
			}
		}
	}

	private void copiarDataBase() throws IOException {

		InputStream myInput = dbContexto.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public void abrirDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		dbQuery = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		dbQuery.execSQL("PRAGMA foreign_keys = ON;");

	}

	// Carrega do banco de dados de fichas para cardlist estatico da aplicacao

	public ArrayList<Card> carregaFichas() {

		ArrayList<Card> cardl = new ArrayList<Card>();

		Cursor cursor = dbQuery.rawQuery("select * from ficha", null);
		if (cursor.moveToFirst()) {
			do {
				cardl.add(new Card(cursor.getInt(0), cursor.getString(1),
						cursor.getInt(2), cursor.getString(3), cursor
								.getString(4)));
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return cardl;
	}

	public ArrayList<String> FichaExpirada() {
		GregorianCalendar g = new GregorianCalendar();
		g.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("d /MM /yyyy");
		String formattedDate = df.format(g.getTime());
		String[] data = { formattedDate };
		ArrayList<String> fichasexpiradas = new ArrayList<String>();
		Cursor cursor = dbQuery.rawQuery(
				"select nome from ficha where datafim=? ", data);
		if (cursor.moveToFirst()) {
			do {
				fichasexpiradas.add(cursor.getString(0));
			} while (cursor.moveToNext());

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return fichasexpiradas;
	}

	public int CheckedExpFicha() {
		Cursor cursor = dbQuery.rawQuery(
				"select fichaexpirada from notificacoes where id=1", null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	public int QntExpFicha() {
		Cursor cursor = dbQuery.rawQuery(
				"select qntexp from notificacoes where id = 1", null);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	public void AtualizarCheckedExpFicha(int i) {

		ContentValues values = new ContentValues();

		values.put("fichaexpirada", i);

		AtualizarCheckedExpFicha(values);
	}

	public void AtualizarCheckedExpFicha(ContentValues valores) {
		dbQuery.update("notificacoes", valores, null, null);
	}

	public void AtualizarQntExpFicha(int i) {

		ContentValues values = new ContentValues();

		values.put("qntexp", i);

		AtualizarQntExpFicha(values);
	}

	public void AtualizarQntExpFicha(ContentValues valores) {
		dbQuery.update("notificacoes", valores, null, null);
	}

	public int lastCardID() {

		Cursor query = dbQuery.rawQuery(
				"select id from ficha where id = (select max(id) from ficha)",
				null);

		if (query.isNull(0)) {

			return 0;
		}
		return query.getInt(0);

	}

	public int rowNumber(int i) {

		String[] pos = { Integer.toString(i) };

		Cursor query;
		query = dbQuery
				.rawQuery(
						"select cnt from (select id, nome, "
								+ "(select count(*) from ficha b where a.id >= b.id) as cnt from ficha a) where id = ?",
						pos);
		query.moveToFirst();

		return query.getInt(0);
	}

	public int isChecked(int id, int treino, String nome) {

		Cursor query;
		int x = 9;
		String[] whereArgs = new String[] { String.valueOf(id), nome,
				String.valueOf(treino) };
		query = dbQuery
				.rawQuery(
						"select concluido from exercicio where id = ? and nome = ? and treino = ?",
						whereArgs);

		if (query != null && query.getCount() > 0) {
			x = 5;
			if (query.moveToFirst()) {
				x = query.getInt(query.getColumnIndex("concluido"));
			}
		}
		query.close();
		return x;
	}

	public int EndTreino(int treino) {
		Cursor query;
		int x = 0;
		String[] whereArgs = new String[] { String.valueOf(treino) };
		query = dbQuery.rawQuery(
				"select sum(concluido) from exercicio where treino=?",
				whereArgs);
		if (query != null && query.getCount() > 0) {
			if (query.moveToFirst()) {
				x = query.getInt(0);
			}
		}
		query.close();
		return x;
	}

	public void UnlockTraining(int id, int treino) {

		ContentValues values = new ContentValues();
		Log.i("UNLOCK", String.valueOf(id) + "   " + String.valueOf(treino));
		values.put("concluido", 0);

		String where = "id=? and treino=?";
		String[] whereArgs = new String[] { String.valueOf(id),
				String.valueOf(treino) };

		UnlockTraining(values, where, whereArgs);
	}

	// Método para atualizar exercicio , recebendo valores condiçoes e usando
	// sql
	public void UnlockTraining(ContentValues valores, String where,
			String[] whereArgs) {
		dbQuery.update("exercicio", valores, where, whereArgs);
	}

	public void DeleteAllExercises(int id, int treino) {
		dbQuery.execSQL("delete from exercicio  where id ="
				+ String.valueOf(id) + " and treino =" + String.valueOf(treino));
	}

	public void AtualizarChecked(int id, int treino, String nome, int status) {

		ContentValues values = new ContentValues();

		values.put("concluido", status);

		String where = "id=? and nome=? and treino=?";
		String[] whereArgs = new String[] { String.valueOf(id), nome,
				String.valueOf(treino) };

		AtualizarChecked(values, where, whereArgs);
	}

	// Método para atualizar exercicio , recebendo valores condiçoes e usando
	// sql
	public void AtualizarChecked(ContentValues valores, String where,
			String[] whereArgs) {
		dbQuery.update("exercicio", valores, where, whereArgs);
	}

	// Método para carga de dados dos exercicios do banco para a aplicacao
	public ArrayList<Exercise> carregaExercicios() {
		ArrayList<Exercise> exer = new ArrayList<Exercise>();

		Cursor cursor = dbQuery.rawQuery("select * from exercicio", null);

		if (cursor.moveToFirst()) {
			do {
				exer.add(new Exercise(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3),
						cursor.getInt(4), cursor.getInt(5), cursor.getInt(6)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return exer;
	}

	@Override
	public synchronized void close() {
		if (dbQuery != null)
			dbQuery.close();
		super.close();
	}

	public ArrayList<String> getAllExercices() {
		Cursor cursor = dbQuery.rawQuery(
				"select * from autocomplete_exercicios ", null);

		if (cursor.getCount() > 0) {
			ArrayList<String> str = new ArrayList<String>();

			while (cursor.moveToNext()) {
				str.add(cursor.getString(cursor.getColumnIndex("nome")));
			}
			return str;
		} else {
			return new ArrayList<String>();
		}
	}

	public String getGrupoMuscular(String exercicio) {

		String[] dadosQuery = { exercicio };
		Cursor cursor = dbQuery
				.rawQuery(
						"select grupo_muscular from autocomplete_exercicios where nome = ?",
						dadosQuery);

		if (cursor.moveToFirst()) {
			
			do {
				
				return cursor.getString(0);

			} while (cursor.moveToNext());
		}

		return "";	
	}
}
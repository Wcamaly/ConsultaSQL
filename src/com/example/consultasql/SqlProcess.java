package com.example.consultasql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.Context;
import android.widget.Toast;

public class SqlProcess {
	private Connection connectionMysql;
	private String urlConexion;
	private Context context;
	private Statement st;
	private String user;
	private String pass;
	private String ip;
	private String puerto;

	public SqlProcess(String user, String pass, String ip, String puerto,
			Context context) {
		this.context = context;
		this.pass = pass;
		this.ip = ip;
		this.puerto = puerto;
		this.user = user;

	}

	public boolean EstablishConection(String catalogo) {
		if (this.connectionMysql == null) {

			if (catalogo != "")
				this.urlConexion = "jdbc:mysql://" + this.ip + ":"
						+ this.puerto + "/" + catalogo;
			else
				this.urlConexion = "jdbc:mysql://" + this.ip + ":"
						+ this.puerto;

			try {

				Class.forName("com.mysql.jdbc.Driver");
				this.connectionMysql = DriverManager.getConnection(urlConexion,
						this.user, this.pass);

				Toast.makeText(this.context, "Conexion server Mysql",
						Toast.LENGTH_LONG).show();

				this.st = this.connectionMysql.createStatement();
				return true;
			} catch (ClassNotFoundException e) {
				Toast.makeText(this.context, "Error: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			} catch (SQLException e) {
				Toast.makeText(this.context, "Error: " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(this.context, "Error: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		
		}
		return false;
	
	}

	public String[] ListDB() {
		if (EstablishConection("")){
			try {
	
				ResultSet rs = this.st.executeQuery("show databases");
				rs.last();
	
				int numfil = 0;
				numfil = rs.getRow();
				String[] listDb = new String[numfil];
	
				for (int i = 0; i < numfil; i++) {
	
					listDb[i] = rs.getObject(1).toString();
					rs.previous();
				}
				Close();
				return listDb;
			} catch (Exception e) {
				Toast.makeText(this.context, "ERROR:" + e.getMessage(),
						Toast.LENGTH_LONG).show();
	
			}
		}
		return null;

	}

	public String QuerySelect(String catalogo, String Sqlconsul) {
		if(EstablishConection(catalogo)){
			try {
	
				ResultSet rs = this.st.executeQuery(Sqlconsul);
				String resultadoSQL = "";
				// número de columnas (campos) de la consula SQL
				int numColumnas = rs.getMetaData().getColumnCount();
	
				// obtenemos el título de las columnas
				for (int i = 1; i <= numColumnas; i++) {
					if (resultadoSQL != "")
						if (i < numColumnas)
							resultadoSQL = resultadoSQL
									+ rs.getMetaData().getColumnName(i).toString()
									+ ";";
						else
							resultadoSQL = resultadoSQL
									+ rs.getMetaData().getColumnName(i).toString();
					else if (i < numColumnas)
						resultadoSQL = rs.getMetaData().getColumnName(i).toString()
								+ ";";
					else
						resultadoSQL = rs.getMetaData().getColumnName(i).toString();
				}
	
				// mostramos el resultado de la consulta SQL
				while (rs.next()) {
					resultadoSQL = resultadoSQL + "\n";
	
					// obtenemos los datos de cada columna
					for (int i = 1; i <= numColumnas; i++) {
						if (rs.getObject(i) != null) {
							if (resultadoSQL != "")
								if (i < numColumnas)
									resultadoSQL = resultadoSQL
											+ rs.getObject(i).toString() + ";";
								else
									resultadoSQL = resultadoSQL
											+ rs.getObject(i).toString();
							else if (i < numColumnas)
								resultadoSQL = rs.getObject(i).toString() + ";";
							else
								resultadoSQL = rs.getObject(i).toString();
						} else {
							if (resultadoSQL != "")
								resultadoSQL = resultadoSQL + "null;";
							else
								resultadoSQL = "null;";
						}
					}
					resultadoSQL = resultadoSQL + "\n";
				}
	
				rs.close();
				Close();
				return resultadoSQL;
	
			} catch (Exception e) {
				Toast.makeText(this.context, "ERROR:" + e.getMessage(),
						Toast.LENGTH_LONG).show();
				
			}
		}
		return null;
	}

	public String QueryInsert(String catalogo, String Sqlconsul) {
		String resultadoSQL = "";
		if(EstablishConection(catalogo)){
			
			try {
				int numAfectados = this.st.executeUpdate(Sqlconsul);
				resultadoSQL = "Registros afectados: "
						+ String.valueOf(numAfectados);
				Close();
	
			} catch (Exception e) {
				Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG)
						.show();
	
				// TODO: handle exception
			}
			
		}
		return resultadoSQL;
	}

	public void Close() {
		try {
			this.st.close();
			this.connectionMysql.close();

		} catch (SQLException e) {
			Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}
}
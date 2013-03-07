package org.mule.munit;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;


import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;


public class DatabaseServer {

    private static Logger logger = Logger.getLogger(DatabaseServer.class);


    /**
     * <p>H2 Database name</p>
     */
    private String database;

    /**
     * <p>Name of (or path to) the SQL file whose statements will be executed when the database is started</p>
     */
    private String sqlFile;

    /**
     * <p>CSV files (separated by semicolon) that creates tables in the database using the file name (without the
     * termination, ".csv") as the table name and its columns as the table columns</p>
     */
    private String csv;

    /**
     * <p>The database connection</p>
     */
    private Connection connection;

    public DatabaseServer(String database, String sqlFile, String csv) {
        this.database = database;
        this.sqlFile = sqlFile;
        this.csv = csv;
    }


    /**
     * <p>Starts the server</p>
     * <p>Executes the correspondent queries if an SQL file has been included in the dbserver configuration</p>
     * <p>Creates the correspondent tables in the database if a CSV file has been included in the dbserver
     * configuration</p>
     */
    public void start()
    {
        try
        {
            addJdbcToClassLoader();
            connection = DriverManager.getConnection("jdbc:h2:mem:"+ database);
            executeQueriesFromSQLFile(connection);
            Statement stmt = connection.createStatement();
            createTablesFromCsv(stmt);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not start the database server", e);
        }
    }

    /**
     * <p>Executes the SQL query received as parameter</p>
     *
     *
     * @param sql query to be executed
     * @return result of the SQL query received
     */
    public Object execute(String sql)
    {
        Statement statement = null;
        try
        {
            statement = connection.createStatement();
            return statement.execute(sql);
        }
        catch (SQLException e)
        {
            logger.error("There has been a problem while executing the SQL statement", e);
            return null;
        }
    }

    /**
     * <p>Executes a SQL query</p>
     *
     * @param sql query to be executed
     * @return result of the SQL query in a JSON format.
     */
    public Object executeQuery(String sql)
    {
        Statement statement = null;
        try
        {
            return getMap(sql);
        }
        catch (SQLException e)
        {
            logger.error("There has been a problem while executing the SQL statement", e);
            return null;
        }
    }

    /**
     * <p>Executes a SQL query</p>
     *
     * @param query query to be executed
     * @param returns Expected value
     */
    public void validateThat(String query, String returns)
    {
        try
        {
            Writer writerQueryResult = getResults(query);
            assertEquals(writerQueryResult.toString().trim(), returns.replace("\\n", "\n"));
        }
        catch (ClassCastException ccException)
        {
            throw new RuntimeException("The JSON String must always be an array");
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Invalid Query");
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could no access to query results");
        }

    }

    /**
     * <p>Stops the server.</p>
     */
    public void stop()
    {
        try
        {
            if ( connection != null ) connection.close();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Could not stop the database server", e);
        }
    }

    private void addJdbcToClassLoader() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        Class.forName("org.h2.Driver").newInstance();
    }

    private void executeQueriesFromSQLFile(Connection conn) throws SQLException, FileNotFoundException
    {
        if(sqlFile != null)
        {
            InputStream streamImput = getClass().getResourceAsStream(File.separator + sqlFile);
            RunScript.execute(conn, new InputStreamReader(streamImput));
        }
    }

    private void createTablesFromCsv(Statement stmt)
    {
        if (csv != null)
        {
            String[] tables = csv.split(";");
            for ( String table : tables )
            {
                String tableName = table.replaceAll(".csv", "");
                try
                {
                    stmt.execute("CREATE TABLE "+tableName+" AS SELECT * FROM CSVREAD(\'" + getClass().
                            getResource("/"+table).toURI().toASCIIString()  + "\');");
                }
                catch (SQLException e)
                {
                    throw new RuntimeException("Invalid SQL, could not create table " + tableName + " from " + table);
                }
                catch (URISyntaxException e)
                {
                    throw new RuntimeException("Could not read file " + table);
                }
            }
        }
    }

    private List<Map<String, String>> getMap(String sql) throws SQLException
    {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<Map<String, String>> jsonArray = new ArrayList<Map<String,String>>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next())
        {
            HashMap<String, String> jsonObject = new HashMap<String,String>();
            for (int i = 1; i <= metaData.getColumnCount(); i++)
            {
                String columnName = metaData.getColumnName(i);
                jsonObject.put(columnName, String.valueOf(resultSet.getObject(columnName)));
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private Writer getResults(String sql) throws SQLException, IOException {
        Statement statement;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        Writer writer = new StringWriter();
        CSVWriter csvwriter = new CSVWriter(writer);
        csvwriter.writeAll(resultSet,true);

        return writer;
    }


}

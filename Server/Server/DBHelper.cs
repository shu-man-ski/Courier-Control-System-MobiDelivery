using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;

namespace Server
{
    class DBHelper
    {
        static private SqlCommand command;       /* Инструкция T-SQL (или хранимая процедура) */
        static private SqlConnection connection; /* Подключение к базе данных SQL Server */
        static private SqlDataAdapter adapter;   /* Набор команд данных и подключение к БД */


        static DBHelper()
        {
            connection = new SqlConnection(ConfigurationManager.ConnectionStrings["LocalSqlServer"].ConnectionString);
            adapter = null;
            command = null;
        }

        static public List<string> Query(string query)
        {
            List<string> data = new List<string>();
            try
            {
                command = new SqlCommand(query, connection);
                adapter = new SqlDataAdapter(command);
                connection.Open();

                DataSet dataSet = new DataSet();
                adapter.Fill(dataSet);

                foreach (DataTable dataTable in dataSet.Tables)
                {
                    foreach (DataRow row in dataTable.Rows)
                    {
                        foreach (object cell in row.ItemArray)
                        {
                            data.Add(cell.ToString());
                        }
                    }
                }
            }
            catch (SqlException ex)
            {
                data.Clear();
                data.Add(ex.Number.ToString());
                return data;
            }
            finally
            {
                if (connection.State == ConnectionState.Open)
                    connection.Close();
            }
            return data;
        }
    }
}
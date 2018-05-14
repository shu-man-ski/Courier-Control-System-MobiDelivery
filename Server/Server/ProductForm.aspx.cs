using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class ProductForm : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            int countOfProducts = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM [Product]")[0]);
            string[] products = DBHelper.Query("SELECT * FROM [Product]").ToArray();

            TableRow tr;
            TableCell tc;

            for (int i = 0; i < countOfProducts; i++)
            {
                tr = new TableRow();
                for (int j = 0; j < 5; j++)
                {
                    tc = new TableCell();
                    tc.Text = products[(i * 5) + j];
                    tr.Cells.Add(tc);
                }
                Table1.Rows.Add(tr);
            }
        }
    }
}
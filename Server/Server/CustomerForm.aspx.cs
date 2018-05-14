using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class CustomerForm : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            int countOfCustomers = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM [Customer]")[0]);
            string[] customers = DBHelper.Query("SELECT * FROM [Customer]").ToArray();

            TableRow tr;
            TableCell tc;

            for (int i = 0; i < countOfCustomers; i++)
            {
                tr = new TableRow();
                for (int j = 0; j < 7; j++)
                {
                    tc = new TableCell();
                    tc.Text = customers[(i * 7) + j];
                    tr.Cells.Add(tc);
                }
                Table1.Rows.Add(tr);
            }
        }
    }
}
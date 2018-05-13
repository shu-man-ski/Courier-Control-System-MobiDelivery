using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class AddOrderForm : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            int countOfOrders = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM [Order]")[0]);
            string[] orders = DBHelper.Query("SELECT * FROM [Order]").ToArray();

            TableRow tr;
            TableCell tc;

            for (int i = 0; i < countOfOrders; i++)
            {
                tr = new TableRow();
                for (int j = 0; j < 7; j++)
                {
                    tc = new TableCell();
                    tc.Text = orders[(i * 7) + j];
                    tr.Cells.Add(tc);
                }
                Table1.Rows.Add(tr);
            }

            tr = new TableRow();
            for (int i = 0; i < 7; i++)
            {
                tc = new TableCell();
                Button btn = new Button();
                btn.Text = "Some Button";
                //btn.Click += new EventHandler(btn_Click);
                tc.Controls.Add(btn);
                tr.Cells.Add(tc);
            }
            Table1.Rows.Add(tr);
        }
    }
}
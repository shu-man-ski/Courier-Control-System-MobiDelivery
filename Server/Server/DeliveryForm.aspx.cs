using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class DeliveryForm : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            int countOfDeliverys = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM [Delivery]")[0]);
            string[] deliverys = DBHelper.Query("SELECT * FROM [Delivery]").ToArray();

            TableRow tr;
            TableCell tc;

            for (int i = 0; i < countOfDeliverys; i++)
            {
                tr = new TableRow();
                for (int j = 0; j < 4; j++)
                {
                    tc = new TableCell();
                    tc.Text = deliverys[(i * 4) + j];
                    tr.Cells.Add(tc);
                }
                Table1.Rows.Add(tr);
            }
        }
    }
}
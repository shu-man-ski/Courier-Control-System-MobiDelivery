using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class CourierForm : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            int countOfCouriers = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM [Courier]")[0]);
            string[] couriers = DBHelper.Query("SELECT * FROM [Courier]").ToArray();

            TableRow tr;
            TableCell tc;

            for (int i = 0; i < countOfCouriers; i++)
            {
                tr = new TableRow();
                for (int j = 0; j < 8; j++)
                {
                    tc = new TableCell();
                    tc.Text = couriers[(i * 8) + j];
                    tr.Cells.Add(tc);
                }
                Table1.Rows.Add(tr);
            }
        }
    }
}
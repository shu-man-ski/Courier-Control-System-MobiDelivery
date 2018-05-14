using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class AddOrderForm : System.Web.UI.Page
    {
        private TextBox[] textBox;

        public TextBox[] TextBox { get => textBox; set => textBox = value; }

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
            textBox = new TextBox[7];
            for (int i = 0; i < 7; i++)
            {
                tc = new TableCell();
                textBox[i] = new TextBox();
                textBox[i].Width = 130;
                tc.Controls.Add(textBox[i]);
                tr.Cells.Add(tc);
            }
            textBox[0].Enabled = false;
            Table1.Rows.Add(tr);
        }

        protected void AddOrder_Click(object sender, EventArgs e)
        {
            string orderCode, productCode, customerCode, deliveryCode, courierCode, quantity, status;

            orderCode = textBox[0].Text;
            productCode = textBox[1].Text;
            customerCode = textBox[2].Text;
            deliveryCode = textBox[3].Text;
            courierCode = textBox[4].Text;
            quantity = textBox[5].Text;
            status = textBox[6].Text;

            string query = "INSERT INTO [Order]([Product Code], [Customer Code], [Delivery Code], [Courier Code], [Quantity], [Status]) " +
                        "VALUES ('" + productCode + "', '" + customerCode + "', '" + deliveryCode + "', '" + courierCode + "', '" + quantity + "', '" + status + "')";
            DBHelper.Query(query);
        }
    }
}
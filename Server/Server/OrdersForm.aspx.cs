using System;
using System.Web.UI.WebControls;

namespace Server
{
    public partial class AddOrderForm : System.Web.UI.Page
    {
        private TableRow tr;
        private TableCell tc;
        private DropDownList[] dropDownList;
        private TextBox textBox;

        protected void Page_Load(object sender, EventArgs e)
        {
            int countOfOrders = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM [Order]")[0]);
            string[] orders = DBHelper.Query("SELECT * FROM [Order]").ToArray();

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

            InitDropDownLists();
        }

        protected void InitDropDownLists()
        {
            tr = new TableRow();
            dropDownList = new DropDownList[5];

            // [Order Code]
            tc = new TableCell();
            TextBox text = new TextBox();
            text.Width = 130;
            text.Enabled = false;
            tc.Controls.Add(text);
            tr.Cells.Add(tc);

            // [Product Code]
            tc = new TableCell();
            dropDownList[0] = new DropDownList();
            string[] productCodes = DBHelper.Query("SELECT [Product Code] FROM [Product]").ToArray();
            for (int j = 0; j < productCodes.Length; j++)
            {
                dropDownList[0].Items.Add(productCodes[j]);
            }
            dropDownList[0].Width = 130;
            tc.Controls.Add(dropDownList[0]);
            tr.Cells.Add(tc);

            // [Customer Code]
            tc = new TableCell();
            dropDownList[1] = new DropDownList();
            string[] customerCodes = DBHelper.Query("SELECT [Customer Code] FROM [Customer]").ToArray();
            for (int j = 0; j < customerCodes.Length; j++)
            {
                dropDownList[1].Items.Add(customerCodes[j]);
            }
            dropDownList[1].Width = 130;
            tc.Controls.Add(dropDownList[1]);
            tr.Cells.Add(tc);

            // [Delivery Code]
            tc = new TableCell();
            dropDownList[2] = new DropDownList();
            string[] deliveryCodes = DBHelper.Query("SELECT [Delivery Code] FROM [Delivery]").ToArray();
            for (int j = 0; j < deliveryCodes.Length; j++)
            {
                dropDownList[2].Items.Add(deliveryCodes[j]);
            }
            dropDownList[2].Width = 130;
            tc.Controls.Add(dropDownList[2]);
            tr.Cells.Add(tc);

            // [Courier Code]
            tc = new TableCell();
            dropDownList[3] = new DropDownList();
            string[] courierCodes = DBHelper.Query("SELECT [Courier Code] FROM [Courier]").ToArray();
            for (int j = 0; j < deliveryCodes.Length; j++)
            {
                dropDownList[3].Items.Add(courierCodes[j]);
            }
            dropDownList[3].Width = 130;
            tc.Controls.Add(dropDownList[3]);
            tr.Cells.Add(tc);

            // [Courier Code]
            tc = new TableCell();
            textBox = new TextBox();
            textBox.Width = 130;
            tc.Controls.Add(textBox);
            tr.Cells.Add(tc);

            // [Status]
            tc = new TableCell();
            dropDownList[4] = new DropDownList();
            string[] status = { "Новый", "Комплектуется", "В доставке", "Получен клиентом", "Оплачен", "Отменен" };
            for (int j = 0; j < status.Length; j++)
            {
                dropDownList[4].Items.Add(status[j]);
            }
            dropDownList[4].Width = 130;
            tc.Controls.Add(dropDownList[4]);
            tr.Cells.Add(tc);


            Table1.Rows.Add(tr);
        }

        protected void AddOrder_Click(object sender, EventArgs e)
        {
            string productCode, customerCode, deliveryCode, courierCode, quantity, status;

            productCode = dropDownList[0].SelectedValue;
            customerCode = dropDownList[1].SelectedValue;
            deliveryCode = dropDownList[2].SelectedValue;
            courierCode = dropDownList[3].SelectedValue;
            quantity = textBox.Text;
            status = dropDownList[4].SelectedValue;

            string query = "INSERT INTO [Order]([Product Code], [Customer Code], [Delivery Code], [Courier Code], [Quantity], [Status]) " +
                        "VALUES ('" + productCode + "', '" + customerCode + "', '" + deliveryCode + "', '" + courierCode + "', '" + quantity + "', '" + status + "')";
            DBHelper.Query(query);

            Response.Redirect(Request.RawUrl);
        }
    }
}
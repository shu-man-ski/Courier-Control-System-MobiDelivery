using Server.Models;
using System.Collections.Generic;
using System.Web.Http;

namespace Server
{
    public class OrdersController : ApiController
    {
        // GET api/<controller>?deviceId=abc
        public IEnumerable<Order> Get(string deviceId)
        {
            string[] values = DBHelper.Query("SELECT DISTINCT [Order].[Order Code], [Product].Name AS ProductName, [Order].Quantity, " +
                "([Product].[Price] * [Order].Quantity) AS [Cost], " +
                "[Customer].Name, [Delivery].Address, [Delivery].Date, [Delivery].Time, [Order].[Status] " +
                "FROM [Product], [Customer], [Delivery], [Order], [Courier] " +
                    "WHERE [Order].[Product Code] = [Product].[Product Code] AND " +
                    "[Customer].[Customer Code] = [Order].[Customer Code] AND " +
                    "[Delivery].[Delivery Code] = [Order].[Delivery Code] AND " +
                    "[Order].[Courier Code] = (SELECT [Courier Code] FROM [Courier] WHERE [Device ID] = '" + deviceId + "')").ToArray();

            Order[] orders = new Order[values.Length / 9];

            for (int i = 0; i < orders.Length; i++)
            {
                orders[i] = new Order
                {
                    OrderCode = values[(i * 9) + 0],
                    ProductName = values[(i * 9) + 1],
                    Quantity = values[(i * 9) + 2],
                    Cost = values[(i * 9) + 3],
                    Name = values[(i * 9) + 4],
                    Address = values[(i * 9) + 5],
                    Date = values[(i * 9) + 6],
                    Time = values[(i * 9) + 7],
                    Status = values[(i * 9) + 8]
                };
            };

            return orders;
        }

        // POST api/<controller>
        public void Post([FromBody]string value)
        {

        }

        // PUT api/<controller>?orderCode=5
        // body: =abc
        public void Put(int orderCode, [FromBody]string status)
        {
            string query = "UPDATE [Order] " +
                "SET[Status] = N'" + status + "' " +
                "WHERE[Order Code] = '"+ orderCode + "'";
            DBHelper.Query(query);
        }

        // DELETE api/<controller>/5
        public void Delete(int id)
        {

        }
    }
}

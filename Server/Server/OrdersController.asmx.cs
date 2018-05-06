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
            string[] values = DBHelper.Query("SELECT DISTINCT [Product].Name AS ProductName, [Order].Quantity, " +
                "([Product].[Price] * [Order].Quantity) AS [Cost], " +
                "[Customer].Name, [Delivery].Address, [Delivery].Date, [Delivery].Time " +
                "FROM [Product], [Customer], [Delivery], [Order], [Courier] " +
                    "WHERE [Order].[Product Code] = [Product].[Product Code] AND " +
                    "[Customer].[Customer Code] = [Order].[Customer Code] AND " +
                    "[Delivery].[Delivery Code] = [Order].[Delivery Code] AND " +
                    "[Order].[Courier Code] = (SELECT [Courier Code] FROM [Courier] WHERE [Device ID] = '" + deviceId + "')").ToArray();

            Order[] orders = new Order[values.Length / 7];

            for (int i = 0; i < orders.Length; i++)
            {
                orders[i] = new Order
                {
                    ProductName = values[i + 0],
                    Quantity = values[i + 1],
                    Cost = values[i + 2],
                    Name = values[i + 3],
                    Address = values[i + 4],
                    Date = values[i + 5],
                    Time = values[i + 6]
                };
            };

            return orders;
        }

        // POST api/<controller>
        public void Post([FromBody]string value)
        {

        }

        // PUT api/<controller>/5
        public void Put(int id, [FromBody]string value)
        {

        }

        // DELETE api/<controller>/5
        public void Delete(int id)
        {

        }
    }
}

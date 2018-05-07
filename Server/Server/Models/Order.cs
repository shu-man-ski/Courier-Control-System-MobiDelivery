namespace Server.Models
{
    public class Order
    {
        public string OrderCode { get; set; }
        public string ProductName { get; set; }
        public string Quantity { get; set; }
        public string Cost { get; set; }
        public string Name { get; set; }
        public string Address { get; set; }
        public string Date { get; set; }
        public string Time { get; set; }
        public string Status { get; set; }
    }
}
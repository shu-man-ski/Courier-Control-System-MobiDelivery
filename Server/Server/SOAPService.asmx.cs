using System.Web.Services;

namespace Server
{
    [WebService(Namespace = "http://192.168.43.234/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class SOAPService : WebService
    {
        [WebMethod]
        public string RegisterCourier(string deviceID, string surname, string name,
            string patronymic, string birthdate, string phoneNumber, string address)
        {
            string query = "SELECT count(*) FROM Courier " +
                                "WHERE [Device ID] = '" + deviceID + "'";

            // Если в таблице нет записи для данного deviceID, то INSERT,
            // если такой deviceID существует — UPDATE
            if (DBHelper.Query(query)[0] == "0")
            {
                query = "INSERT INTO Courier([Device ID], [Surname], [Name], [Patronymic], [Birthdate], [Phone Number], [Address])" +
                "VALUES ('" + deviceID + "', '" + surname + "', '" + name + "', '" + patronymic + "', '" + birthdate + "', '" + phoneNumber + "', '" + address + "')";
                DBHelper.Query(query);

                return "INSERT success";
            }
            else
            {
                query = "UPDATE Courier " +
                   "SET [Surname] = '" + surname + "', [Name] = '" + name + "', [Patronymic] = '" + patronymic + "', [Birthdate] = '" + birthdate + "', [Phone Number] = '" + phoneNumber + "', [Address] = '" + address + "'" +
                   "WHERE [Device ID] = '" + deviceID + "'";
                DBHelper.Query(query);

                return "UPDATE success";
            }          
        }
        
        [WebMethod]
        public string Coordinates(string deviceID, string latitude, string longitude, string speed)
        {
            string query = "SELECT count(*) FROM CurrentCoordinatesOfCourier " +
                                "WHERE [Courier Code] = (SELECT[Courier Code] FROM Courier WHERE[Device ID] = '" + deviceID + "')";

            // Если в таблице нет записи координат курьера, с таким deviceID, то INSERT,
            // если курьер, с таким deviceID, существует — UPDATE
            if (DBHelper.Query(query)[0] == "0")
            {
                // Если в БД имеется курьер с таким deviceID
                if (DBHelper.Query("(SELECT count(*) FROM Courier WHERE[Device ID] = '" + deviceID + "')")[0] == "1")
                {
                    query = "INSERT INTO CurrentCoordinatesOfCourier([Courier Code], [Latitude], [Longitude], [Speed]) " +
                        "VALUES ((SELECT[Courier Code] FROM Courier WHERE[Device ID] = '" + deviceID + "'), '" + latitude + "', '" + longitude + "', '" + speed + "')";
                    DBHelper.Query(query);

                    return "INSERT success";
                }
                else
                {
                    return "INSERT error";
                }
            }
            else
            {
                query = "UPDATE CurrentCoordinatesOfCourier " +
                    "SET [Latitude] = '" + latitude + "', [Longitude] = '" + longitude + "', [Speed] = '" + speed + "' " +
                    "WHERE [Courier Code] = (SELECT[Courier Code] FROM Courier WHERE[Device ID] = '" + deviceID + "')";
                DBHelper.Query(query);

                return "UPDATE success";
            }
        }
    }
}

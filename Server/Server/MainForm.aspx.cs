using Subgurim.Controles;
using System;
using System.Web.UI;

namespace Server
{
    public partial class MainForm : Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            initMap();

            if (!IsPostBack)
            {
                updateMarkerOnMap();
            }
        }


        protected void initMap()
        {
            /* Настройка карты */
            MainGMap.Add(new GControl(GControl.preBuilt.LargeMapControl));
            MainGMap.setCenter(new GLatLng(53.901547, 27.553763), 12, GMapType.GTypes.Normal);
        }

        protected void updateMarkerOnMap()
        {
            /* Установка меток на карте */
            int countOfCouriers = Convert.ToInt32(DBHelper.Query("SELECT count(*) FROM CurrentCoordinatesOfCourier")[0]);

            // Если в таблице имеются записи с координатами курьеров
            if (countOfCouriers != 0)
            {
                double[] convertedLatitude = new double[countOfCouriers];
                double[] convertedLongitude = new double[countOfCouriers];
                double[] convertedSpeed = new double[countOfCouriers];
                GMarker[] markers = new GMarker[countOfCouriers];
                GInfoWindow[] windows = new GInfoWindow[countOfCouriers];

                string[] courierCode = DBHelper.Query("SELECT [Courier Code] FROM CurrentCoordinatesOfCourier").ToArray();
                string[] latitude = DBHelper.Query("SELECT [Latitude] FROM CurrentCoordinatesOfCourier").ToArray();
                string[] longitude = DBHelper.Query("SELECT [Longitude] FROM CurrentCoordinatesOfCourier").ToArray();
                string[] speed = DBHelper.Query("SELECT [Speed] FROM CurrentCoordinatesOfCourier").ToArray();

                for (int i = 0; i < countOfCouriers; i++)
                {
                    // Преобразование в double с заменой символа запятой на точку
                    convertedLatitude[i] = Convert.ToDouble(latitude[i]);
                    convertedLongitude[i] = Convert.ToDouble(longitude[i]);
                    convertedSpeed[i] = Convert.ToDouble(speed[i]);

                    // Заказы курьера
                    string[] courierOrders = DBHelper.Query("SELECT [Order Code], [Status] FROM [Order]"+
                        "WHERE[Courier Code] = '" + courierCode[i] + "'").ToArray();

                    markers[i] = new GMarker(new GLatLng(convertedLatitude[i], convertedLongitude[i]));
                    windows[i] = new GInfoWindow(markers[i], GetGInfoWindowString(courierCode[i], courierOrders, speed[i]), true);
                    MainGMap.Add(windows[i]);
                }
            }
        }

        protected string GetGInfoWindowString(string courierCode, string[] courierOrders, string speed)
        {
            int counOfOrders = courierOrders.Length / 2;
            string orders = "<tr>" +
                                "<th><b>Код заказа</b></th>" +
                                "<th><b>Cтатус</b></th>" +
                            "</<tr>"; ;
            for(int i = 0; i < counOfOrders; i++)
            {
                orders += "<tr>" +
                            "<td align=\"center\"><i>" + courierOrders[(i * 2) + 0] + "</i></td>" +
                            "<td align=\"center\"><i>" + courierOrders[(i * 2) + 1] + "</i></td>" +
                          "</<tr>";
            }

            string infoWindow =
                "<center>" +
                    "<table>" +
                        "<tr>" +
                            "<td><b>Код курьера: </b></td>" +
                            "<td><i>" + courierCode + "</i></td>" +
                        "</<tr>" +
                        "<tr>" +
                            "<td><b>Скорость движения: </b></td>" +
                            "<td><i>" + speed + "</i></td>" +
                        "</<tr>" +
                        orders +
                    "</table>" +
                "</center>";

            return infoWindow;
        }


        protected void Timer_Tick(object sender, EventArgs e)
        {
            MainGMap.reset();
            initMap();
            updateMarkerOnMap();
        }
    }
}
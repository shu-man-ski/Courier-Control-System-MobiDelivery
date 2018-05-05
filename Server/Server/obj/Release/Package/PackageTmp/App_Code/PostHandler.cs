using System.IO;
using System.Web;

public class PostHandler : IHttpHandler
{
    public void ProcessRequest(HttpContext context)
    {
        HttpRequest request = context.Request;
        HttpResponse response = context.Response;
 
        //response.Write("Param = " + request["Param"] + "\n");
        //response.Write("InputStream: " + request.InputStream);

        if (request["Param"] == "Write")
        {
            // запись в файл
            using (FileStream fstream = new FileStream(@"C:\Sites\Course\Course.txt", FileMode.OpenOrCreate))
            {
                byte[] array;
                using (MemoryStream ms = new MemoryStream())
                {
                    request.InputStream.CopyTo(ms);
                    array = ms.ToArray();
                }
                fstream.Write(array, 0, array.Length);
            }
        }


        response.StatusCode = 200;
    }

    public bool IsReusable
    {
        get { return false; }
    }
}
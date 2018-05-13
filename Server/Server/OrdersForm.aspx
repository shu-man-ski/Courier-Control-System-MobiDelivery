<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="OrdersForm.aspx.cs" Inherits="Server.AddOrderForm" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
        <div>
            <asp:Table ID="Table1" runat="server" style="width: 100%; padding:15px" BorderWidth="1">
                <asp:tablerow runat="server">
                    <asp:tablecell runat="server">
                       <b>Код заказа</b>
                    </asp:tablecell>
                    <asp:tablecell runat="server">
                       <b>Код продукта</b>
                    </asp:tablecell>
                    <asp:tablecell runat="server">
                       <b>Код заказчика</b>
                    </asp:tablecell>
                    <asp:tablecell runat="server">
                       <b>Код доставки</b>
                    </asp:tablecell>
                    <asp:tablecell runat="server">
                       <b>Код курьера</b>
                    </asp:tablecell>
                    <asp:tablecell runat="server">
                      <b>Количество товара</b>
                    </asp:tablecell>
                    <asp:tablecell runat="server">
                       <b>Статус заказа</b>
                    </asp:tablecell>
                 </asp:tablerow>
            </asp:Table>
        </div>
    </form>
</body>
</html>

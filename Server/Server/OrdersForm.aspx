<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="OrdersForm.aspx.cs" Inherits="Server.AddOrderForm"
    MasterPageFile="~/Site.Master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Заказы</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:Table ID="Table1" runat="server" Style="width: 100%; padding: 15px" BorderWidth="1">
        <asp:TableRow runat="server">
            <asp:TableCell runat="server">
                       <b>Код заказа</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Код продукта</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Код заказчика</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Код доставки</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Код курьера</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                      <b>Количество товара</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Статус заказа</b>
            </asp:TableCell>
        </asp:TableRow>
    </asp:Table>
</asp:Content>
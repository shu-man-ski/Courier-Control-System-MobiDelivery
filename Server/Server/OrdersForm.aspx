<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="OrdersForm.aspx.cs" Inherits="Server.AddOrderForm"
    MasterPageFile="~/Site.Master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Заказы</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:Table ID="Table1" runat="server" Width="100%" Style="padding: 15px; border-spacing: 0; border: 1px solid; border-radius: 2px;">
        <asp:TableHeaderRow runat="server">
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
        </asp:TableHeaderRow>
    </asp:Table>
    <br />
    <asp:Button ID="Button1" runat="server" Text="Добавить" style="float: right; background-color:#E0E0E0;" OnClick="AddOrder_Click" />
</asp:Content>
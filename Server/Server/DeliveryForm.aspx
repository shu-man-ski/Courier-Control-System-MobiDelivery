<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="DeliveryForm.aspx.cs" Inherits="Server.DeliveryForm" 
    MasterPageFile="~/Site.Master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Доставка</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:Table ID="Table1" runat="server" Width="100%" Style="padding: 15px; border-spacing: 0; border: 1px solid; border-radius: 2px;">
        <asp:TableRow runat="server">
            <asp:TableCell runat="server">
                       <b>Код доставки</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Адрес</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Дата</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Время</b>
            </asp:TableCell>
        </asp:TableRow>
    </asp:Table>
</asp:Content>
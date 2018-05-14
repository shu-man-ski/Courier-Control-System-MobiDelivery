<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="CourierForm.aspx.cs" Inherits="Server.CourierForm" 
    MasterPageFile="~/Site.Master"%>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Курьеры</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:Table ID="Table1" runat="server" Width="100%" Style="padding: 15px" BorderWidth="1">
        <asp:TableRow runat="server">
            <asp:TableCell runat="server">
                       <b>Код курьера</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>ID устройства</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Фамилия</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Имя</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Отчество</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                      <b>Дата рождения</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Номер телефона</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Адрес</b>
            </asp:TableCell>
        </asp:TableRow>
    </asp:Table>
</asp:Content>
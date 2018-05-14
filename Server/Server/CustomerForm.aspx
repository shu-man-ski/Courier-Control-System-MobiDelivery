<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="CustomerForm.aspx.cs" Inherits="Server.CustomerForm"
    MasterPageFile="~/Site.Master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Заказчики</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:Table ID="Table1" runat="server" Width="100%" Style="padding: 15px; border-spacing: 0; border: 1px solid; border-radius: 2px;">
        <asp:TableRow runat="server">
            <asp:TableCell runat="server">
                       <b>Код заказчика</b>
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
                       <b>Номер телефона</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Адрес</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                      <b>E-mail</b>
            </asp:TableCell>
        </asp:TableRow>
    </asp:Table>
</asp:Content>
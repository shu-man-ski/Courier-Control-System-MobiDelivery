<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="ProductForm.aspx.cs" Inherits="Server.ProductForm"
    MasterPageFile="~/Site.Master" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Товары</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:Table ID="Table1" runat="server" Width="100%" Style="padding: 15px; border-spacing: 0; border: 1px solid; border-radius: 2px;">
        <asp:TableRow runat="server">
            <asp:TableCell runat="server">
                       <b>Код продукта</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Название</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Описание</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Цена</b>
            </asp:TableCell>
            <asp:TableCell runat="server">
                       <b>Количество</b>
            </asp:TableCell>
        </asp:TableRow>
    </asp:Table>
</asp:Content>
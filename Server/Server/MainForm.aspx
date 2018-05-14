<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="MainForm.aspx.cs" Inherits="Server.MainForm"
    MasterPageFile="~/Site.Master" %>

<%@ OutputCache Duration="30" VaryByParam="None" %>

<%@ Register Assembly="GMaps" Namespace="Subgurim.Controles" TagPrefix="cc1" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>MobiDelivery | Главная</title>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="body" runat="server">
    <asp:ScriptManager ID="ScriptManager" runat="server" />
    <asp:Timer ID="Timer" runat="server" Interval="60000" OnTick="Timer_Tick" />
    <asp:UpdatePanel ID="UpdatePanel" runat="server" UpdateMode="Conditional" style="height:100%">
        <ContentTemplate >
            <cc1:GMap ID="MainGMap" runat="server" Key="AIzaSyAcPwZsxpGSBYe5hl9Y0chdYtMjls1uww0"
                 Width="100%" Height="100%"/>
        </ContentTemplate>
        <Triggers>
            <asp:AsyncPostBackTrigger ControlID="Timer" EventName="Tick" />
        </Triggers>
    </asp:UpdatePanel>
</asp:Content>

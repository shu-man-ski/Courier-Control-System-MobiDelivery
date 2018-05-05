<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="MainForm.aspx.cs" Inherits="Server.MainForm" %>

<%@ Register Assembly="GMaps" Namespace="Subgurim.Controles" TagPrefix="cc1" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <meta charset="utf-8"/>
    <title></title>
    <style>
      #form1 {
        position: absolute;
        top: 0;
        right: 0;
      }
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
</head>
<body>
    <cc1:GMap ID="GMap1" runat="server" Key="AIzaSyAcPwZsxpGSBYe5hl9Y0chdYtMjls1uww0" 
        Height="100%" Width="85%"/>
    <form id="form1" runat="server">
        <asp:Label ID="Label1" runat="server" Text="Label"></asp:Label>
        <br />
        <asp:Button ID="Button1" runat="server" Text="Add Marker" OnClick="Button1_Click" />
    </form>
</body>
</html>
<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="MainForm.aspx.cs" Inherits="Server.MainForm" %>

<%@ Register Assembly="GMaps" Namespace="Subgurim.Controles" TagPrefix="cc1" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta charset="utf-8" />
    <title></title>
    <link href="StyleSheet.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <form id="form1" runat="server">
        <div class="first-column">
            <asp:ScriptManager ID="ScriptManager" runat="server" />
            <asp:Timer ID="Timer" runat="server" Interval="60000" OnTick="Timer_Tick" />
            <asp:UpdatePanel ID="UpdatePanel" runat="server" UpdateMode="Conditional">
                <ContentTemplate>
                    <cc1:GMap ID="MainGMap" runat="server" Key="AIzaSyAcPwZsxpGSBYe5hl9Y0chdYtMjls1uww0"
                        Height="100%" Width="100%" />
                </ContentTemplate>
                <Triggers>
                    <asp:AsyncPostBackTrigger ControlID="Timer" EventName="Tick" />
                </Triggers>
            </asp:UpdatePanel>
        </div>
        <div class="second-column">
            <asp:Label ID="Label1" runat="server" Text="Label"></asp:Label>
            <br />
            <asp:Button ID="Button1" runat="server" Text="Add Marker" OnClick="Button1_Click" />
        </div>
    </form>
</body>
</html>

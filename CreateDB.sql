CREATE DATABASE [Shop]
USE [Shop]

----------------------------------------- CREATE TABLE [Courier] ------------------------------------------
CREATE TABLE [dbo].[Courier](
	[Courier Code]   [int] IDENTITY(1,1) NOT NULL,
	[Device ID]      [nvarchar](50) NOT NULL,
	[Surname]        [nvarchar](50) NOT NULL,
	[Name]           [nvarchar](50) NOT NULL,
	[Patronymic]     [nvarchar](50) NOT NULL,
	[Birthdate]      [nvarchar](50) NOT NULL,
	[Phone Number]   [nvarchar](50) NOT NULL,
	[Address]        [nvarchar](50) NOT NULL,
	CONSTRAINT AK_DeviceID UNIQUE([Device ID]),
	CONSTRAINT [PK_Courier] PRIMARY KEY ([Courier Code] ASC)
)ON [PRIMARY]

----------------------------------------- CREATE TABLE [Customer] -----------------------------------------
CREATE TABLE [dbo].[Customer](
	[Customer Code]   [int] IDENTITY(1,1) NOT NULL,
	[Surname]         [nvarchar](50) NOT NULL,
	[Name]            [nvarchar](50) NOT NULL,
	[Patronymic]      [nvarchar](50) NOT NULL,
	[Phone Number]    [nvarchar](50) NOT NULL,
	[Address]         [nvarchar](50) NOT NULL,
	[Email]           [nvarchar](50) NULL,
	CONSTRAINT [PK_Customer] PRIMARY KEY([Customer Code] ASC)
)ON [PRIMARY]

----------------------------------------- CREATE TABLE [Product] ------------------------------------------
CREATE TABLE [dbo].[Product](
	[Product Code]   [int] IDENTITY(1,1) NOT NULL,
	[Name]           [nvarchar](50) NOT NULL,
	[Description]    [nvarchar](50) NULL,
	[Price]          [int] NOT NULL,
	[Quantity]       [int] NOT NULL,
	CONSTRAINT [PK_Product] PRIMARY KEY([Product Code] ASC)
)ON [PRIMARY]

----------------------------------------- CREATE TABLE [Delivery] -----------------------------------------
CREATE TABLE [dbo].[Delivery](
	[Delivery Code]   [int] IDENTITY(1,1) NOT NULL,
	[Address]         [nvarchar](50) NOT NULL,
	[Date]            [nvarchar](50) NOT NULL,
	[Time]            [nvarchar](50) NULL,
	CONSTRAINT [PK_Delivery] PRIMARY KEY([Delivery Code] ASC)
)ON [PRIMARY]

----------------------------------------- CREATE TABLE [Order] -----------------------------------------
CREATE TABLE [dbo].[Order](
	[Order Code]      [int] IDENTITY(1,1) NOT NULL,
	[Product Code]    [int] NOT NULL,
	[Customer Code]   [int] NOT NULL,
	[Delivery Code]   [int] NOT NULL,
	[Courier Code]    [int] NOT NULL,
	[Quantity]        [int] NOT NULL,
	[Status]		  [nvarchar](50) CHECK([Status] = 'Новый' OR 
										   [Status] = 'Комплектуется' OR
										   [Status] = 'В доставке' OR
										   [Status] = 'Получен клиентом' OR
										   [Status] = 'Оплачен' OR
										   [Status] = 'Отменен') DEFAULT 'Новый' NOT NULL,
	CONSTRAINT [PK_Order] PRIMARY KEY([Order Code] ASC),
	CONSTRAINT [FK_Product] FOREIGN KEY([Product Code]) REFERENCES [dbo].[Product]([Product Code]),
	CONSTRAINT [FK_Customer] FOREIGN KEY([Customer Code]) REFERENCES [dbo].[Customer]([Customer Code]),
	CONSTRAINT [FK_Delivery] FOREIGN KEY([Delivery Code]) REFERENCES [dbo].[Delivery]([Delivery Code]),
	CONSTRAINT [FK_Courier] FOREIGN KEY([Courier Code]) REFERENCES [dbo].[Courier]([Courier Code])
)ON [PRIMARY]

----------------------------------------- CREATE TABLE [CurrentCoordinatesOfCourier] -------------------
CREATE TABLE [dbo].[CurrentCoordinatesOfCourier](
	[Courier Code]   [int] NOT NULL,
	[Latitude]       [nvarchar](50) NOT NULL,
	[Longitude]      [nvarchar](50) NOT NULL,
	[Speed]          [nvarchar](50) NULL,
	CONSTRAINT [PK_CourierCode] PRIMARY KEY([Courier Code] ASC),
	CONSTRAINT [FK_CourierCode] FOREIGN KEY([Courier Code]) REFERENCES [dbo].[Courier]([Courier Code])
)ON [PRIMARY]

----------------------------------------- INSERT INTO [Product] ----------------------------------------
INSERT INTO [dbo].[Product]([Name], [Description], [Price], [Quantity])
				 VALUES ('LG P880 4X HD', 'My first awesome phone!', 233, 13),
				 ('Google Nexus 4', 'The most awesome phone of 2013!', 299, 12),
				 ('Samsung Galaxy S4', 'How about no?', 600, 3),
				 ('Bench Shirt', 'The best shirt!', 29, 1),
				 ('Lenovo Laptop', 'My business partner.', 399, 2),
				 ('Samsung Galaxy Tab 10.1', 'Good tablet.', 259, 2),
				 ('Spalding Watch', 'My sports watch.', 199, 1),
				 ('Sony Smart Watch', 'The coolest smart watch!', 300, 2),
				 ('Huawei Y300', 'For testing purposes.', 100, 2),
				 ('Abercrombie Lake Arnold Shirt', 'Perfect as gift!', 60, 1),
				 ('Abercrombie Allen Brook Shirt', 'Cool red shirt!', 70, 1),
				 ('Another product', 'Awesome product!', 555, 2),
				 ('Wallet', 'You can absolutely use this one!', 799, 6),
				 ('Amanda Waller Shirt', 'New awesome shirt!', 333, 1),
				 ('Nike Shoes for Men', 'Nike Shoes', 12999, 3),
				 ('Bristol Shoes', 'Awesome shoes.', 999, 5),
				 ('Rolex Watch', 'Luxury watch.', 25000, 1);
CREATE DATABASE [mobideliveryshopdb]
USE [mobideliveryshopdb]

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
	[Quantity]        [int] DEFAULT 1 NOT NULL,
	[Status]		  [nvarchar](50) CHECK([Status] = N'Новый' OR 
										   [Status] = N'Комплектуется' OR
										   [Status] = N'В доставке' OR
										   [Status] = N'Получен клиентом' OR
										   [Status] = N'Оплачен' OR
										   [Status] = N'Отменен') DEFAULT N'Новый' NOT NULL,
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

SET IDENTITY_INSERT [dbo].[Courier] ON 
INSERT [dbo].[Courier] ([Courier Code], [Device ID], [Surname], [Name], [Patronymic], [Birthdate], [Phone Number], [Address]) 
	VALUES (13, N'11fdd78j4678g0vd', N'Петров', N'Петр', N'Петрович', N'13.11.1990', N'+375291236528', N'г.Минск, ул.Красноармейская 2а'),
	(17, N'26gjk11d4787m3zq', N'Александров', N'Александр', N'Александровчи', N'27.08.1997', N'+375256984585', N'г.Минск, ул.Скрыганова 16'),
	(21, N'69ecc07d3509f6ec', N'Иванов', N'Иван', N'Иванович', N'10.02.1993', N'+375291231212', N'г.Минск, ул.Матусевича 22');
SET IDENTITY_INSERT [dbo].[Courier] OFF

INSERT [dbo].[CurrentCoordinatesOfCourier] ([Courier Code], [Latitude], [Longitude], [Speed]) 
	VALUES (13, N'53.916998573810388', N'27.583909657109989', N'0.0'),
	(17, N'53.908579684921132', N'27.521189869020097', N'0.0'),
	(21, N'53.86987348275568', N'27.59668316382602', N'1.1082288');

SET IDENTITY_INSERT [dbo].[Customer] ON 
INSERT [dbo].[Customer] ([Customer Code], [Surname], [Name], [Patronymic], [Phone Number], [Address], [Email]) 
	VALUES (1, N'Петрова', N'Надежда', N'Павловна', N'+375256781658', N'г.Минск, ул.Янки Мавра 16', N'nadegda2010@ya.ru'),
	(2, N'Голубев', N'Игорь', N'Михайлович', N'+375295523615', N'г.Минск, ул.Короля 11', N'golubev-igor@gmail.com'),
	(3, N'Ефремов', N'Петр', N'Андрееич', N'+375295693211', N'г.Минск, ул.Орловская 36', NULL);
SET IDENTITY_INSERT [dbo].[Customer] OFF

SET IDENTITY_INSERT [dbo].[Delivery] ON 
INSERT [dbo].[Delivery] ([Delivery Code], [Address], [Date], [Time]) 
	VALUES (1, N'г.Минск, ул.Янки Мавра 16', N'16.06.2018', N'14:00'),
	(2, N'г.Минск, ул.Короля 11', N'22.06.2018', N'13:00'),
	(3, N'г.Минск, ул.Орловская 36', N'23.06.2018', N'15:00');
SET IDENTITY_INSERT [dbo].[Delivery] OFF

SET IDENTITY_INSERT [dbo].[Order] ON 
INSERT [dbo].[Order] ([Order Code], [Product Code], [Customer Code], [Delivery Code], [Courier Code], [Quantity], [Status]) 
	VALUES (1, 1, 1, 1, 13, 1, N'Новый'),
	(2, 2, 2, 2, 17, 2, N'Комплектуется'),
	(3, 3, 2, 2, 17, 1, N'Новый'),
	(4, 8, 1, 1, 13, 1, N'Новый'),
	(5, 5, 1, 1, 21, 2, N'Новый'),
	(6, 6, 3, 3, 21, 2, N'Комплектуется'),
	(7, 7, 3, 3, 21, 1, N'В доставке'),
	(8, 8, 2, 2, 21, 1, N'Новый'),
	(9, 9, 1, 1, 21, 1, N'Отменен'),
	(10, 10, 1, 1, 13, 1, N'Отменен');
SET IDENTITY_INSERT [dbo].[Order] OFF
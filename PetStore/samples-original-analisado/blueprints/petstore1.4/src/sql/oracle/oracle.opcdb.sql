drop table LineItemEJBTable;
drop table PurchaseOrderEJBTable;
drop table CardEJBTable;
drop table AddressEJBTable;


CREATE TABLE AddressEJBTable (
   PMPrimaryKey NUMBER(22) NOT NULL,
   reverse_address_poId VARCHAR2(255),
   streetName1 VARCHAR2(255),
   streetName2 VARCHAR2(255),
   city VARCHAR2(80),
   state VARCHAR2(80),
   zipCode VARCHAR2(25),
   country VARCHAR2(80)
   );

ALTER TABLE AddressEJBTable ADD CONSTRAINT pk_AddressEJBTabl 
Primary Key(PMPrimaryKey);


CREATE TABLE CardEJBTable (
  PMPrimaryKey NUMBER(22) NOT NULL,
  reverse_card_poId VARCHAR2(255),
  cardNumber VARCHAR2(80),
  cardType VARCHAR2(25),
  expiryDate VARCHAR2(25) 
  );
  
ALTER TABLE CardEJBTable
   ADD CONSTRAINT pk_CardEJBTabl Primary Key (PMPrimaryKey);


CREATE TABLE PurchaseOrderEJBTable (
  address___PMPrimaryKey NUMBER(22),
  card___PMPrimaryKey NUMBER(22),
  poDate NUMBER(22) NOT NULL,
  poEmailId VARCHAR2(255),
  poId VARCHAR2(255) NOT NULL,
  poLocale VARCHAR2(25),
  poStatus VARCHAR2(25),
  poUserId VARCHAR2(255),
  poValue NUMBER(10,2) NOT NULL 
  );

ALTER TABLE PurchaseOrderEJBTable
   ADD CONSTRAINT pk_PurchaseOrderEJBTabl Primary Key (poId);

ALTER TABLE PurchaseOrderEJBTable ADD CONSTRAINT FK_ADDRESSEJBTABLE_1 Foreign Key (address___PMPrimaryKey) REFERENCES ADDRESSEJBTABLE (PMPRIMARYKEY);
ALTER TABLE PurchaseOrderEJBTable ADD CONSTRAINT FK_CARDEJBTABLE_1 Foreign Key (CARD___PMPRIMARYKEY) REFERENCES CARDEJBTABLE (PMPRIMARYKEY);


  CREATE TABLE LineItemEJBTable (
  PMPrimaryKey NUMBER(22) NOT NULL,
  PurchaseOrderEJB_poId VARCHAR2(255), 
  categoryId VARCHAR2(80),
  itemId VARCHAR2(80),
  lineNumber VARCHAR2(80),
  productId VARCHAR2(80),
  quantityShipped NUMBER(22) NOT NULL,
  unitPrice NUMBER(10,2) NOT NULL 
  );

ALTER TABLE LineItemEJBTable ADD CONSTRAINT pk_LineItemEJBTabl 
Primary Key (PMPrimaryKey);
ALTER TABLE LineItemEJBTable ADD CONSTRAINT FK_LineItem_PurchaseOrder Foreign Key (PURCHASEORDEREJB_POID) REFERENCES PURCHASEORDEREJBTABLE (POID);




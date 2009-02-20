<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	 xmlns:tpaso="http://blueprints.j2ee.sun.com/TPASupplierOrder"
	 xmlns:tpali="http://blueprints.j2ee.sun.com/TPALineItem"
	 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">

 	<xsl:output method="xml" indent="yes" encoding="UTF-8"
	        doctype-public="-//Sun Microsystems, Inc. - J2EE Blueprints Group//DTD SupplierOrder 1.1//EN"
       		doctype-system="/com/sun/j2ee/blueprints/supplierpo/rsrc/schemas/SupplierOrder.dtd"/>

	<xsl:strip-space elements="*" />


  	<xsl:template match="/">
		<tpaso:SupplierOrder>
			<tpaso:OrderId><xsl:value-of select="/tpaso:SupplierOrder/tpaso:OrderId" /></tpaso:OrderId>
			<tpaso:OrderDate><xsl:value-of select="/tpaso:SupplierOrder/tpaso:OrderDate" /></tpaso:OrderDate>
			<xsl:apply-templates select=".//tpaso:ShippingAddress|.//tpali:LineItem"/>
		</tpaso:SupplierOrder>
	</xsl:template>

	<xsl:template match="/tpaso:SupplierOrder/tpaso:ShippingAddress">
		<tpaso:ShippingInfo>
			<tpaso:ContactInfo>
				<tpaso:FamilyName><xsl:value-of select="tpaso:FirstName" /></tpaso:FamilyName>
				<tpaso:GivenName><xsl:value-of select="tpaso:LastName" /></tpaso:GivenName>
				<tpaso:Address>
					<tpaso:StreetName><xsl:value-of select="tpaso:Street" /></tpaso:StreetName>
					<tpaso:City><xsl:value-of select="tpaso:City" /></tpaso:City>
					<tpaso:State><xsl:value-of select="tpaso:State" /></tpaso:State>
					<tpaso:ZipCode><xsl:value-of select="tpaso:ZipCode" /></tpaso:ZipCode>
					<tpaso:Country><xsl:value-of select="tpaso:Country" /></tpaso:Country>
				</tpaso:Address>
				<tpaso:Email><xsl:value-of select="tpaso:Email" /></tpaso:Email>
				<tpaso:Phone><xsl:value-of select="tpaso:Phone" /></tpaso:Phone>
			</tpaso:ContactInfo>
		</tpaso:ShippingInfo>
	</xsl:template>

	<xsl:template match="/tpaso:SupplierOrder/tpaso:LineItems/tpali:LineItem">
		<tpali:LineItem>
			<tpali:CategoryId><xsl:value-of select="@categoryId" /></tpali:CategoryId>
			<tpali:ProductId><xsl:value-of select="@productId" /></tpali:ProductId>
			<tpali:ItemId><xsl:value-of select="@itemId" /></tpali:ItemId>
			<tpali:LineNum><xsl:value-of select="@lineNo" /></tpali:LineNum>
			<tpali:Quantity><xsl:value-of select="@quantity" /></tpali:Quantity>
			<tpali:UnitPrice><xsl:value-of select="@unitPrice" /></tpali:UnitPrice>
		</tpali:LineItem>
	</xsl:template>

</xsl:stylesheet>

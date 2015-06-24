<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<!-- format of output file  -->
	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>
	<xsl:template match="/"> 
		<xsl:variable name="A3.H.page.height">29cm</xsl:variable>
		<xsl:variable name="A3.H.page.width">42cm</xsl:variable>
		<xsl:variable name="A3.V.page.height">42cm</xsl:variable>
		<xsl:variable name="A3.V.page.width">29cm</xsl:variable>
		<xsl:variable name="A4.H.page.height">21cm</xsl:variable>
		<xsl:variable name="A4.H.page.width">29.7cm</xsl:variable>
		<xsl:variable name="A4.V.page.height">29.7cm</xsl:variable>
		<xsl:variable name="A4.V.page.width">21cm</xsl:variable>
		<fo:root >
			<fo:layout-master-set>
				<fo:simple-page-master master-name="main" margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
				<xsl:choose>
					<xsl:when test="/root/pagesize = 'A3-H'"> 
						<xsl:attribute name="page-width"><xsl:value-of select="$A3.H.page.width"/></xsl:attribute>
						<xsl:attribute name="page-height"><xsl:value-of select="$A3.H.page.height"/></xsl:attribute>
					</xsl:when>
					<xsl:when test="/root/pagesize = 'A3-V'"> 
						<xsl:attribute name="page-width"><xsl:value-of select="$A3.V.page.width"/></xsl:attribute>
						<xsl:attribute name="page-height"><xsl:value-of select="$A3.V.page.height"/></xsl:attribute>
					</xsl:when>
					<xsl:when test="/root/pagesize = 'A4-H'"> 
						<xsl:attribute name="page-width"><xsl:value-of select="$A4.H.page.width"/></xsl:attribute>
						<xsl:attribute name="page-height"><xsl:value-of select="$A4.H.page.height"/></xsl:attribute>
					</xsl:when>
					<xsl:when test="/root/pagesize = 'A4-V'"> 
						<xsl:attribute name="page-width"><xsl:value-of select="$A4.V.page.width"/></xsl:attribute>
						<xsl:attribute name="page-height"><xsl:value-of select="$A4.V.page.height"/></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="page-width"><xsl:value-of select="$A4.V.page.width"/></xsl:attribute>
						<xsl:attribute name="page-height"><xsl:value-of select="$A4.V.page.height"/></xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<fo:region-body/>
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="main">
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates/> 
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<xsl:template match="table">
		<fo:table 
			table-layout="fixed" width="100%" font-family="SimHei">
			<!-- select all the cells included in first row and build the fo:table-column -->
			
			<xsl:call-template name="build-columns" />
			<xsl:choose>
				<xsl:when test="./tbody">
						<xsl:apply-templates />
				</xsl:when>
				<xsl:otherwise>
					<fo:table-body>
						<xsl:apply-templates />
					</fo:table-body>
				</xsl:otherwise>
			</xsl:choose>
		</fo:table>
	</xsl:template>
	<!-- process OrderObservation element from input file -->
	<xsl:template match="theader">
		<fo:table-header>
			<xsl:apply-templates />
		</fo:table-header>
	</xsl:template>
	<xsl:template match="tbody">
		<fo:table-body>
			<xsl:apply-templates />
		</fo:table-body>
	</xsl:template>
	<xsl:template match="tr">
		<fo:table-row >
			
			<xsl:apply-templates />
		</fo:table-row>
	</xsl:template>
	<xsl:template match="td">
		<fo:table-cell>
				<xsl:attribute name="number-columns-spanned">
					<xsl:choose>
						<xsl:when test="./@colspan &gt; 1"><xsl:value-of select="./@colspan" /></xsl:when>
						<xsl:otherwise>1</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="number-rows-spanned">
					<xsl:choose>
						<xsl:when test="./@rowspan &gt; 1"><xsl:value-of select="./@rowspan" /></xsl:when>
						<xsl:otherwise>1</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:if test="ancestor::table/@border"> 
					<xsl:attribute name="border-color">black</xsl:attribute>
					<xsl:attribute name="border-style">solid</xsl:attribute>
					<xsl:attribute name="border-width">.5mm</xsl:attribute>
				</xsl:if>
			<fo:block font-weight="bold">
				<xsl:choose>
					<xsl:when test="./@align='center'"><xsl:attribute name="text-align">center</xsl:attribute></xsl:when>
					<xsl:when test="./@align='left'"><xsl:attribute name="text-align">start</xsl:attribute></xsl:when>
					<xsl:when test="./@align='right'"><xsl:attribute name="text-align">end</xsl:attribute></xsl:when>
					<!-- <xsl:when test="parent::tr/@valign='top'"><xsl:attribute name="text-align">before</xsl:attribute></xsl:when>
					<xsl:when test="parent::tr/@valign='bottom'"><xsl:attribute name="text-align">after</xsl:attribute></xsl:when> -->
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="./@align='center'"><xsl:attribute name="font-size"><xsl:value-of select="./@font-size"/></xsl:attribute></xsl:when>
					<xsl:otherwise><xsl:attribute name="font-size">12pt</xsl:attribute></xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates />
			</fo:block>
		</fo:table-cell>
	</xsl:template>
	<xsl:template match="font">
		<fo:inline >
			<xsl:if test="./@color">
				<xsl:attribute name="color"><xsl:value-of select="./@color"/> </xsl:attribute>
			</xsl:if>
			<xsl:apply-templates />
		</fo:inline>
	</xsl:template>
	<xsl:template match="text()">
		<xsl:value-of select="." />
	</xsl:template>

	<!-- for build fo:table-column -->
	<xsl:template name="build-columns">
		<xsl:for-each select="//tr[1]/td">
			<xsl:choose>
				<xsl:when test="./@colspan &gt; 1">
					<xsl:call-template name="for-loop">
						<xsl:with-param name="i">1</xsl:with-param>
						<xsl:with-param name="count"><xsl:value-of select="./@colspan" /></xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<fo:table-column/>
				</xsl:otherwise>
			</xsl:choose>

		</xsl:for-each>
	</xsl:template>
	<xsl:template name="for-loop">
		<xsl:param name="i" />
		<xsl:param name="count" />
		<xsl:if test="$i &lt;= $count">
			<fo:table-column/>
		</xsl:if>
		<xsl:if test="$i &lt;= $count">
			<xsl:call-template name="for-loop">
				<xsl:with-param name="i">
					<xsl:value-of select="$i + 1" />
				</xsl:with-param>
				<xsl:with-param name="count">
					<xsl:value-of select="$count" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
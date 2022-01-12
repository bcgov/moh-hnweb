<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="urn:hl7-org:v3" xmlns:mif="urn:hl7-org:v3/mif" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <xsl:template match="/root">
        <HCIM_IN_FindCandidates >
<!-- unique id for the message -->
            <id>
                <xsl:attribute name="root">
                    <xsl:value-of select="msgConfig/messageIdRoot"/>
                </xsl:attribute>
                <xsl:attribute name="extension">
                    <xsl:value-of select="msgConfig/messageIdExt"/>
                </xsl:attribute>    
            </id>
<!-- message creation time -->
            <creationTime>
                <xsl:attribute name="value">
                    <xsl:value-of select="msgConfig/timestamp"/>
                </xsl:attribute>                   
            </creationTime>
<!-- message version -->
            <versionCode code="V3PR1"/>
<!-- interaction id -->
            <interactionId root="2.16.840.1.113883.3.51.1.1.2" extension="HCIM_IN_FindCandidates" use="BUS" displayable="true"/>
<!-- processing code (P = production) -->
            <processingCode code="P"/>
<!-- processing mode code (T = current processing) -->
            <processingModeCode code="T"/>
<!-- accept ack (TBD) -->
            <acceptAckCode code="NE"/>
<!-- receiver of the message -->
            <receiver typeCode="RCV">
                <device determinerCode="INSTANCE" classCode="DEV">
                <!-- id/@extension identifies the receiving device/system inside the organization -->
                    <id>
                        <xsl:attribute name="root">
                            <xsl:value-of select="hl7Config/recieverDeviceRoot"/>
                        </xsl:attribute>
                        <xsl:attribute name="extension">
                            <xsl:value-of select="hl7Config/recieverDeviceExtension"/>
                        </xsl:attribute>
                    </id>
                    <asAgent classCode="AGNT">
                        <representedOrganization determinerCode="INSTANCE" classCode="ORG">
                                <!-- id/@extension identifies the receiving organization -->
                            <id>
                                <xsl:attribute name="root">
                                    <xsl:value-of select="hl7Config/recieverOrgRoot"/>
                                </xsl:attribute>
                                <xsl:attribute name="extension">
                                    <xsl:value-of select="hl7Config/recieverOrgExtension"/>
                                </xsl:attribute>
                            </id>
                        </representedOrganization>
                    </asAgent>
                </device>
            </receiver>
<!-- sender of the message -->
            <sender typeCode="SND">
                <device determinerCode="INSTANCE" classCode="DEV">
                <!-- id/@extension identifies the sending device/system inside the organization -->
                    <id>
                        <xsl:attribute name="root">
                            <xsl:value-of select="hl7Config/senderDeviceRoot"/>
                        </xsl:attribute>
                        <xsl:attribute name="extension">
                            <xsl:choose>
                                <xsl:when test="msgConfig/sourceSystemOverride">
                                    <xsl:value-of select="msgConfig/sourceSystemOverride"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="hl7Config/senderDeviceExtension"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                    </id>
                    <asAgent classCode="AGNT">
                        <representedOrganization determinerCode="INSTANCE" classCode="ORG">
                                <!-- id/@extension identifies the sending organization -->
                            <id>
                                <xsl:attribute name="root">
                                    <xsl:value-of select="hl7Config/senderOrgRoot"/>
                                </xsl:attribute>
                                <xsl:attribute name="extension">
                                    <xsl:value-of select="msgConfig/organization"/>
                                </xsl:attribute>
                            </id>
                        </representedOrganization>
                    </asAgent>
                </device>
            </sender>
            <controlActProcess classCode="CACT" moodCode="EVN">
                <dataEnterer typeCode="ENT">
                    <assignedPerson classCode="ASSIGNED">
                        <id>
                            <xsl:attribute name="root">
                                <xsl:value-of select="msgConfig/dataEntererRoot"/>
                            </xsl:attribute>
                            <xsl:attribute name="extension">
                                <xsl:value-of select="msgConfig/dataEntererExt"/>
                            </xsl:attribute>                                       
                        </id>
                    </assignedPerson>
                </dataEnterer>
                <xsl:apply-templates/>
            </controlActProcess>
        </HCIM_IN_FindCandidates>
    </xsl:template>
    
    <xsl:template match="findCandidates">
        <queryByParameter>
            <queryByParameterPayload>
                <xsl:if test="address/addressLine1">
                    <person.addr>
                        <value use="PHYS PST">
                            <country>
                                <xsl:value-of select="address/country"/>
                            </country>
                            <state>
                                <xsl:value-of select="address/province"/>
                            </state>
                            <city>
                                <xsl:value-of select="address/city"/>
                            </city>
                            <postalCode>
                                <xsl:value-of select="address/postalCode"/>
                            </postalCode>
                            <streetAddressLine>
                                <xsl:value-of select="address/addressLine1"/>
                            </streetAddressLine>
                        </value>
                    </person.addr>
                </xsl:if>
                <xsl:if test="gender">
                <person.administrativeGender>
                    <xsl:if test="gender ='UNK'">
                        <value>
                            <xsl:attribute name="nullFlavor">
                                <xsl:value-of select="gender"/>
                            </xsl:attribute>
                        </value>
                    </xsl:if>
                    <xsl:if test="gender !='UNK'">
                        <value>
                            <xsl:attribute name="code">
                                <xsl:value-of select="gender"/>
                            </xsl:attribute>
                        </value>
                    </xsl:if>
                </person.administrativeGender>
                </xsl:if>
                <xsl:apply-templates select="birthDate"/>
                <xsl:apply-templates select="deathDate"/>
                <person.name>
                    <value use="L C">
                        <family>
                            <xsl:value-of select="name/surname"/>
                        </family>
                        <given>
                            <xsl:value-of select="name/firstGivenName"/>
                        </given>
                        <xsl:if test="name/secondGivenName!=''">
                            <given>
                                <xsl:value-of select="name/secondGivenName"/>
                            </given>
                        </xsl:if>
                        <xsl:if test="name/thirdGivenName!=''">
                            <given>
                                <xsl:value-of select="name/thirdGivenName"/>
                            </given>
                        </xsl:if>
                    </value>
                </person.name>               
                <xsl:apply-templates select="telecommunication"/>               
            </queryByParameterPayload>
        </queryByParameter>
    </xsl:template>
    
    <xsl:template match="telecommunication">
        <xsl:if test="telecommunicationNumber!=''">
            <person.telecom>
                <value use="H WP MC">
                    <xsl:attribute name="value">tel:
                        <xsl:value-of select="telecommunicationNumber"/>
                    </xsl:attribute>
                </value>
            </person.telecom> 
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="deathDate">
        <person.deceasedTime>
            <value>
                <xsl:attribute name="value">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </value>
        </person.deceasedTime>        
    </xsl:template>
    
    <xsl:template match="birthDate">
        <person.birthTime>
            <value>
                <xsl:attribute name="value">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </value>
        </person.birthTime>
    </xsl:template>
    
    <xsl:template match="hl7Config"/>
    <xsl:template match="msgConfig"/>
</xsl:stylesheet>
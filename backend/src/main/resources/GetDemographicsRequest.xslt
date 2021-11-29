<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="urn:hl7-org:v3" xmlns:mif="urn:hl7-org:v3/mif" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <xsl:template match="/root">
        <HCIM_IN_GetDemographics >
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
            <interactionId root="2.16.840.1.113883.3.51.1.1.2" extension="HCIM_IN_GetDemographics.History"/>
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
            <controlActProcess classCode="ACCM" moodCode="EVN">
                <dataEnterer typeCode="CST">
                    <assignedPerson classCode="ENT">
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
        </HCIM_IN_GetDemographics>
    </xsl:template>
    
    <xsl:template match="getDemographics">
        <queryByParameter>
            <queryByParameterPayload>
                <person.id>
                    <value root="2.16.840.1.113883.3.51.1.1.6" >
                        <xsl:attribute name="assigningAuthorityName">
                            <xsl:value-of select="mrnSource"/>
                        </xsl:attribute>
                        <xsl:attribute name="extension">
                            <xsl:value-of select="mrn"/>
                        </xsl:attribute>
                    </value>
                </person.id>
            </queryByParameterPayload>
        </queryByParameter>
                      
    </xsl:template>
    
    <xsl:template match="hl7Config"/>
    <xsl:template match="msgConfig"/>
</xsl:stylesheet>
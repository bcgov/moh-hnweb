<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <getDemographicsResponse>
        <xsl:apply-templates/>
        </getDemographicsResponse>
    </xsl:template>
    <xsl:template match="queryAck">
        <resultCount><xsl:value-of select="resultTotalQuantity/@value"/></resultCount>
        <message>
            <details><xsl:value-of select="queryResponseCode/@code"/></details>
        </message>
    </xsl:template>
    <xsl:template match="target">
        <person>
            <xsl:choose>
                <xsl:when test="identifiedPerson/name[@use='L' and @nullFlavor='MSK']">
                    <declaredNameMasked>true</declaredNameMasked>
                </xsl:when>
                <xsl:when test="identifiedPerson/name[count(validTime[@value])=0 and @use='L']">
                    <declaredName>
                        <type><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='L']/@use"/></type>
                        <surname><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='L']/family"/></surname>
                        <firstGivenName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='L']/given[not(@qualifier)][1]"/></firstGivenName>
                        <secondGivenName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='L']/given[not(@qualifier)][2]"/></secondGivenName>
                        <thirdGivenName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='L']/given[not(@qualifier)][3]"/></thirdGivenName>
                        <preferredName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='L']/given[@qualifier='CL']"/></preferredName>
                    </declaredName>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="identifiedPerson/name[@use='C' and @nullFlavor='MSK']">
                    <documentedNameMasked>true</documentedNameMasked>
                </xsl:when>
                <xsl:when test="identifiedPerson/name[count(validTime[@value])=0 and @use='C']">
                    <documentedName>
                        <type><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and@use='C']/@use"/></type>
                        <surname><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='C']/family"/></surname>
                        <firstGivenName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='C']/given[not(@qualifier)][1]"/></firstGivenName>
                        <secondGivenName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='C']/given[not(@qualifier)][2]"/></secondGivenName>
                        <thirdGivenName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='C']/given[not(@qualifier)][3]"/></thirdGivenName>
                        <preferredName><xsl:value-of select="identifiedPerson/name[count(validTime[@value])=0 and @use='C']/given[@qualifier='CL']"/></preferredName>
                    </documentedName>        
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="identifiedPerson/birthTime"/>
            <xsl:apply-templates select="identifiedPerson/deceasedTime[@value]"/>
            <xsl:apply-templates select="identifiedPerson/deceasedInd[@value]"/>
            <phn><xsl:value-of select="identifiedPerson/id/@extension"/></phn>
            <gender>
                <xsl:choose>
                    <xsl:when test="identifiedPerson/administrativeGenderCode[@nullFlavor ='MSK']">
                        *
                    </xsl:when>
                    <xsl:when test="identifiedPerson/administrativeGenderCode[@nullFlavor ='UNK']">UNK</xsl:when>
                    <xsl:when test="identifiedPerson/administrativeGenderCode[@code !='']">
                        <xsl:apply-templates select="identifiedPerson/administrativeGenderCode/@code"/>
                    </xsl:when>
                </xsl:choose>
            </gender>
            
            <!-- Process physical and mailing addresses -->
            <xsl:choose>
                <xsl:when test="addr[@use='PHYS VER' and @nullFlavor='MSK']">
                    <physicalAddress>
                        <ver>true</ver>
                        <masked>true</masked>
                    </physicalAddress>
                </xsl:when>
                <xsl:when test="addr[@use='PHYS' and @nullFlavor='MSK']">
                    <physicalAddress>
                        <ver>false</ver>
                        <masked>true</masked>
                    </physicalAddress>
                </xsl:when>
                <xsl:when test="addr[@use='PHYS VER' and count(useablePeriod)=0]">
                    <physicalAddress>
                        <xsl:apply-templates select="addr[@use='PHYS VER' and count(useablePeriod)=0][1]"/>
                        <ver>true</ver>
                        <masked>false</masked>
                    </physicalAddress>    
                </xsl:when>
                <xsl:when test="addr[@use='PHYS' and count(useablePeriod)=0]">
                    <physicalAddress>
                        <xsl:apply-templates select="addr[starts-with(@use,'PHYS') and count(useablePeriod)=0][1]"/>
                        <ver>false</ver>
                        <masked>false</masked>
                    </physicalAddress>    
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="addr[@use='PST VER' and @nullFlavor='MSK']">
                    <mailingAddress>
                        <ver>true</ver>
                        <masked>true</masked>
                    </mailingAddress>
                </xsl:when>
                <xsl:when test="addr[@use='PST' and @nullFlavor='MSK']">
                    <mailingAddress>
                        <ver>false</ver>
                        <masked>true</masked>
                    </mailingAddress>
                </xsl:when>
                <xsl:when test="addr[@use='PST VER' and count(useablePeriod)=0]">
                    <mailingAddress>
                        <xsl:apply-templates select="addr[@use='PST VER' and count(useablePeriod)=0][1]"/>
                        <ver>true</ver>
                        <masked>false</masked>
                    </mailingAddress>    
                </xsl:when>
                <xsl:when test="addr[@use='PST' and count(useablePeriod)=0]">
                    <mailingAddress>
                        <xsl:apply-templates select="addr[@use='PST' and count(useablePeriod)=0][1]"/>
                        <ver>false</ver>
                        <masked>false</masked>
                    </mailingAddress>    
                </xsl:when>
            </xsl:choose>
            
            <!--Process phone numbers -->
            <xsl:choose>
                <xsl:when test="telecom[@use='H' and @nullFlavor='MSK']">
                    <homePhone>
                        <masked>true</masked>
                        <type>H</type>
                    </homePhone>
                </xsl:when>
                <xsl:when test="telecom[@use='H' and count(useablePeriod)=0 and substring(@value,1,3)='tel']">
                    <homePhone>
                        <xsl:apply-templates select="telecom[@use='H' and count(useablePeriod)=0 and substring(@value,1,3)='tel'][1]"/>
                    </homePhone>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="telecom[@use='WP' and @nullFlavor='MSK']">
                    <workPhone>
                        <masked>true</masked>
                        <type>WP</type>
                    </workPhone>
                </xsl:when>
                <xsl:when test="telecom[@use='WP' and count(useablePeriod)=0 and substring(@value,1,3)='tel']">
                    <workPhone>
                        <xsl:apply-templates select="telecom[@use='WP' and count(useablePeriod)=0 and substring(@value,1,3)='tel'][1]"/>
                    </workPhone>
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="telecom[@use='MC' and @nullFlavor='MSK']">
                    <mobilePhone>
                        <masked>true</masked>
                        <type>MC</type>
                    </mobilePhone>
                </xsl:when>
                <xsl:when test="telecom[@use='MC' and count(useablePeriod)=0 and substring(@value,1,3)='tel']">
                    <mobilePhone>
                        <xsl:apply-templates select="telecom[@use='MC' and count(useablePeriod)=0 and substring(@value,1,3)='tel'][1]"/>
                    </mobilePhone>
                </xsl:when>
            </xsl:choose>
            
            <xsl:if test="telecom[@use='H' and count(useablePeriod)=0 and substring(@value,1,3)='mai']">
            <homeEmail>
                <xsl:apply-templates select="telecom[@use='H' and count(useablePeriod)=0 and substring(@value,1,3)='mai'][1]"/>                
            </homeEmail>
            </xsl:if>
            <xsl:if test="telecom[@use='WP' and count(useablePeriod)=0 and substring(@value,1,3)='mai']">
            <workEmail>
                <xsl:apply-templates select="telecom[@use='WP' and count(useablePeriod)=0 and substring(@value,1,3)='mai'][1]"/>                
            </workEmail>
            </xsl:if>
            <xsl:if test="telecom[@use='MC' and count(useablePeriod)=0 and substring(@value,1,3)='mai']">
            <mobileEmail>
                <xsl:apply-templates select="telecom[@use='MC' and count(useablePeriod)=0 and substring(@value,1,3)='mai'][1]"/>                
            </mobileEmail>            
            </xsl:if>            
            <medicalRecordNumbers>
                <xsl:for-each select="id[@displayable='true']">
                    <medicalRecordNumber>
                        <mrn>
                            <xsl:value-of select="@extension"/>
                        </mrn>
                        <source>
                            <xsl:value-of select="@assigningAuthorityName"/>
                        </source>
                        <mrnStatus>true</mrnStatus>
                    </medicalRecordNumber>
                </xsl:for-each>
                <xsl:for-each select="identifiedPerson/id[@displayable='true']">
                    <medicalRecordNumber>
                        <mrn>
                            <xsl:value-of select="@extension"/>
                        </mrn>
                        <source>
                            <xsl:value-of select="@assigningAuthorityName"/>
                        </source>
                        <mrnStatus>true</mrnStatus>
                    </medicalRecordNumber>
                </xsl:for-each>
                <xsl:for-each select="identifiedPerson/playedOtherIDs/id">
                    <xsl:choose>
                        <xsl:when test="@displayable='false'">
                        </xsl:when>
                        <xsl:otherwise>
                            <medicalRecordNumber>
                                <mrn>
                                    <xsl:value-of select="@extension"/>
                                </mrn>
                                <source>
                                    <xsl:value-of select="@assigningAuthorityName"/>
                                </source>
                                <mrnStatus>true</mrnStatus>
                            </medicalRecordNumber>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>                
            </medicalRecordNumbers>       
            <telecommunicationHistory>
                <xsl:for-each select="telecom[count(useablePeriod)>0]">
                    <telecommunication>
                        <xsl:apply-templates select="."/>
                    </telecommunication>
                </xsl:for-each>
            </telecommunicationHistory>
            <addressHistory>
                <xsl:for-each select="addr[count(useablePeriod)>0]">
                    <address>
                        <xsl:apply-templates select="."/>
                        <ver>
                            <xsl:if test="contains(@use, 'VER')">true</xsl:if>
                        </ver>
                        <masked>
                            <xsl:if test="@nullFlavor='MSK'">true</xsl:if>
                        </masked>                        
                    </address>
                </xsl:for-each>
            </addressHistory>     
            <mrnHistory>
                <xsl:for-each select="id[@displayable='false']">
                    <medicalRecordNumber>
                        <mrn>
                            <xsl:value-of select="@extension"/>
                        </mrn>
                        <source>
                            <xsl:value-of select="@assigningAuthorityName"/>
                        </source>
                        <mrnStatus>false</mrnStatus>
                    </medicalRecordNumber>
                </xsl:for-each>
                <xsl:for-each select="identifiedPerson/id[@displayable='false']">
                    <medicalRecordNumber>
                        <mrn>
                            <xsl:value-of select="@extension"/>
                        </mrn>
                        <source>
                            <xsl:value-of select="@assigningAuthorityName"/>
                        </source>
                        <mrnStatus>false</mrnStatus>
                    </medicalRecordNumber>
                </xsl:for-each>                
                <xsl:for-each select="identifiedPerson/playedOtherIDs/id">
                    <xsl:choose>
                        <xsl:when test="@displayable='false'">
                            <medicalRecordNumber>
                                <mrn>
                                    <xsl:value-of select="@extension"/>
                                </mrn>
                                <source>
                                    <xsl:value-of select="@assigningAuthorityName"/>
                                </source>
                                <mrnStatus>false</mrnStatus>
                            </medicalRecordNumber>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            </mrnHistory>
            <nameHistory>
                <xsl:for-each select="//name[count(validTime[@value])>0]">
                    <name>
                        <type><xsl:value-of select="./@use"/></type>
                        <surname><xsl:value-of select="./family"/></surname>
                        <firstGivenName><xsl:value-of select="./given[not(@qualifier)][1]"/></firstGivenName>
                        <secondGivenName><xsl:value-of select="./given[not(@qualifier)][2]"/></secondGivenName>
                        <thirdGivenName><xsl:value-of select="./given[not(@qualifier)][3]"/></thirdGivenName>
                        <preferredName><xsl:value-of select="./given[@qualifier='CL']"/></preferredName>
                        <effectiveDate><xsl:value-of select="validTime/@value"/></effectiveDate>
                    </name>
                </xsl:for-each>
            </nameHistory>
            <personHistory>
                <xsl:if test="identifiedPerson/id[@displayable='false']">
                    <xsl:for-each select="identifiedPerson/id[@displayable='false']">
                        <person>
                            <phn>
                                <xsl:value-of select="@extension"/>
                            </phn>
                            <phnStatus>
                                <xsl:value-of select="@displayable"/>
                            </phnStatus>
                        </person>
                    </xsl:for-each>
                </xsl:if>
            </personHistory>            
        </person>
    </xsl:template>
    <xsl:template match="identifiedPerson/birthTime[@value]">
        <birthDate><xsl:value-of select="@value"/></birthDate>
    </xsl:template>
    <xsl:template match="identifiedPerson/birthTime[@nullFlavor='MSK']">
        <birthDateMasked>true</birthDateMasked>
    </xsl:template>    
    <xsl:template match="identifiedPerson/deceasedTime">
        <deathDate><xsl:value-of select="@value"/></deathDate>
    </xsl:template>
    <xsl:template match="identifiedPerson/deceasedInd">
        <deceased><xsl:value-of select="@value"/></deceased>
    </xsl:template>
    <xsl:template match="addr">
        <addressType>
            <xsl:if test="starts-with(@use, 'PHYS')">PHYS</xsl:if>
            <xsl:if test="starts-with(@use, 'PST')">PST</xsl:if>
        </addressType>
        <addressLine1>
            <xsl:value-of select="streetAddressLine[1]"/>
        </addressLine1>
        <addressLine2>
            <xsl:value-of select="streetAddressLine[2]"/>
        </addressLine2>
        <addressLine3>
            <xsl:value-of select="streetAddressLine[3]"/>
        </addressLine3>        
        <city>
            <xsl:value-of select="city"/>
        </city>
        <province>
            <xsl:value-of select="state"/>
        </province>
        <postalCode>
            <xsl:value-of select="postalCode"/>
        </postalCode>
        <country>
            <xsl:value-of select="country"/>
        </country>
        <xsl:if test="useablePeriod">
        <effectiveDate><xsl:value-of select="useablePeriod/@value"/></effectiveDate>
        </xsl:if>
    </xsl:template>
    <xsl:template match="target/id">
        <medicalRecordNumber>
            <mrn>
                <xsl:value-of select="@extension"/>
            </mrn>
            <source>
                <xsl:value-of select="@assigningAuthorityName"/>
            </source>
            <mrnStatus>
                <xsl:value-of select="@displayable"/>
            </mrnStatus>
        </medicalRecordNumber>
    </xsl:template>       
    <xsl:template match="telecom">
            <telecommunicationNumber><xsl:value-of select="substring-after(@value,':')"/></telecommunicationNumber>
            <type><xsl:value-of select="@use"/></type>
            <xsl:if test="useablePeriod">
            <effectiveDate><xsl:value-of select="useablePeriod/@value"/></effectiveDate>
            </xsl:if>
    </xsl:template>
</xsl:stylesheet>
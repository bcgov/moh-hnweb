<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <findCandidatesResponse>
        <xsl:apply-templates select="//HCIM_IN_FindCandidatesResponse/id"/>
        <xsl:apply-templates select="//queryAck"/>
        <results>
            <xsl:apply-templates select="//controlActProcess/subject"/>
        </results>
        </findCandidatesResponse>
    </xsl:template>    
    <xsl:template match="HCIM_IN_FindCandidatesResponse/id">
    	<messageIdExtension><xsl:value-of select="@extension"/></messageIdExtension>
    </xsl:template>
    <xsl:template match="controlActProcess/queryAck">
        <resultCount><xsl:value-of select="resultTotalQuantity/@value"/></resultCount>
        <message>
            <details><xsl:value-of select="queryResponseCode/@code"/></details>
        </message>
    </xsl:template>
    <xsl:template match="target">
        <findCandidatesResult>
            <score>
                <xsl:value-of select="subjectOf/observationEvent/value/@value"/>
            </score>
        <person>
            <xsl:choose>
                <xsl:when test="identifiedPerson/name[@use='L' and @nullFlavor='MSK']">
                    <declaredNameMasked>true</declaredNameMasked>
                </xsl:when>
                <xsl:when test="identifiedPerson/name[count(validTime[@value])=0 and @use='L']">
                    <declaredName>
                        <type><xsl:value-of select="identifiedPerson/name[@use='L']/@use"/></type>
                        <surname><xsl:value-of select="identifiedPerson/name[@use='L']/family"/></surname>
                        <firstGivenName><xsl:value-of select="identifiedPerson/name[@use='L']/given[not(@qualifier)][1]"/></firstGivenName>
                        <secondGivenName><xsl:value-of select="identifiedPerson/name[@use='L']/given[not(@qualifier)][2]"/></secondGivenName>
                        <thirdGivenName><xsl:value-of select="identifiedPerson/name[@use='L']/given[not(@qualifier)][3]"/></thirdGivenName>
                        <preferredName><xsl:value-of select="identifiedPerson/name[@use='L']/given[@qualifier='CL']"/></preferredName>
                    </declaredName>
                </xsl:when>                
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="identifiedPerson/name[@use='C' and @nullFlavor='MSK']">
                    <documentedNameMasked>true</documentedNameMasked>
                </xsl:when>
                <xsl:when test="identifiedPerson/name[count(validTime[@value])=0 and @use='C']">
                    <documentedName>
                        <type><xsl:value-of select="identifiedPerson/name[@use='C']/@use"/></type>
                        <surname><xsl:value-of select="identifiedPerson/name[@use='C']/family"/></surname>
                        <firstGivenName><xsl:value-of select="identifiedPerson/name[@use='C']/given[1]"/></firstGivenName>
                        <secondGivenName><xsl:value-of select="identifiedPerson/name[@use='C']/given[2]"/></secondGivenName>
                        <thirdGivenName><xsl:value-of select="identifiedPerson/name[@use='C']/given[3]"/></thirdGivenName>
                        <preferredName><xsl:value-of select="identifiedPerson/name[@use='C']/given[@qualifier='CL']"/></preferredName>
                    </documentedName> 
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="identifiedPerson/birthTime"/>
            <xsl:apply-templates select="identifiedPerson/deceasedTime[@value]"/>
            <phn><xsl:value-of select="identifiedPerson/id/@extension"/></phn>
            <gender>
                <xsl:choose>
                    <xsl:when test="identifiedPerson/administrativeGenderCode[@nullFlavor ='MSK']">
                        *
                    </xsl:when>
                    <xsl:when test="identifiedPerson/administrativeGenderCode[@nullFlavor ='UNK']">UNK</xsl:when>
                    <xsl:when test="identifiedPerson/administrativeGenderCode[@code !='']">
                        <xsl:value-of select="identifiedPerson/administrativeGenderCode/@code"/>
                    </xsl:when>
                </xsl:choose>
            </gender>            
            <!-- Process physical and mailing addresses -->
            <xsl:choose>
                <xsl:when test="addr[starts-with(@use,'PHYS') and @nullFlavor='MSK']">
                    <physicalAddress>
                        <addressType>PHYS</addressType>
                        <masked>true</masked>
                    </physicalAddress>
                </xsl:when>
                <xsl:when test="addr[starts-with(@use,'PHYS') and count(useablePeriod)=0]">
                    <physicalAddress>
                        <xsl:apply-templates select="addr[starts-with(@use,'PHYS') and count(useablePeriod)=0][1]"/>
                    </physicalAddress>    
                </xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="addr[starts-with(@use,'PST') and @nullFlavor='MSK']">
                    <mailingAddress>
                        <addressType>PST</addressType>
                        <masked>true</masked>
                    </mailingAddress>
                </xsl:when>
                <xsl:when test="addr[starts-with(@use,'PST') and count(useablePeriod)=0]">
                    <mailingAddress>
                        <xsl:apply-templates select="addr[starts-with(@use,'PST') and count(useablePeriod)=0][1]"/>
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
            <xsl:if test="telecom[@use='H' and substring(@value,1,3)='mai']">
            <homeEmail>
                <xsl:apply-templates select="telecom[@use='H' and substring(@value,1,3)='mai'][1]"/>
            </homeEmail>
            </xsl:if>
            <xsl:if test="telecom[@use='WP' and substring(@value,1,3)='mai']">
            <workEmail>
                <xsl:apply-templates select="telecom[@use='WP' and substring(@value,1,3)='mai'][1]"/>                
            </workEmail>
            </xsl:if>
            <xsl:if test="telecom[@use='MC' and substring(@value,1,3)='mai']">
            <mobileEmail>
                <xsl:apply-templates select="telecom[@use='MC' and substring(@value,1,3)='mai'][1]"/>                
            </mobileEmail>            
            </xsl:if>                        
            <medicalRecordNumbers>
                <xsl:apply-templates select="id"/>
            </medicalRecordNumbers>            
        </person>
        </findCandidatesResult>
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
    <xsl:template match="addr">
        <addressType>
            <xsl:value-of select="@use"/>
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
    </xsl:template>
    <xsl:template match="target/id">
        <medicalRecordNumber>
            <mrn>
                <xsl:value-of select="@extension"/>
            </mrn>
            <source>
                <xsl:value-of select="@assigningAuthorityName"/>
            </source>
        </medicalRecordNumber>
    </xsl:template>       
    <xsl:template match="telecom">
            <telecommunicationNumber><xsl:value-of select="@value"/></telecommunicationNumber>
            <type><xsl:value-of select="@use"/></type>
    </xsl:template>      
</xsl:stylesheet>
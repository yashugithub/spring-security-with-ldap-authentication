## spring-security-with-ldap-authentication

#### Introduction:
This document is for to understand the how to integrate LDAP in spring security for authentication mode. This document is limited only for authentication purpose. Before proceeding with this, understand spring boot and spring boot security.

##### LDAP:  L(DAP) – Lightweight(Directory Access Protocol) 
The information about LDAP is captured for the reader to understand the LDAP Data Information Tree(DIT). 

LDAP is a hierarchical with objectClasses and attributes. LDAP doesn’t define how data is stored in LDAP server, only how LDAP data accessed.  Example when communicating to LDAP server, you have no idea where the data is comes from. 

LDAP is a protocol ldap:// similar to JDBC/http/

##### Understand LDAP object and its tree structure
LDAF data model is defined by DIT(Data Information Tree), when DIT is created/populated each entry will be uniquely identified. Each Entry composed of one or more objectClasses, and these objectClasses act as a container for attributes. Finally, Attributes are holding actual data. 

![alt text](/src/main/resources/DIT.png)

Below is the table illustrated the couple of objectClasses and attributeName
Attribute Name |	objectClasses | Alias	Description
------------ | ------------- | ------------- 
dc | dcObject | domainComponent	It is domain name or organization name
cn | person  organizationalPerson device applicationEntry | 	 commonName	
ou	| organizationUint	organizationalUnitName	| department, branches or any sub division
mail	| | 		Email address 

##### Which kind of information suitable to store in LDAP server?

###### Data example: Banking domain 

In Bank, there are multiple kind of data will be stored example payments, these transactions are frequently update on every data reading. And similar way there is Bank Branch information these are quite constant for long time and will not be change very frequently. Out of these two data Bank Branch information can be stored in LDAP server as LDAP is characterized as a write once and read many times service.

##### What is advantage of using LDAP?

As we come across the scenario, what kind of data suitable in LDAP server then there is no room discuss about performance because in general RDBS is significantly faster than LDAP.  

Then why do we use LDAP?
-	LDAP can be used to connect Remote/local data access, most of the time RDBMS access local data.
-	LDAP system provide a capability to replicate the data in one or more LDAP servers or application without adding either code or changing the external access of that data
#### LDIF (LDAP Data Interchange Format) 
In this section we will learn about design DIT (Data information Tree) with simple from root entry to hierarchical separator entry. Let’s try to design the LDIF for the below use case
##### Use Case:  
The ABC organization maintain its employee details as he is permanent, contractor or consultant.

#####Solution: 
Before creating LDIF file, draw DIT for people structure

![alt text](/src/main/resources/dit-people.jpg)
 
With the above information let’s start creating the LDIF file with objectClasses and attributeName

    // DEFINE DIT ROOT/BASE/SUFFIX 
    // uses RFC 2377 format
    // replace example and com as necessary below
    // or for experimentation leave as is
    // dcObject is an AUXILLIARY objectclass and MUST
    // have a STRUCTURAL objectclass (organization in this case)
    
    // this is an ENTRY sequence and is preceded by a BLANK line

    dn: dc=ABC,dc=net
    dc: ABC
    description: ABC employee list
    objectClass: dcObject
    objectClass: organization
    o: ABC, net.

    // FIRST Level hierarchy - people 
    dn: ou=people, dc=ABC,dc=net
    ou: people
    description: All people in ABC organisation
    objectclass: organizationalunit

    // SECOND Level hierarchy
    //  ADD a single entry under FIRST (people) level
    dn: ou=permanent, ou=people, dc=ABC,dc=net
    ou: permanent
    description: All people in ABC organisation
    objectclass: organizationalunit
    dn: ou=contractor, ou=people, dc=ABC,dc=net
    ou: contractor
    description: All people in ABC organisation
    objectclass: organizationalunit
    dn: ou=consultant, ou=people, dc=ABC,dc=net
    ou: consultant
    description: All people in ABC organisation
    objectclass: organizationalunit

    //  Data entry Level hierarchy
    //  ADD a single entry under Second level
    dn: cn=Joe,ou=contractor,ou=people,dc=ABC,dc=net
    objectclass: inetOrgPerson
    cn: Joe
    cn: Joe L
    sn: L
    uid: joel
    userpassword: joepwd
    homephone: 555-111-2222
    mail: joe123@example.com
    mail: joel@example.com
    description: Joe is a developer
    ou: contractor



#### Learn Spring Security with LDAP authentication
In this section we will use the embedded LDAP server and authenticate the rest api with LDAP authentication mode.
In the first place you can create a spring security ldap demo project from https://start.spring.io/ . 
 

Then add the below dependencies to enable LDAP functionalities

    implementation("org.springframework.ldap:spring-ldap-core")
    implementation("org.springframework.security:spring-security-ldap")
    implementation("com.unboundid:unboundid-ldapsdk")


###### application.properties
 
> // Below are the minimal configuration to strat the spring embedded //LDAP server 
spring.ldap.embedded.base-dn=dc=ABC,dc=net
spring.ldap.embedded.ldif=classpath:ldap-server.ldif
spring.ldap.embedded.port=8389

###### ldap-server.ldif
Create the ldap-server.ldif file under src/main/resources and copy the above ldif example data 
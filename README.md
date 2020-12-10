![build](https://github.com/abdulwahabO/filebox/workflows/build/badge.svg)

## Overview

A simple web-based file storage service built on top of AWS infrastructure. Users upload small files which are 
stored on Amazon S3. File metadata and user data are persisted on a DynamoDB table. I chose DynamoDB for the 
database because it's a good fit for the one-to-many relationship between users and files.

User login is implemented using the OAuth 2.0 authorization code flow with Github as the authorization server. 
User session is managed using browser cookies. To make things interesting I chose to implement the OAuth2 flow and 
session management manually without using any of the robust integrations offered by the Spring Framework.

![](filebox-demo.gif)

## Tech Stack

* Java 11 - The application backend code is written in Java.
* Spring Boot - An application server, web framework and HTML templating engine.
* AWS DynamoDB - NoSQL store for persisting user data and file metadata.
* AWS S3 - For secure storage of the actual files.
* AWS Elastic Beanstalk - For conveniently deploying the application to an AWS production environment.
* Bulma CSS - For the layout and styling of the user interface.

## Deploying Locally

To deploy this project on your local machine you'll need a Github OAuth app and AWS credentials. Also, a Maven
 profile (in `settings.xml`) with properties as shown below. 
 
```xml
<profile>
    <id>filebox-local</id>
	    <properties>
		    <aws.s3.bucket>{TODO}</aws.s3.bucket>
		    <aws.dynamo.user.table>{TODO}</aws.dynamo.user.table>
		    <aws.s3.region>{TODO}</aws.s3.region>
		    <aws.dynamo.region>{TODO}</aws.dynamo.region>
		    <github.client.secret>{TODO}</github.client.secret>
		    <github.client.id>{TODO}</github.client.id>
		    <filebox.host>{TODO}</filebox.host>
		    <filebox.port>{TODO}</filebox.port>	
	    </properties>
</profile>
```

The application can be started using the Spring Boot Maven plugin and passing the name of the Maven profile i.e 
`mvn spring-boot:run -Pfilebox-local`.
 
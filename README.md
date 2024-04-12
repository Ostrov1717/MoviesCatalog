# MoviesCatalog
Description of the film catalog API, which contains information about films and actors.

This repository contains the results of the homework Designing a REST API from the Java online program (self-paced) EPAM University course in the Introduction to Data Base and Web Development section.

As a topic for work, it was chosen to create a description of an API catalog that contains information about films and the actors who starred in them.
The API is supposed to:
- provide information about all movies included in the catalog (in the structure there is - a unique movie id, title, brief description, list of actors who starred in the film);
- provide information about all actors included in the catalog (in the structure there is - a unique number id of the actor, first name, last name, list of films in which the actor starred);
- provide information about a film or actor using their unique number id;
- provide the ability to add and delete information about a movie or actor in/from the catalog;
- support pagination of response  which contains a large amount of information (in our case, requests with complete lists of movies and actors);
- support caching of information (in this work, these are responses about a film or actor with a specific id);
- support authentication and authorization of users (in this work, all users are allowed to perform requests for information(get requests), even without authentication, and only authenticated and authorized users can add and delete information about films or actors);
- the results of processing requests are matched with the corresponding response codes and described.

Swagger Editor was used to prepare the API description. The description results are saved in files in yaml and json formats (which this editor uses):
- MovieCatalog.yaml;
- MovieCatalog.json.

For ease of use, the file can be uploaded to the online Swagger Editor (https://editor.swagger.io).

In addition, in order to practice writing simple web services, a server was developed that provides operation in accordance with the description of this API.
The catalog is based on a demo sample database of the PostgresSQL - dvdrental (from which, for the purposes of this task, unnecessary tables and data were removed and a table was added to identify and authorize users who can change database data). The database backup is contained in the resources java folder of the project.
The server is implemented to process http requests using the Java web application server TOMCAT, developed custom servlets and filters, and the JDBC API.

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h1>Hello !</h1>
<h2>It's movies catalog</h2>
<hr/>
<h3>GET requests</h3>
<p>Get information about all movies included in the catalog: <a href="http://localhost:8080/movies">http://localhost:8080/movies</a></p>
<p>Get information about all actors included in the catalog: <a href="http://localhost:8080/actors">http://localhost:8080/actors</a></p>
<p>Get information about all movies included in the catalog with pagination: <a href="http://localhost:8080/movies?page=2">http://localhost:8080/movies?page=2</a></p>
<p>Get information about all actors included in the catalog with pagination: <a href="http://localhost:8080/actors?page=3">http://localhost:8080/actors?page=3</a></p>
<p>Request information about an movie with a specific id (example id=10): <a href="http://localhost:8080/movie/10">http://localhost:8080/movie/{id}</a></p>
<p>Request information about an actor with a specific id (example id=11): <a href="http://localhost:8080/actor/11">http://localhost:8080/actor/{id}</a></p>
<hr/>
<h3>POST requests (requires basic http authentication (user:Alex, password: 9999) and request body)</h3>
<p>Add a new movie to the catalog (example id=1001):<a methods="post" href="http://localhost:8080/movie">(POST) http://localhost:8080/movie</a></p>
<p>Add a new actor to the catalog: (POST) http://localhost:8080/actor</p>
<hr/>
<h3>DELETE requests (requires basic http authentication (user:Alex, password: 9999))</h3>
<p>Deleting information from catalog about the movie with a specific id (example id=1001):<a methods="delete" href="http://localhost:8080/movie/1001">(DELETE) http://localhost:8080/movie/{id}</a></p>
<p>Deleting information from catalog about the actor with a specific id:(DELETE) http://localhost:8080/actor/{id}</p>
</body>
</html>
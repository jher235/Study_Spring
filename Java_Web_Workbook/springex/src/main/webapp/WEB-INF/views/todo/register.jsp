<%--
  Created by IntelliJ IDEA.
  User: 김재헌
  Date: 2024-02-21
  Time: 오전 11:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hello World!</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>

<div class="container-fluid">
    <div class="row">
        <!--            <h1>Header</h1>-->
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Navbar</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                    <div class="navbar-nav">
                        <a class="nav-link active" aria-current="page" href="#">Home</a>
                        <a class="nav-link" href="#">Features</a>
                        <a class="nav-link" href="#">Pricing</a>
                        <a class="nav-link disabled" aria-disabled="true">Disabled</a>
                    </div>
                </div>
            </div>
        </nav>

        <div class="card">
            <h5 class="card-header">Featured</h5>
            <div class="card-body">
                <form action="/todo/register" method="post">

                    <div class="mb-3 row">
                        <label for="inputTitle" class="col-sm-2 col-form-label">Title</label>
                        <div class="col-sm-10">
                            <input type="text" id="inputTitle" name="title" class="form-control" placeholder="Title">
                        </div>
                    </div>

                    <div class="mb-3 row">
                        <label for="inputDueDate" class="col-sm-2 col-form-label">DueDate</label>
                        <div class="col-sm-10">
                            <input type="date" id="inputDueDate" name="dueDate" class="form-control" placeholder="dueDate">
                        </div>
                    </div>

                    <div class="mb-3 row">
                        <label for="inputWriter" class="col-sm-2 col-form-label">Writer</label>
                        <div class="col-sm-10">
                            <input type="text" id="inputWriter" name="writer" class="form-control" placeholder="Writer">
                        </div>
                    </div>

                    <div class="my-4">
                        <div class="float-end">
                            <button type="submit" class="btn btn-primary">Submit</button>
                            <button type="result" class="btn btn-secondary">Reset</button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>

    <div class="row footer">
        <div class="row fixed-bottom" style="z-index: -100">
            <footer class="py-1 my-1">
                <p class="text-center text-muted">Footer</p>
            </footer>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>

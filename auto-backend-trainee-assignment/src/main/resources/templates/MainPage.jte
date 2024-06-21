@import dto.MainPage
@param MainPage page


<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Генератор ссылок</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body class="d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Генератор ссылок</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
    </div>
</nav>
@if(page.getFlash() != null)
    <div class="alert alert-warning" role="alert">
        ${page.getFlash()}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
    </div>
@endif
<div class="container text-center">
    <div class="row">
        <div class="col">
            <h1>Генератор ссылок</h1>
        </div>
    </div>
    <form action="/" method="post" class="rss-form text-body">
        <div class="mb-3">
            <label class="form-label">Бесплатно создавайте короткие ссылки</label>
            <div class="col-auto">
                <input name="url" type="text" class="form-control form-control-lg" placeholder="Ссылка">
            </div>
            <div class="form-text">Пример: https://www.example.com</div>
            <label class="form-label">Кастомизировать ссылку (необязательно)</label>
            <div class="col-auto">
                <input name="readablePart" type="text" class="form-control form-control-lg" placeholder="Читаемая часть">
            </div>
            <div class="form-text">Пример: avito-auto-be</div>
            <div class="col-auto">
                <button class="btn btn-primary col-auto" type="submit">Сгенерировать</button>
            </div>
        </div>
    </form>
    @if(page.getShortUrl() != null)
        <div class="row">
            <div class="col">
                <p>Твоя ссылка ${page.getShortUrl().getName()}</p>
            </div>
        </div>
    @endif
</div>
<footer class="footer border-top py-3 mt-5 bg-light">
    <div class="container-xl">
        <div class="text-center">
            Visit my GitHub profile
            <a href="https://github.com/Grad566" target="_blank">Click Here</a>
        </div>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
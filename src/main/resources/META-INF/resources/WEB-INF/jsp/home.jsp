<!DOCTYPE html>
<html lang="en">
<head>
    <title>URL Shortener</title>
    <style>
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
        
        body {
            display: flex;
            justify-content: center;
            font-family: Arial, sans-serif;
        }
        
        .container {
            text-align: center;
            max-width: 400px;
            margin-top: 50px;
        }
        
        h3 {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
        }
        
        form {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 10px;
            color: #555;
        }
        
        input[type="text"] {
            width: 100%;
            padding: 8px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 3px;
            margin-bottom: 10px;
            box-sizing: border-box;
        }
        
        button[type="submit"] {
            padding: 8px 16px;
            font-size: 14px;
            background-color: #4CAF50;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        
        div {
            margin-bottom: 10px;
        }
        
        span#shortLink {
            font-weight: bold;
            color: #4CAF50;
            text-decoration: underline;
            cursor: pointer;
        }
        
        a {
            color: #4CAF50;
            text-decoration: none;
        }
        
        a:hover {
            text-decoration: underline;
        }
        
        .database-link {
            margin-top: 30px;
        }
        
        .database-link a {
            font-weight: bold;
            color: #333;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .database-link a::after {
            content: "\1F449"; /* Cursor emoji (pointing finger) */
            margin-left: 5px;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h3>URL Shortener System</h3>
        <form id="urlForm" action="" method="post">
            <label for="originalLink">Original Link:</label>
            <input id="originalLink" type="text" name="originalLink">
            <br>
            <label for="customLink">Custom Link:</label>
            <input id="customLink" type="text" name="customLink">
            <br>
            <div class="button-container">
                <button type="submit">Shorten</button>
            </div>
        </form>
        <div><a href="http://localhost:8080/home/${shortLink}" target="_blank"><span id="shortLink">${shortLink}</span></a></div>
        <div class="database-link"><a href="http://localhost:8080/h2-console" target="_blank">Database</a></div>
    </div>
</body>
</html>

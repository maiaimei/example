const http = require('http');
const url = require('url');

const host = '192.168.1.101';
const port = 8080;
const serverUrl = `http://${host}:${port}`

const server = http.createServer((req, res) => {
  const urlObj = url.parse(req.url, true);
  if (urlObj.path === "/favicon.ico") {
    return;
  }

  const queryObj = urlObj.query;
  const name = queryObj.name;

  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end(`Hello, ${name}!`);
});

server.listen(port, host, () => {
  console.log(`Server running at ${serverUrl}`);
});
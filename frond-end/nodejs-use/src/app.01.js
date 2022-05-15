const http = require('http');

const host = '127.0.0.1';
const port = 8080;
const serverUrl = `http://${host}:${port}`

const server = http.createServer((req, res) => {
  const name = 'World'

  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end(`Hello, ${name}!`);
});

server.listen(port, host, () => {
  console.log(`Server running at ${serverUrl}`);
});
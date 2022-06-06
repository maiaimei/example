const config = require("./config");
config();

const Koa = require('koa');

const app = new Koa();

const registerMiddleware = require('./middleware');
registerMiddleware(app);

const registerRouter = require('./controller');
app.use(registerRouter());

const port = process.env.PORT || 7777;
const server = app.listen(port, () => {
    console.log(`Server is running at http://localhost:${port}`)
});

module.exports = server;
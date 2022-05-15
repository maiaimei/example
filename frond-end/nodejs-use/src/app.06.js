const Koa = require('koa');
const app = new Koa();

async function responseTime(ctx, next) {
    const start = Date.now();
    await next();
    const ms = Date.now() - start;
    ctx.set('X-Response-Time', `${ms}ms`);
    console.log(`${ctx.method} ${ctx.url} - ${ms}ms`);
}

app.use(responseTime);

app.use(ctx => {
    ctx.body = 'Hello Koa!';
});

app.listen(8080);
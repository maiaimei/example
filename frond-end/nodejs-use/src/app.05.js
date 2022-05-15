const Koa = require('koa');
const app = new Koa();

// Middleware normally takes two parameters (ctx, next), ctx is the context for one request,
// next is a function that is invoked to execute the downstream middleware. 
// It returns a Promise with a then function for running code after completion.
app.use(async (ctx, next) => {
    const start = Date.now();
    await next();
    const ms = Date.now() - start;
    console.log(`${ctx.method} ${ctx.url} - ${ms}ms`);
});

app.use(ctx => {
    ctx.body = 'Hello Koa';
});

app.listen(8080);
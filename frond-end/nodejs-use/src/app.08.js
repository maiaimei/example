const Koa = require('koa');
const app = new Koa();

// Combining multiple middleware with koa-compose
const compose = require('koa-compose');
async function errorHandler(ctx, next) {
    try {
        await next();
    } catch (err) {
        ctx.status = err.statusCode || err.status || 500;
        ctx.body = {
            message: err.message
        };
        ctx.app.emit("error", err, ctx);
    }
};
const all = compose([errorHandler]);
app.use(all);

app.use(ctx => {
    const path = ctx.path;
    switch (path) {
        case '/users': {
            ctx.body = 'users management';
            break;
        }
        case '/error1': {
            throw new Error('error1');
        }
        case '/error2':{
            ctx.throw(401, "error2");
            break;
        }
        case '/favicon.ico': {
            break;
        }
        default: {
            ctx.body = 'Hello Koa!';
            break;
        }
    }
});

app.on("error", (err, ctx) => {
    /** 
     * 异常统一处理:
     * 写入日志
     * 写入数据库
     * ...
     */
     const code = err.statusCode || err.status || 500;
     const message = err.message;
     console.log(`code: ${code}, message: ${message}`);
});

app.listen(8080);
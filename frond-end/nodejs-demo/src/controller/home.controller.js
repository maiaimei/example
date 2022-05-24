const Router = require('koa-router');
const router = new Router();

router.get('/', (ctx, next) => {
    ctx.body = "Hello, koa!"
});

router.get('/env', (ctx, next) => {
    ctx.body = process.env;
});

router.get('/login', (ctx, next) => {
    ctx.throw(500, "登录账号或密码错误！");
});

module.exports = router;
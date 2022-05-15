const Router = require('koa-router');
const router = new Router();

const userService = require("../service/user.service");

router.prefix('/users');

router.get('/', async (ctx, next) => {
    ctx.body = await userService.list();
});

router.get('/:id', async (ctx, next) => {
    let id = ctx.params.id;
    ctx.body = await userService.get(id);
});

router.post('/', async (ctx, next) => {
    let user = ctx.request.body;
    ctx.body = await userService.create(user);
});

router.put('/:id', async (ctx, next) => {
    let user = ctx.request.body;
    ctx.body = await userService.update(user);
});

router.del('/:id', async (ctx, next) => {
    let id = ctx.params.id;
    ctx.body = await userService.remove(id);
});

module.exports = router;
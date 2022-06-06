// Node's built-in assert
// const assert = require("assert");

const chai = require('chai');
//const assert = chai.assert;    // Using Assert style
const expect = chai.expect;    // Using Expect style
//const should = chai.should();  // Using Should style

const nock = require('nock');

const { v4: uuidv4 } = require('uuid');

const userService = require("../../src/service/user.service");

const user = {};

describe('userService', () => {
    before(function () {
        // runs once before the first test in this block
        user.id = uuidv4();
        user.nickname = user.id.replace(/-/g, "");
        user.username = user.nickname;
        user.passeord = user.username;
    });

    after(async function () {
        // runs once after the last test in this block

        // You can cleanup all the prepared mocks (could be useful to cleanup some state after a failed test) like this:
        nock.cleanAll();
    });

    beforeEach(function () {
        // runs before each test in this block
    });

    afterEach(function () {
        // runs after each test in this block
    });

    describe('#create', () => {
        it('actually makes request to the server', async () => {
            const result = await userService.create(user);
            expect(result).to.be.deep.equal(user);
        });

        // If you need some request on the same host name to be mocked 
        // and some others to really go through the HTTP stack, 
        // you can use the allowUnmocked option.
        it('goes through nock', async () => {
            nock(process.env.JSON_SERVER_URL, { allowUnmocked: true })
                .post('/users', user)
                .reply(200, user);
            const result = await userService.create(user);
            expect(result).to.be.deep.equal(user);
        });
    });

    describe('#update', () => {
        it('actually makes request to the server', async () => {
            user.username = "test";
            const result = await userService.update(user);
            expect(result).to.be.deep.equal(user);
        });

        it('goes through nock', async () => {
            user.username = "test";
            nock(process.env.JSON_SERVER_URL, { allowUnmocked: true })
                .put(`/users/${user.id}`, user)
                .reply(200, user);
            const result = await userService.update(user);
            expect(result).to.be.deep.equal(user);
        });
    });

    describe('#get', () => {
        it('actually makes request to the server', async () => {
            const result = await userService.get(user.id);
            expect(result).to.be.deep.equal(user);
        });

        it('goes through nock', async () => {
            nock(process.env.JSON_SERVER_URL, { allowUnmocked: true })
                .get(`/users/${user.id}`)
                .reply(200, user);
            const result = await userService.get(user.id);
            expect(result).to.be.deep.equal(user);
        });
    });

    describe('#list', () => {
        it('actually makes request to the server', async () => {
            const result = await userService.list();
            expect(result).to.be.not.null;
        });

        it('goes through nock', async () => {
            nock(process.env.JSON_SERVER_URL, { allowUnmocked: true })
                .get(`/users`)
                .reply(200, [user]);
            const result = await userService.list();
            expect(result).to.be.not.null;
        });
    });

    describe('#remove', () => {
        it('actually makes request to the server', async () => {
            const result = await userService.remove(user.id);
            expect(result).to.be.deep.equal({});
        });

        it('goes through nock', async () => {
            nock(process.env.JSON_SERVER_URL, { allowUnmocked: true })
                .delete(`/users/${user.id}`)
                .reply(200, {});
            const result = await userService.remove(user.id);
            expect(result).to.be.deep.equal({});
        });
    });
});
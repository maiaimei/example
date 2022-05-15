const nock = require('nock');
const chai = require('chai'),
    chaiHttp = require('chai-http'),
    expect = chai.expect;
chai.use(chaiHttp);

const app = require("../../src/app.09");
const user = {};

describe('userController', () => {
    before(function () {
        user.id = 'f105cec6-e3e1-436e-85b3-ff41103ebb3b';
        user.nickname = 'tester';
        user.username = 'tester';
        user.passeord = '123';
    });

    after(async function () {
        nock.cleanAll();
    });

    it('create', done => {
        nock(process.env.JSON_SERVER_URL)
            .post('/users', user)
            .reply(200, user);
        chai.request(app)
            .post(`/users`)
            .send(user)
            .set('X-Auth-Token', '123456')
            .end(function (err, res) {
                expect(res).to.have.status(200);
                expect(res.body.data).to.be.deep.equal(user);
                done();
            });
    });

    it('update', done => {
        nock(process.env.JSON_SERVER_URL)
            .put(`/users/${user.id}`, user)
            .reply(200, user);
        chai.request(app)
            .put(`/users/${user.id}`)
            .send(user)
            .set('X-Auth-Token', '123456')
            .end(function (err, res) {
                expect(res).to.have.status(200);
                expect(res.body.data).to.be.deep.equal(user);
                done();
            });

    });

    it('delete', done => {
        nock(process.env.JSON_SERVER_URL)
            .delete(`/users/${user.id}`)
            .reply(200, {});
        chai.request(app)
            .del(`/users/${user.id}`)
            .set('X-Auth-Token', '123456')
            .end(function (err, res) {
                expect(res).to.have.status(200);
                expect(res.body.data).to.be.deep.equal({});
                done();
            });
    });

    it('get', done => {
        nock(process.env.JSON_SERVER_URL)
            .get(`/users/${user.id}`)
            .reply(200, user);
        chai.request(app)
            .get(`/users/${user.id}`)
            .set('X-Auth-Token', '123456')
            .end(function (err, res) {
                expect(res).to.have.status(200);
                expect(res.body.data).to.be.deep.equal(user);
                done();
            });
    });

    it('list', done => {
        nock(process.env.JSON_SERVER_URL)
            .get(`/users`)
            .reply(200, [user]);
        chai.request(app)
            .get(`/users`)
            .set('X-Auth-Token', '123456')
            .end(function (err, res) {
                expect(res).to.have.status(200);
                expect(res.body.data).to.be.deep.equal([user]);
                done();
            });
    });
});
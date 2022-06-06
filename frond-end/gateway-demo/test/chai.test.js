const chai = require('chai');  
const assert = chai.assert;    // Using Assert style
const expect = chai.expect;    // Using Expect style
const should = chai.should();  // Using Should style

const foo = 'bar',
    beverages = { tea: ['chai', 'matcha', 'oolong'] },
    one = 1;

describe('chai', () => {
    describe('#expect', () => {
        it('check type', () => {
            expect(foo).to.be.a('string');
            expect(one).to.be.a('number');
            expect(one).to.not.be.a('string');
        });

        it('check value', () => {
            expect(foo).to.equal('bar');
            expect(one).to.equal(1);
            expect(beverages).to.deep.equal({ tea: ['chai', 'matcha', 'oolong'] });
        });

        it('check length', () => {
            expect(foo).to.have.lengthOf(3);
            expect(beverages).to.have.property('tea').with.lengthOf(3);
        });

        it('check property', () => {
            expect(beverages).to.have.property('tea');
        });
    });

    describe('#should', () => {
        it('should', () => {
            foo.should.be.a('string');
            foo.should.equal('bar');
            foo.should.have.lengthOf(3);
            beverages.should.have.property('tea').with.lengthOf(3);
        });
    });
});

fixture `firstTest`
    .page `http://localhost:3000`;

test
    .page `http://localhost:3000`
    ('My test', async t => {
        console.log("testcafe first test for hnweb")
    });

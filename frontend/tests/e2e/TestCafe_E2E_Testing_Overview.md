# TestCafe E2E Testing - Overview & Status

## What is TestCafe?

TestCafe is an end-to-end (E2E) testing framework that automates browser interactions to test the application as a real user would. It launches a browser, navigates pages, clicks buttons, fills forms, and verifies expected outcomes without requiring browser plugins or external dependencies.

## Purpose

The TestCafe test suite serves multiple critical purposes:

- **Validate critical user workflows** - Login, eligibility checks, group member operations, patient registration
- **Catch regressions before deployment** - Automated tests run before each release to ensure existing functionality hasn't broken
- **Ensure authentication and authorization work correctly** - Tests verify that different authentication methods (IDIR, Keycloak, BCeID, Health Authority ID) function as expected
- **Verify integration between frontend and backend services** - End-to-end validation of the full application stack
- **Reduce manual testing burden** - Automated tests free up QA resources for exploratory and edge-case testing

## Test Coverage

Our E2E test suite includes comprehensive coverage across key business flows:

### Authentication & Access
- Welcome/Login page functionality
- Multiple authentication methods (IDIR, Keycloak, BCeID Business, BC Services Card, Health Authority ID)
- User permissions and role-based access control

### Core Business Operations
- **Eligibility Checking** - Verify MSP eligibility for individuals and groups
- **Group Member Management** - Add, update, and query group member information
- **MSP Contract Operations** - Contract inquiries, period retrieval, address updates
- **Patient Registration** - Name searches, person details, visa resident registration
- **Maintenance Operations** - Coverage renewal, reinstatement, cancellation, effective date changes

### Supporting Functions
- Audit reporting and transaction tracking
- Help documentation access
- Error handling and user feedback

## Current Challenges

### Technical Incompatibility

TestCafe is currently incompatible with Keycloak version 26.x due to architectural differences:

**Root Cause:**
- Keycloak 26.x enforces PKCE (Proof Key for Code Exchange) security
- PKCE requires the Web Crypto API (`window.crypto.subtle`)
- TestCafe's proxy architecture breaks the secure context needed for Web Crypto API
- This occurs even on HTTPS environments due to how TestCafe proxies connections

**Investigation Findings:**
- The issue affects both local (HTTP) and deployed environments (HTTPS)
- Likely occurred after a keycloak-js library upgrade from an older version (pre-20.x) to 26.x
- Downgrading to keycloak-js 15.x resolves the Web Crypto error but breaks authentication redirects
- Conditional PKCE logic doesn't work because the error occurs during Keycloak initialization, before we can handle it

**Impact:**
- Automated E2E tests cannot run
- Authentication workflows cannot be validated automatically
- Regression testing requires manual effort

## Value Despite Current Issues

Even though automated tests aren't currently running, the test code base provides significant value:

### 1. Test Documentation
- Test scenarios document expected behavior and serve as acceptance criteria
- Each test file describes user workflows and business logic
- Provides clear examples of how features should work

### 2. Manual Testing Guide
- QA teams can use test steps as structured manual test scripts
- Test selectors identify exact UI elements to interact with
- Expected outcomes are clearly defined in assertions

### 3. Framework Portability
- Test logic is framework-agnostic and can be ported to:
  - **Playwright** - Modern alternative with better Keycloak support
  - **Cypress** - Popular choice with strong community
  - **Selenium-based frameworks** - If needed for specific requirements
- Core test scenarios remain valuable regardless of tooling

### 4. Future Regression Prevention
- Once resolved, provides automated safety net for future changes
- Prevents breaking changes from reaching production
- Reduces cost and time of manual regression testing

### 5. Knowledge Repository
- Captures institutional knowledge about critical workflows
- New team members can understand business processes through tests
- Serves as living documentation that evolves with the application

## Investigation Summary

### What We Discovered
1. TestCafe worked previously with an older Keycloak version
2. Library upgrade introduced incompatibility
3. Issue is architectural, not configuration-based
4. Affects all environments (local and deployed)

### Attempted Solutions
- ✗ Downgrading keycloak-js - breaks authentication
- ✗ Conditional PKCE handling - error occurs too early
- ✗ Different browsers (Chrome, Firefox, Edge) - same issue across all
- ✗ HTTPS vs HTTP - TestCafe proxy breaks both

### Viable Solutions
1. **Proper investigation and code refactoring** - Most robust but requires time
2. **Switch to Playwright/Cypress** - Modern frameworks handle this better
3. **Disable authentication for E2E** - Tests functionality but not auth flows
4. **Configure Keycloak server** - Remove PKCE requirement for test client (if permitted)

## Recommended Next Steps

### Short Term
- Continue using test scenarios as manual test scripts
- Document the incompatibility as a known issue
- Create backlog story for proper investigation

### Medium Term
- Evaluate alternative testing frameworks (Playwright recommended)
- Pilot new framework with subset of critical tests
- Plan migration timeline if alternative proves viable

### Long Term
- Migrate full test suite to compatible framework
- Re-establish automated E2E testing in CI/CD pipeline
- Expand test coverage with confidence in stable foundation

## Conclusion

While TestCafe is currently blocked due to Keycloak compatibility issues, the test suite remains a valuable asset. The test scenarios document critical workflows, guide manual testing, and can be migrated to modern frameworks. Resolving this requires dedicated investigation time rather than quick fixes, but the investment will restore automated regression testing capabilities.



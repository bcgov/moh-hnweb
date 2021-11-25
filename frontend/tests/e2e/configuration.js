const DEFAULT_SITE_UNDER_TEST = 'http://localhost:3000';
export let SITE_UNDER_TEST

if (process.env.HNWEB_APP_URL_TO_TEST) {
    SITE_UNDER_TEST = process.env.HNWEB_APP_URL_TO_TEST
} else {
    SITE_UNDER_TEST = DEFAULT_SITE_UNDER_TEST
}
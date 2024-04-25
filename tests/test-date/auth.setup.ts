import { test as setup, expect } from "@playwright/test";
 
const authFile = 'playwright/.auth/user.json';

setup('authenticate', async ({ page }) => {
    await page.goto('http://localhost:8080/product-management-ex/login');
    await page.getByLabel('mail address').click();
    await page.getByLabel('mail address').fill('test1@example.com');
    await page.getByLabel('mail address').click();
    await page.getByLabel('password').click();
    await page.getByLabel('password').fill('Test1234');
    await page.getByRole('button', { name: 'Login' }).click();

    await page.context().storageState({ path: authFile });
});
 
// login.js

const login = async (page, email, password) => {
    await page.goto('http://localhost:8080/product-management-ex/login');
    await page.getByLabel('mail address').click();
    await page.getByLabel('mail address').fill(email);
    await page.getByLabel('mail address').click();
    await page.getByLabel('password').click();
    await page.getByLabel('password').fill(password);
    await page.getByRole('button', { name: 'Login' }).click();
  };
  
  module.exports = { login };
  
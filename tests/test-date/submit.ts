// submit-helper.ts

export async function submitNewItem(page, itemData) {
    await page.getByRole('link', { name: ' Add New Item' }).click();
    await fillItemForm(page, itemData);
  }
  
  async function fillItemForm(page, itemData) {
    await page.getByLabel('parent category').selectOption(itemData.parentCategory);
    await page.getByLabel('child category').selectOption(itemData.childCategory);
    await page.getByLabel('grand category').selectOption(itemData.grandCategory);
    await page.getByLabel('brand').fill(itemData.brand);
    if (itemData.image) {
      await page.getByLabel('image').fill(itemData.image);
    }
    await page.getByRole('button', { name: 'Submit' }).click();
  }
  
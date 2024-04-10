// submit-helper.ts

export async function submitNewItem(page, itemData) {
  await page.getByRole('link', { name: ' Add New Item' }).click();
  await fillItemForm(page, itemData);
}

async function fillItemForm(page, itemData) {

  //name
  if (itemData.name !== undefined && itemData.name !== null && itemData.name !== '') {
    await page.getByLabel('name').fill(itemData.name);
  }


  // price
  // if (itemData.price !== undefined && itemData.price !== null && typeof itemData.price === 'number') {
  //   await page.getByLabel('price').fill(itemData.price.toString());
  // }

  // price
  if (itemData.price !== undefined && itemData.price !== null && itemData.price.trim() !== '') {
    await page.getByLabel('price').evaluate((element, value) => {
      element.value = value;
    }, itemData.price);
  }


  // parent category
  if (itemData.parentCategory !== undefined && itemData.parentCategory !== null && itemData.parentCategory.trim() !== '') {
    await page.getByLabel('parent category').selectOption(itemData.parentCategory);
  }

  // child category
  if (itemData.childCategory !== undefined && itemData.childCategory !== null && itemData.childCategory.trim() !== '') {
    await page.getByLabel('child category').selectOption(itemData.childCategory);
  }

  // grand category
  if (itemData.grandCategory !== undefined && itemData.grandCategory !== null && itemData.grandCategory.trim() !== '') {
    await page.getByLabel('grand category').selectOption(itemData.grandCategory);
  }

  await page.getByLabel('brand').fill(itemData.brand);

  //condition
  if (itemData.condition !== undefined && itemData.condition !== null && itemData.condition !== '') {
    await page.check(`#condition${itemData.condition}`);
  }

  //image
  if (itemData.image) {
    await page.getByLabel('image').fill(itemData.image);
  }
  await page.getByRole('button', { name: 'Submit' }).click();
}

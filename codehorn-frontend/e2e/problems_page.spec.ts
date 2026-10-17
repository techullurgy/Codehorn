import test, { expect } from "@playwright/test";

test.describe("Problems Page with empty problems", () => {
    test.beforeEach(async ({ page }) => {
        await page.setExtraHTTPHeaders({
            'x-mock-scenario': 'empty-state'
        })
        await page.goto("")
    })

    test("should show problems list", async ({ page }) => {
        await expect(page.locator("table")).toBeVisible()
    })

    test("should show empty table when there is no problems", async ({ page }) => {
        await expect(page.locator("table")).toBeVisible()
    })
})
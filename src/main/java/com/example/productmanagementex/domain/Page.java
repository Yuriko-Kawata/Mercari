package com.example.productmanagementex.domain;

public class Page {
    // 現在のページ
    private int currentPage;
    // 前のページ
    private int pageFrom;
    // 次のページ
    private int pageTo;
    // 1ページあたりの件数
    private int recordPerPage;
    // 表示される商品の件数
    private int totalRecordCount;
    // ページの総数
    private int totalPageCount;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageFrom() {
        return pageFrom;
    }

    public void setPageFrom(int pageFrom) {
        this.pageFrom = pageFrom;
    }

    public int getPageTo() {
        return pageTo;
    }

    public void setPageTo(int pageTo) {
        this.pageTo = pageTo;
    }

    public int getRecordPerPage() {
        return recordPerPage;
    }

    public void setRecordPerPage(int recordPerPage) {
        this.recordPerPage = recordPerPage;
    }

    public int getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    /**
     * 総ページ数の計算
     * 
     * @param recordPerPage    1ページあたりの件数
     * @param totalRecordCount 商品総数
     */
    public Page(int recordPerPage, int totalRecordCount) {
        this.recordPerPage = recordPerPage;
        this.totalRecordCount = totalRecordCount;

        if (this.totalRecordCount % this.recordPerPage > 0) {
            this.totalPageCount = this.totalRecordCount / this.recordPerPage + 1;
        } else {
            this.totalPageCount = this.totalRecordCount / this.recordPerPage;
        }
    }

    public void moveTo(int page) {
        this.currentPage = page;
        this.pageFrom = Math.max(page - 1, 2);
        if (this.totalPageCount > 3) {
            this.pageTo = Math.min(this.pageFrom + 2, this.totalPageCount - 1);
        } else {
            this.pageTo = this.totalPageCount;
        }
        this.pageFrom = Math.max(this.pageTo - 2, 2);
    }

    @Override
    public String toString() {
        return "Page [currentPage=" + currentPage + ", pageFrom=" + pageFrom + ", pageTo=" + pageTo + ", recordPerPage="
                + recordPerPage + ", totalRecordCount=" + totalRecordCount + ", totalPageCount=" + totalPageCount + "]";
    }
}

package com.ust.bookt.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jace on 3/6/2017.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BookBean {

    private String bookAuthor;
    private String bookContactNumber;
    private String bookDesc;
    private String bookPostStatus;
    private String bookPriceEnd;
    private String bookPriceStart;
    private String bookSeller;
    private String bookStatus;
    private String bookTitle;
    private String bookLocation;
    private String bookReservedBy;
    private String bookImgUrl;

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookContactNumber() {
        return bookContactNumber;
    }

    public void setBookContactNumber(String bookContactNumber) { this.bookContactNumber = bookContactNumber;}

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getBookPostStatus() {
        return bookPostStatus;
    }

    public void setBookPostStatus(String bookPostStatus) {
        this.bookPostStatus = bookPostStatus;
    }

    public String getBookPriceEnd() {
        return bookPriceEnd;
    }

    public void setBookPriceEnd(String bookPriceEnd) {
        this.bookPriceEnd = bookPriceEnd;
    }

    public String getBookPriceStart() {
        return bookPriceStart;
    }

    public void setBookPriceStart(String bookPriceStart) {
        this.bookPriceStart = bookPriceStart;
    }

    public String getBookSeller() {
        return bookSeller;
    }

    public void setBookSeller(String bookSeller) {
        this.bookSeller = bookSeller;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookLocation() { return bookLocation; }

    public void setBookLocation(String bookLocation) { this.bookLocation = bookLocation; }

    public String getBookReservedBy() { return bookReservedBy; }

    public void setBookReservedBy(String bookReservedBy) { this.bookReservedBy = bookReservedBy; }

    public String getBookImgUrl() { return bookImgUrl; }

    public void setBookImgUrl(String bookImgUrl) { this.bookImgUrl = bookImgUrl; }
}

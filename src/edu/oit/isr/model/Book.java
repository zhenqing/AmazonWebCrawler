package edu.oit.isr.model;

import java.util.Date;

public class Book {
	private String title;
	private String bookUrl;
	private String isbn;
	private int rank;
	private String catagory;
	private String author;
	private String imgUrl;
	private int reviews;
	private String bindingPlatform;
	private float amazonPrice;
	private int sellers;
	private float lowestUsedPrice;
	private Date date;
	private String releaseDate;
	
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String toString(){
		String str = getIsbn()+",rank:"+getRank()+",title:"+getTitle()+",isbn:"+getIsbn()+",authour:"+getAuthor()+",releaseDate:"+getReleaseDate()+",reviews:"+getReviews()+",binding:"+getBindingPlatform()
				+",amazonPrice:"+getAmazonPrice()+",sellers:"+getSellers()+",lowest price:"+getLowestUsedPrice();
		return str;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getCatagory() {
		return catagory;
	}
	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getReviews() {
		return reviews;
	}
	public void setReviews(int reviews) {
		this.reviews = reviews;
	}
	public String getBindingPlatform() {
		return bindingPlatform;
	}
	public void setBindingPlatform(String bindingPlatform) {
		this.bindingPlatform = bindingPlatform;
	}
	public float getAmazonPrice() {
		return amazonPrice;
	}
	public void setAmazonPrice(float amazonPrice) {
		this.amazonPrice = amazonPrice;
	}
	public int getSellers() {
		return sellers;
	}
	public void setSellers(int sellers) {
		this.sellers = sellers;
	}
	public float getLowestUsedPrice() {
		return lowestUsedPrice;
	}
	public void setLowestUsedPrice(float lowestUsedPrice) {
		this.lowestUsedPrice = lowestUsedPrice;
	}
	

	public String getBookUrl() {
		return bookUrl;
	}
	public void setBookUrl(String bookUrl) {
		this.bookUrl = bookUrl;
	}
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
